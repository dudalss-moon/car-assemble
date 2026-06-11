package enums;

public enum EngineType {
    GM(1, "GM"),
    TOYOTA(2, "TOYOTA"),
    WIA(3, "WIA"),
    BROKEN(4, "고장난 엔진");

    private final int code;
    private final String displayName;

    EngineType(int code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public int getCode() { return code; }
    public String getDisplayName() { return displayName; }

    public static EngineType from(int code) {
        for (EngineType t : values()) {
            if (t.code == code) return t;
        }
        throw new IllegalArgumentException("잘못된 엔진 타입: " + code);
    }
}
