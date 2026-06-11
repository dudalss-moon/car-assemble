# Phase 3. 도메인 모델 분리

## 목적

익명 배열 `stack[]`을 의미 있는 필드를 가진 `Car` 객체로 교체하여, 조립 상태 관리 책임을 단일 클래스에 부여합니다.

---

## 현재 문제

```java
// 인덱스가 무엇을 의미하는지 코드를 읽어야만 알 수 있음
private static int[] stack = new int[5];

stack[0] = carType;
stack[1] = engine;
stack[2] = brake;
stack[3] = steering;
```

---

## 설계

### 파일 구조

```
src/main/java/
├── Assemble.java
├── domain/
│   └── Car.java
└── enums/
    ├── CarType.java
    ├── EngineType.java
    ├── BrakeType.java
    └── SteeringType.java
```

### Car 클래스

```java
public class Car {
    private CarType carType;
    private EngineType engineType;
    private BrakeType brakeType;
    private SteeringType steeringType;

    public CarType getCarType()           { return carType; }
    public EngineType getEngineType()     { return engineType; }
    public BrakeType getBrakeType()       { return brakeType; }
    public SteeringType getSteeringType() { return steeringType; }

    public void setCarType(CarType carType)           { this.carType = carType; }
    public void setEngineType(EngineType engineType)  { this.engineType = engineType; }
    public void setBrakeType(BrakeType brakeType)     { this.brakeType = brakeType; }
    public void setSteeringType(SteeringType steeringType) { this.steeringType = steeringType; }
}
```

> `Car`는 데이터 보관만 담당합니다. 유효성 검사 로직은 Phase 4의 `CarValidator`가 담당합니다.

---

## stack[] → Car 매핑

| 기존 | 변경 후 |
|------|--------|
| `stack[0] = answer` | `car.setCarType(CarType.from(answer))` |
| `stack[1] = answer` | `car.setEngineType(EngineType.from(answer))` |
| `stack[2] = answer` | `car.setBrakeType(BrakeType.from(answer))` |
| `stack[3] = answer` | `car.setSteeringType(SteeringType.from(answer))` |
| `stack[CarType_Q] == SEDAN` | `car.getCarType() == CarType.SEDAN` |

---

## Assemble.java 변경 범위

- `private static int[] stack` 제거
- `private static Car car = new Car()` 추가
- `selectCarType()`, `selectEngine()`, `selectBrakeSystem()`, `selectSteeringSystem()` 내부의 `stack[]` 대입을 `car.set...()` 호출로 교체
- `isValidCheck()`, `runProducedCar()`, `testProducedCar()` 내부의 `stack[]` 참조를 `car.get...()` 호출로 교체

---

## 완료 기준

- [ ] `Car` 클래스 생성
- [ ] `stack[]` 배열 완전 제거
- [ ] `Car` 객체만으로 조립 상태 관리
- [ ] Phase 1 테스트 GREEN 유지
- [ ] `./gradlew build` 통과
