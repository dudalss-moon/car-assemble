import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AssembleValidationTest {

    // stack[] 인덱스: 0=CarType, 1=Engine, 2=Brake, 3=Steering
    private static final int SEDAN = 1, SUV = 2, TRUCK = 3;
    private static final int GM = 1, TOYOTA = 2, WIA = 3;
    private static final int MANDO = 1, CONTINENTAL = 2, BOSCH = 3;
    private static final int BOSCH_S = 1, MOBIS = 2;

    @BeforeEach
    void resetStack() {
        Assemble.stack = new int[5];
    }

    private void setup(int carType, int engine, int brake, int steering) {
        Assemble.stack[0] = carType;
        Assemble.stack[1] = engine;
        Assemble.stack[2] = brake;
        Assemble.stack[3] = steering;
    }

    // --- 유효한 조합 (PASS) ---

    @Test
    void sedan_gm_mando_bosch_passes() {
        setup(SEDAN, GM, MANDO, BOSCH_S);
        assertTrue(Assemble.isValidCheck());
    }

    @Test
    void suv_gm_bosch_bosch_passes() {
        setup(SUV, GM, BOSCH, BOSCH_S);
        assertTrue(Assemble.isValidCheck());
    }

    @Test
    void truck_gm_continental_mobis_passes() {
        setup(TRUCK, GM, CONTINENTAL, MOBIS);
        assertTrue(Assemble.isValidCheck());
    }

    // --- 제약 조건 위반 (FAIL) ---

    @Test
    void sedan_continental_brake_fails() {
        setup(SEDAN, GM, CONTINENTAL, BOSCH_S);
        assertFalse(Assemble.isValidCheck());
    }

    @Test
    void suv_toyota_engine_fails() {
        setup(SUV, TOYOTA, MANDO, BOSCH_S);
        assertFalse(Assemble.isValidCheck());
    }

    @Test
    void truck_wia_engine_fails() {
        setup(TRUCK, WIA, CONTINENTAL, BOSCH_S);
        assertFalse(Assemble.isValidCheck());
    }

    @Test
    void truck_mando_brake_fails() {
        setup(TRUCK, GM, MANDO, BOSCH_S);
        assertFalse(Assemble.isValidCheck());
    }

    @Test
    void bosch_brake_non_bosch_steering_fails() {
        setup(SEDAN, GM, BOSCH, MOBIS);
        assertFalse(Assemble.isValidCheck());
    }
}
