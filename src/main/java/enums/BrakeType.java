package enums;

public enum BrakeType {
    MANDO(1, "Mando"),
    CONTINENTAL(2, "Continental"),
    BOSCH(3, "Bosch");

    private final int code;
    private final String displayName;

    BrakeType(int code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public int getCode() { return code; }
    public String getDisplayName() { return displayName; }

    public static BrakeType from(int code) {
        for (BrakeType t : values()) {
            if (t.code == code) return t;
        }
        throw new IllegalArgumentException("잘못된 제동장치 타입: " + code);
    }
}
