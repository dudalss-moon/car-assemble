import domain.Car;
import domain.CarValidator;
import enums.BrakeType;
import enums.CarType;
import enums.EngineType;
import enums.SteeringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class AssembleValidationTest {

    private CarValidator validator;
    private Car car;

    @BeforeEach
    void setUp() {
        validator = new CarValidator();
        car = new Car();
    }

    private void setup(CarType carType, EngineType engineType, BrakeType brakeType, SteeringType steeringType) {
        car.setCarType(carType);
        car.setEngineType(engineType);
        car.setBrakeType(brakeType);
        car.setSteeringType(steeringType);
    }

    // --- 유효한 조합 (PASS) ---

    @Test
    void sedan_gm_mando_bosch_passes() {
        setup(CarType.SEDAN, EngineType.GM, BrakeType.MANDO, SteeringType.BOSCH);
        assertNull(validator.validate(car));
    }

    @Test
    void suv_gm_bosch_bosch_passes() {
        setup(CarType.SUV, EngineType.GM, BrakeType.BOSCH, SteeringType.BOSCH);
        assertNull(validator.validate(car));
    }

    @Test
    void truck_gm_continental_mobis_passes() {
        setup(CarType.TRUCK, EngineType.GM, BrakeType.CONTINENTAL, SteeringType.MOBIS);
        assertNull(validator.validate(car));
    }

    // --- 제약 조건 위반 (FAIL) ---

    @Test
    void sedan_continental_brake_fails() {
        setup(CarType.SEDAN, EngineType.GM, BrakeType.CONTINENTAL, SteeringType.BOSCH);
        assertNotNull(validator.validate(car));
    }

    @Test
    void suv_toyota_engine_fails() {
        setup(CarType.SUV, EngineType.TOYOTA, BrakeType.MANDO, SteeringType.BOSCH);
        assertNotNull(validator.validate(car));
    }

    @Test
    void truck_wia_engine_fails() {
        setup(CarType.TRUCK, EngineType.WIA, BrakeType.CONTINENTAL, SteeringType.BOSCH);
        assertNotNull(validator.validate(car));
    }

    @Test
    void truck_mando_brake_fails() {
        setup(CarType.TRUCK, EngineType.GM, BrakeType.MANDO, SteeringType.BOSCH);
        assertNotNull(validator.validate(car));
    }

    @Test
    void bosch_brake_non_bosch_steering_fails() {
        setup(CarType.SEDAN, EngineType.GM, BrakeType.BOSCH, SteeringType.MOBIS);
        assertNotNull(validator.validate(car));
    }
}
