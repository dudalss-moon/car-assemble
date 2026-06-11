package domain;

import enums.BrakeType;
import enums.CarType;
import enums.EngineType;
import enums.SteeringType;

public class CarValidator {

    public String validate(Car car) {
        if (isSedanWithContinental(car))          return "Sedan에는 Continental제동장치 사용 불가";
        if (isSuvWithToyota(car))                 return "SUV에는 TOYOTA엔진 사용 불가";
        if (isTruckWithWia(car))                  return "Truck에는 WIA엔진 사용 불가";
        if (isTruckWithMando(car))                return "Truck에는 Mando제동장치 사용 불가";
        if (isBoschBrakeWithNonBoschSteering(car)) return "Bosch제동장치에는 Bosch조향장치 이외 사용 불가";
        return null;
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
