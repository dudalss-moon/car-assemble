package domain;

import enums.BrakeType;
import enums.CarType;
import enums.EngineType;
import enums.SteeringType;

public class Car {
    private CarType carType;
    private EngineType engineType;
    private BrakeType brakeType;
    private SteeringType steeringType;

    public CarType getCarType()           { return carType; }
    public EngineType getEngineType()     { return engineType; }
    public BrakeType getBrakeType()       { return brakeType; }
    public SteeringType getSteeringType() { return steeringType; }

    public void setCarType(CarType carType)                { this.carType = carType; }
    public void setEngineType(EngineType engineType)       { this.engineType = engineType; }
    public void setBrakeType(BrakeType brakeType)          { this.brakeType = brakeType; }
    public void setSteeringType(SteeringType steeringType) { this.steeringType = steeringType; }
}
