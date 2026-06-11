# Phase 2. Enum 도입

## 목적

의미 없는 int 상수를 타입 안전한 Enum으로 교체하여 이름만으로 역할을 파악할 수 있게 합니다.

---

## 현재 문제

```java
// 숫자만 봐서는 무엇을 의미하는지 알 수 없음
private static final int SEDAN = 1, SUV = 2, TRUCK = 3;
private static final int GM = 1, TOYOTA = 2, WIA = 3;

if (stack[0] == 1 && stack[2] == 2) return false; // 무슨 의미인지 불분명
```

---

## 설계

### 파일 구조

```
src/main/java/
├── Assemble.java
└── enums/
    ├── CarType.java
    ├── EngineType.java
    ├── BrakeType.java
    └── SteeringType.java
```

### CarType

```java
public enum CarType {
    SEDAN(1, "Sedan"),
    SUV(2, "SUV"),
    TRUCK(3, "Truck");

    private final int code;
    private final String displayName;

    CarType(int code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }

    public static CarType from(int code) {
        for (CarType t : values()) {
            if (t.code == code) return t;
        }
        throw new IllegalArgumentException("잘못된 차량 타입: " + code);
    }
}
```

### EngineType

```java
public enum EngineType {
    GM(1, "GM"),
    TOYOTA(2, "TOYOTA"),
    WIA(3, "WIA"),
    BROKEN(4, "고장난 엔진");

    private final int code;
    private final String displayName;

    // 생성자, getDisplayName(), from() — CarType과 동일한 구조
}
```

### BrakeType

```java
public enum BrakeType {
    MANDO(1, "Mando"),
    CONTINENTAL(2, "Continental"),
    BOSCH(3, "Bosch");

    // 생성자, getDisplayName(), from() — CarType과 동일한 구조
}
```

### SteeringType

```java
public enum SteeringType {
    BOSCH(1, "Bosch"),
    MOBIS(2, "Mobis");

    // 생성자, getDisplayName(), from() — CarType과 동일한 구조
}
```

---

## Assemble.java 변경 범위

| 변경 전 | 변경 후 |
|--------|--------|
| `private static final int SEDAN = 1` | `CarType.SEDAN` |
| `private static final int GM = 1` | `EngineType.GM` |
| `private static final int MANDO = 1` | `BrakeType.MANDO` |
| `private static final int BOSCH_S = 1` | `SteeringType.BOSCH` |
| `stack[0] == SEDAN` | `stack[0] == CarType.SEDAN.getCode()` (Phase 3 전까지 임시) |

> `stack[]` 배열은 Phase 3에서 `Car` 객체로 교체합니다.
> 이 단계에서는 Enum 정의와 상수 교체에만 집중합니다.

---

## 완료 기준

- [ ] Enum 4개 생성
- [ ] `Assemble.java`에서 `private static final int` 상수 전부 제거
- [ ] Phase 1 테스트 GREEN 유지
- [ ] `./gradlew build` 통과
