# PLAN.md — 리팩토링 단계별 계획

> 각 단계는 `AGENTS.md`의 클린 코드 원칙을 기준으로 작성합니다.
> **테스트를 먼저 작성하고, 테스트가 GREEN인 상태에서 리팩토링을 진행합니다.**

## 전체 흐름

```
Phase 1. 테스트 먼저 작성   ← 리팩토링 전 현재 동작 고정
  └─ Phase 2. Enum 도입
       └─ Phase 3. 도메인 모델 분리
            └─ Phase 4. 유효성 검사 통합
                 └─ Phase 5. UI 분리
```

---

## Phase 1. 테스트 먼저 작성

**목적**: 리팩토링 전에 현재 동작을 테스트로 고정하여 이후 변경의 안전망을 확보

**적용 원칙**: 테스트 코드는 필수다 / 중복은 제거한다

> 현재 코드(`Assemble.java`)에서 비즈니스 로직에 해당하는 부분을 대상으로 테스트를 작성합니다.
> 테스트 대상은 UI(메뉴 출력)가 아닌 **제약 조건 검사 로직**입니다.

**작업 목록**
- [ ] `AssembleValidationTest` 작성 — `isValidCheck()` 동작 검증
  - 유효한 부품 조합 → 정상 동작 확인
  - 제약 조건 5가지 위반 조합 → 각각 실패 확인
    - Sedan + CONTINENTAL 제동장치
    - SUV + TOYOTA 엔진
    - Truck + WIA 엔진
    - Truck + MANDO 제동장치
    - BOSCH 제동장치 + 타사 조향장치
  - 반복되는 테스트 준비 코드는 헬퍼 메서드로 추출하여 중복 제거
- [ ] 테스트 전체 통과 확인 (`./gradlew test`)

**완료 기준**
- 모든 테스트 GREEN
- 제약 조건 5가지 케이스 전부 커버
- 이후 Phase에서 이 테스트가 계속 GREEN을 유지해야 함

---

## Phase 2. Enum 도입

**목적**: int 상수를 의미 있는 이름을 가진 타입으로 교체

**적용 원칙**: 이름에는 의미가 있어야 한다

```java
// Before
private static final int SEDAN = 1, SUV = 2, TRUCK = 3;

// After
public enum CarType { SEDAN, SUV, TRUCK }
```

**작업 목록**
- [ ] `CarType` enum 생성 — `SEDAN`, `SUV`, `TRUCK`
- [ ] `EngineType` enum 생성 — `GM`, `TOYOTA`, `WIA`, `BROKEN`
- [ ] `BrakeType` enum 생성 — `MANDO`, `CONTINENTAL`, `BOSCH`
- [ ] `SteeringType` enum 생성 — `BOSCH`, `MOBIS`
- [ ] `Assemble.java`의 int 상수를 Enum 참조로 교체
- [ ] Phase 1 테스트 GREEN 확인

**완료 기준**
- `private static final int` 상수 전부 제거
- Phase 1 테스트 GREEN 유지
- 빌드 통과 (`./gradlew build`)

---

## Phase 3. 도메인 모델 분리

**목적**: 익명 배열(`stack[]`)을 의미 있는 객체로 교체하고, 조립 상태 관리 책임을 단일 클래스에 부여

**적용 원칙**: 이름에는 의미가 있어야 한다 / 클래스는 하나의 책임을 갖는다 (SRP)

```java
// Before
private static int[] stack = new int[5];

// After
Car car = new Car();  // 조립 상태 보관 책임
```

**작업 목록**
- [ ] `Car` 클래스 생성 — `CarType`, `EngineType`, `BrakeType`, `SteeringType` 필드 보유
- [ ] `Assemble.java`의 `stack[]` 배열을 `Car` 인스턴스로 교체
- [ ] `Car`의 각 필드명은 역할을 명확히 드러내도록 작성
- [ ] Phase 1 테스트 GREEN 확인

**완료 기준**
- `stack[]` 배열 완전 제거
- `Car` 객체만으로 조립 상태 관리
- Phase 1 테스트 GREEN 유지
- 빌드 통과

---

## Phase 4. 유효성 검사 통합

**목적**: 두 곳에 흩어진 동일한 제약 조건 로직을 하나로 통합하고, 각 제약을 독립된 메서드로 분리

**적용 원칙**: 중복은 제거한다 / 함수는 한 가지 역할만 담당한다 / 클래스는 하나의 책임을 갖는다 (SRP)

```java
// Bad: isValidCheck()와 testProducedCar()에 동일 로직 중복
// Good: CarValidator 한 곳에서 제약 조건별 메서드로 분리
public class CarValidator {
    public String validate(Car car) { ... }

    private boolean isSedanWithContinental(Car car) { ... }
    private boolean isSuvWithToyota(Car car) { ... }
    // ...
}
```

**작업 목록**
- [ ] `CarValidator` 클래스 생성
- [ ] 제약 조건 5가지를 각각 독립된 private 메서드로 분리
- [ ] `validate(Car car)` — 위반된 제약 조건 메시지 반환, 없으면 `null`
- [ ] `Assemble.java`에서 `isValidCheck()` / `testProducedCar()` 제거 후 `CarValidator` 위임
- [ ] Phase 1 테스트 GREEN 확인

**완료 기준**
- 제약 조건 코드가 `CarValidator` 한 곳에만 존재
- 각 제약 조건이 이름으로 의미를 알 수 있는 메서드로 분리
- Phase 1 테스트 GREEN 유지
- 빌드 통과

---

## Phase 5. UI 분리

**목적**: 메뉴 출력과 입력 처리를 비즈니스 로직에서 분리하여 각 클래스가 하나의 책임만 갖도록 구성

**적용 원칙**: 클래스는 하나의 책임을 갖는다 (SRP) / 가독성 우선 / 함수는 한 가지 역할만 담당한다

| 분리 전 (`Assemble.java`) | 분리 후 |
|--------------------------|---------|
| 메뉴 출력 메서드 5개 | `ConsoleView` |
| 입력 파싱 / 범위 검증 / exit 처리 | `InputHandler` |
| 흐름 제어 (`while` + `switch`) | `Assemble.main()` |

**작업 목록**
- [ ] `ConsoleView` 클래스 생성 — 메뉴 출력 메서드 이동
  - 각 메서드는 출력만 담당하며 반환값 없음
- [ ] `InputHandler` 클래스 생성 — 입력 파싱 / 범위 검증 / `exit` 처리
  - 입력 처리 결과는 명확한 이름의 값으로 반환
- [ ] `Assemble.main()`은 흐름 제어(단계 전환)만 담당하도록 정리
- [ ] Phase 1 테스트 GREEN 확인

**완료 기준**
- `Assemble.java`에 `System.out.print` / 입력 파싱 코드 없음
- 각 클래스를 읽었을 때 역할이 이름만으로 파악 가능
- Phase 1 테스트 GREEN 유지
- 빌드 통과
