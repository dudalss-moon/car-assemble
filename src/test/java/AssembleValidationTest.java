import domain.Car;
import enums.BrakeType;
import enums.CarType;
import enums.EngineType;
import enums.SteeringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AssembleValidationTest {

    @BeforeEach
    void resetCar() {
        Assemble.car = new Car();
    }

    private void setup(CarType carType, EngineType engineType, BrakeType brakeType, SteeringType steeringType) {
        Assemble.car.setCarType(carType);
        Assemble.car.setEngineType(engineType);
        Assemble.car.setBrakeType(brakeType);
        Assemble.car.setSteeringType(steeringType);
    }

    // --- 유효한 조합 (PASS) ---

    @Test
    void sedan_gm_mando_bosch_passes() {
        setup(CarType.SEDAN, EngineType.GM, BrakeType.MANDO, SteeringType.BOSCH);
        assertTrue(Assemble.isValidCheck());
    }

    @Test
    void suv_gm_bosch_bosch_passes() {
        setup(CarType.SUV, EngineType.GM, BrakeType.BOSCH, SteeringType.BOSCH);
        assertTrue(Assemble.isValidCheck());
    }

    @Test
    void truck_gm_continental_mobis_passes() {
        setup(CarType.TRUCK, EngineType.GM, BrakeType.CONTINENTAL, SteeringType.MOBIS);
        assertTrue(Assemble.isValidCheck());
    }

    // --- 제약 조건 위반 (FAIL) ---

    @Test
    void sedan_continental_brake_fails() {
        setup(CarType.SEDAN, EngineType.GM, BrakeType.CONTINENTAL, SteeringType.BOSCH);
        assertFalse(Assemble.isValidCheck());
    }

    @Test
    void suv_toyota_engine_fails() {
        setup(CarType.SUV, EngineType.TOYOTA, BrakeType.MANDO, SteeringType.BOSCH);
        assertFalse(Assemble.isValidCheck());
    }

    @Test
    void truck_wia_engine_fails() {
        setup(CarType.TRUCK, EngineType.WIA, BrakeType.CONTINENTAL, SteeringType.BOSCH);
        assertFalse(Assemble.isValidCheck());
    }

    @Test
    void truck_mando_brake_fails() {
        setup(CarType.TRUCK, EngineType.GM, BrakeType.MANDO, SteeringType.BOSCH);
        assertFalse(Assemble.isValidCheck());
    }

    @Test
    void bosch_brake_non_bosch_steering_fails() {
        setup(CarType.SEDAN, EngineType.GM, BrakeType.BOSCH, SteeringType.MOBIS);
        assertFalse(Assemble.isValidCheck());
    }
}
