package enums;

public enum SteeringType {
    BOSCH(1, "Bosch"),
    MOBIS(2, "Mobis");

    private final int code;
    private final String displayName;

    SteeringType(int code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public int getCode() { return code; }
    public String getDisplayName() { return displayName; }

    public static SteeringType from(int code) {
        for (SteeringType t : values()) {
            if (t.code == code) return t;
        }
        throw new IllegalArgumentException("잘못된 조향장치 타입: " + code);
    }
}
