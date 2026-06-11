# Phase 4. 유효성 검사 통합

## 목적

`isValidCheck()`와 `testProducedCar()`에 흩어진 동일한 제약 조건 로직을 `CarValidator` 한 곳으로 통합하고, 제약 조건마다 독립된 메서드로 분리합니다.

---

## 현재 문제

동일한 제약 조건이 두 메서드에 중복 존재합니다.

```java
// isValidCheck() — boolean 반환
private static boolean isValidCheck() {
    if (stack[CarType_Q] == SEDAN && stack[BrakeSystem_Q] == CONTINENTAL) return false;
    if (stack[CarType_Q] == SUV   && stack[Engine_Q] == TOYOTA)           return false;
    ...
}

// testProducedCar() — 메시지 출력
private static void testProducedCar() {
    if (stack[CarType_Q] == SEDAN && stack[BrakeSystem_Q] == CONTINENTAL) {
        fail("Sedan에는 Continental제동장치 사용 불가");
    } else if (stack[CarType_Q] == SUV && stack[Engine_Q] == TOYOTA) {
        fail("SUV에는 TOYOTA엔진 사용 불가");
    }
    ...
}
```

---

## 설계

### 파일 구조

```
src/main/java/
├── Assemble.java
├── domain/
│   ├── Car.java
│   └── CarValidator.java
└── enums/
    └── ...
```

### CarValidator 클래스

```java
public class CarValidator {

    public String validate(Car car) {
        if (isSedanWithContinental(car))       return "Sedan에는 Continental 제동장치 사용 불가";
        if (isSuvWithToyota(car))              return "SUV에는 TOYOTA 엔진 사용 불가";
        if (isTruckWithWia(car))               return "Truck에는 WIA 엔진 사용 불가";
        if (isTruckWithMando(car))             return "Truck에는 Mando 제동장치 사용 불가";
        if (isBoschBrakeWithNonBoschSteering(car)) return "Bosch 제동장치에는 Bosch 조향장치 이외 사용 불가";
        return null; // 제약 조건 위반 없음
    }

    private boolean isSedanWithContinental(Car car) {
        return car.getCarType() == CarType.SEDAN
            && car.getBrakeType() == BrakeType.CONTINENTAL;
    }

    private boolean isSuvWithToyota(Car car) {
        return car.getCarType() == CarType.SUV
            && car.getEngineType() == EngineType.TOYOTA;
    }

    private boolean isTruckWithWia(Car car) {
        return car.getCarType() == CarType.TRUCK
            && car.getEngineType() == EngineType.WIA;
    }

    private boolean isTruckWithMando(Car car) {
        return car.getCarType() == CarType.TRUCK
            && car.getBrakeType() == BrakeType.MANDO;
    }

    private boolean isBoschBrakeWithNonBoschSteering(Car car) {
        return car.getBrakeType() == BrakeType.BOSCH
            && car.getSteeringType() != SteeringType.BOSCH;
    }
}
```

---

## validate() 반환 규칙

| 상태 | 반환값 |
|------|--------|
| 제약 조건 위반 없음 | `null` |
| 제약 조건 위반 있음 | 위반 메시지 문자열 |

호출부에서는 `null` 여부로 분기합니다.

```java
String violation = validator.validate(car);
if (violation != null) {
    System.out.println("자동차가 동작되지 않습니다");   // RUN 경로
    // 또는
    fail(violation);                                  // Test 경로
}
```

---

## Assemble.java 변경 범위

- `isValidCheck()` 제거 → `validator.validate(car) == null` 으로 대체
- `testProducedCar()` 제거 → `validator.validate(car)` 반환값으로 대체
- `CarValidator validator = new CarValidator()` 인스턴스 추가

---

## 완료 기준

- [ ] `CarValidator` 클래스 생성
- [ ] 제약 조건 5가지 각각 독립 private 메서드로 분리
- [ ] `isValidCheck()` / `testProducedCar()` 제거
- [ ] Phase 1 테스트 GREEN 유지 (테스트 대상을 `CarValidator.validate()`로 전환)
- [ ] `./gradlew build` 통과
