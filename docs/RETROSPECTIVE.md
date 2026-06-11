# 리팩토링 회고

## 개요

`Assemble.java` 단일 클래스에 모든 로직이 집중되어 있던 코드를 5단계에 걸쳐 클린 코드 원칙에 따라 구조를 개선하였습니다.

---

## 리팩토링 전/후 비교

### Before

```
src/main/java/
└── Assemble.java  ← 271줄, 모든 책임 혼재
```

- UI 출력, 입력 파싱, 유효성 검사, 상태 관리가 한 클래스에 집중
- `int` 상수(`SEDAN = 1`, `GM = 1`)로 의미 없는 숫자가 코드 전반에 산재
- `stack[]` 배열의 인덱스가 무엇을 의미하는지 코드를 읽어야만 파악 가능
- 동일한 제약 조건 로직이 `isValidCheck()`와 `testProducedCar()` 두 곳에 중복

### After

```
src/main/java/
├── Assemble.java          — 흐름 제어
├── view/ConsoleView.java  — 출력
├── handler/InputHandler.java — 입력
├── domain/Car.java        — 상태 보관
├── domain/CarValidator.java — 유효성 검사
└── enums/ (4개)           — 타입 정의
```

- 각 클래스가 하나의 책임만 가짐 (SRP)
- 숫자 대신 Enum으로 의도가 드러나는 코드
- 중복 제거: 제약 조건이 `CarValidator` 한 곳에만 존재
- 테스트 코드(`AssembleValidationTest`)가 `CarValidator`를 직접 검증

---

## 단계별 회고

### Phase 1 — 테스트 먼저 작성

테스트를 가장 먼저 작성한 것이 이후 리팩토링 전 단계에서 실질적인 안전망이 되었습니다.
`isValidCheck()`가 `static` 필드에 직접 의존하고 있어서 테스트 접근을 위해 `package-private`으로 접근 제한자를 변경하는 최소한의 수정이 필요했습니다. 이 경험을 통해 테스트하기 어려운 구조 자체가 설계의 문제를 드러낸다는 점을 확인할 수 있었습니다.

### Phase 2 — Enum 도입

`int` 상수를 Enum으로 교체하는 것만으로도 `isValidCheck()` 코드의 가독성이 눈에 띄게 개선되었습니다.

```java
// Before
if (stack[0] == 1 && stack[2] == 2) return false;

// After
if (car.getCarType() == CarType.SEDAN && car.getBrakeType() == BrakeType.CONTINENTAL) return false;
```

이름 하나가 주석보다 더 많은 것을 설명한다는 사실을 체감한 단계였습니다.

### Phase 3 — 도메인 모델 분리

`stack[]` 배열을 `Car` 객체로 교체하면서 테스트 코드도 함께 개선되었습니다.
기존에는 `setup(1, 1, 2, 1)`처럼 숫자를 나열해야 했지만, 이후에는 `setup(CarType.SEDAN, EngineType.GM, BrakeType.CONTINENTAL, SteeringType.BOSCH)`처럼 의도가 명확하게 드러났습니다. 프로덕션 코드를 개선하면 테스트 코드도 함께 좋아진다는 점이 인상적이었습니다.

### Phase 4 — 유효성 검사 통합

`isValidCheck()`와 `testProducedCar()`의 중복을 제거하고 `CarValidator`로 통합하면서 두 가지 효과가 있었습니다.
첫째, 제약 조건이 변경되더라도 수정할 곳이 하나로 줄었습니다.
둘째, 테스트가 `Assemble` 클래스에 의존하지 않는 순수한 단위 테스트로 바뀌었습니다. 이 단계에서 중복 제거가 단순히 코드량을 줄이는 것이 아니라, 변경의 영향 범위를 줄이는 것임을 다시 확인하였습니다.

### Phase 5 — UI 분리

가장 많은 코드가 이동한 단계였습니다. `ConsoleView`와 `InputHandler`로 분리한 뒤 `Assemble.java`에는 `System.out.print`와 `Integer.parseInt`가 단 한 줄도 남지 않았습니다. 결과적으로 `Assemble.java`는 단계 전환 흐름만 담당하게 되어, 코드를 읽는 사람이 전체 흐름을 한 곳에서 파악할 수 있게 되었습니다.

---

## 잘된 점

- **테스트 먼저 작성**: Phase 1에서 작성한 8개 테스트가 이후 4단계의 리팩토링 동안 단 한 번도 실패하지 않았습니다. 안전하게 구조를 변경할 수 있었던 핵심 이유입니다.
- **단계 분리**: 한 번에 모든 것을 바꾸지 않고 단계별로 진행하여, 각 단계에서 빌드와 테스트를 확인하며 문제를 즉시 발견할 수 있었습니다.
- **설계 문서 선행**: `PLAN.md`와 `docs/design/Phase#.md`를 먼저 작성하고 개발에 들어간 덕분에 각 단계의 목표와 완료 기준이 명확했습니다.

## 아쉬운 점

- **`InputHandler`의 step 의존**: `InputHandler.isValidRange()`가 step 정수값(0~4)을 직접 받아 처리합니다. step을 Enum으로 분리했다면 `InputHandler`와 `Assemble` 간의 결합도를 더 낮출 수 있었습니다.
- **`delay()` 위치**: `delay()`는 UI 타이밍에 관한 관심사이지만 `Assemble.java`에 남아 있습니다. `ConsoleView`로 이동하거나 별도로 분리하는 것이 더 일관된 구조였을 것입니다.

## 배운 점

> 테스트 코드는 리팩토링의 안전망이고, 좋은 이름은 가장 저렴한 문서이며, 중복은 변경 비용을 두 배로 만든다.
