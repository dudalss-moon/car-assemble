package enums;

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

    public int getCode() { return code; }
    public String getDisplayName() { return displayName; }

    public static CarType from(int code) {
        for (CarType t : values()) {
            if (t.code == code) return t;
        }
        throw new IllegalArgumentException("잘못된 차량 타입: " + code);
    }
}
