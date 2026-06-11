# Phase 1. 테스트 먼저 작성

## 목적

리팩토링 전 현재 동작을 테스트로 고정하여, 이후 단계에서 기존 동작이 깨지지 않았음을 검증할 안전망을 확보합니다.

---

## 테스트 대상

현재 `Assemble.java`에서 비즈니스 로직에 해당하는 메서드는 `isValidCheck()`입니다.

```java
private static boolean isValidCheck() {
    if (stack[CarType_Q] == SEDAN && stack[BrakeSystem_Q] == CONTINENTAL) return false;
    if (stack[CarType_Q] == SUV   && stack[Engine_Q] == TOYOTA)           return false;
    if (stack[CarType_Q] == TRUCK && stack[Engine_Q] == WIA)              return false;
    if (stack[CarType_Q] == TRUCK && stack[BrakeSystem_Q] == MANDO)       return false;
    if (stack[BrakeSystem_Q] == BOSCH_B && stack[SteeringSystem_Q] != BOSCH_S) return false;
    return true;
}
```

> `isValidCheck()`는 현재 static 필드(`stack[]`)에 직접 의존합니다.
> 테스트에서 호출하려면 `stack[]`을 사전에 설정하는 헬퍼 메서드를 사용합니다.

---

## 테스트 구조

```
src/test/java/
└── AssembleValidationTest.java
```

### 헬퍼 메서드

반복되는 `stack[]` 설정 코드를 헬퍼 메서드로 추출해 중복을 제거합니다.

```java
// stack[] 인덱스: 0=CarType, 1=Engine, 2=Brake, 3=Steering
private void setup(int carType, int engine, int brake, int steering) {
    Assemble.stack[0] = carType;
    Assemble.stack[1] = engine;
    Assemble.stack[2] = brake;
    Assemble.stack[3] = steering;
}
```

> `stack` 필드의 접근제한자를 `package-private`으로 변경하거나, 리플렉션을 사용합니다.
> 현재 단계에서는 최소한의 변경만 허용하므로 `package-private` 변경을 선택합니다.

---

## 테스트 케이스 목록

### 유효한 조합 (PASS)

| 테스트 이름 | CarType | Engine | Brake | Steering |
|------------|---------|--------|-------|----------|
| `sedan_gm_mando_bosch` | SEDAN | GM | MANDO | BOSCH |
| `suv_gm_bosch_bosch` | SUV | GM | BOSCH | BOSCH |
| `truck_gm_continental_mobis` | TRUCK | GM | CONTINENTAL | MOBIS |

### 제약 조건 위반 (FAIL)

| 테스트 이름 | 위반 조건 | CarType | Engine | Brake | Steering |
|------------|----------|---------|--------|-------|----------|
| `sedan_continental_brake_fails` | Sedan + CONTINENTAL | SEDAN | GM | CONTINENTAL | BOSCH |
| `suv_toyota_engine_fails` | SUV + TOYOTA | SUV | TOYOTA | MANDO | BOSCH |
| `truck_wia_engine_fails` | Truck + WIA | TRUCK | WIA | CONTINENTAL | BOSCH |
| `truck_mando_brake_fails` | Truck + MANDO | TRUCK | GM | MANDO | BOSCH |
| `bosch_brake_non_bosch_steering_fails` | BOSCH 제동 + 타사 조향 | SEDAN | GM | BOSCH | MOBIS |

---

## 예시 코드

```java
class AssembleValidationTest {

    private void setup(int carType, int engine, int brake, int steering) {
        Assemble.stack[0] = carType;
        Assemble.stack[1] = engine;
        Assemble.stack[2] = brake;
        Assemble.stack[3] = steering;
    }

    @Test
    void sedan_gm_mando_bosch_passes() {
        setup(1, 1, 1, 1); // SEDAN, GM, MANDO, BOSCH
        assertTrue(Assemble.isValidCheck());
    }

    @Test
    void sedan_continental_brake_fails() {
        setup(1, 1, 2, 1); // SEDAN, GM, CONTINENTAL, BOSCH
        assertFalse(Assemble.isValidCheck());
    }
}
```

---

## 완료 기준

- [ ] 유효 조합 3가지 PASS 확인
- [ ] 제약 조건 위반 5가지 FAIL 확인
- [ ] `./gradlew test` GREEN
