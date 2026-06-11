package view;

import domain.Car;
import enums.BrakeType;
import enums.CarType;
import enums.EngineType;
import enums.SteeringType;

public class ConsoleView {

    private static final String CLEAR_SCREEN = "\033[H\033[2J";

    public void clearScreen() {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
    }

    public void showCarTypeMenu() {
        System.out.println("        ______________");
        System.out.println("       /|            |");
        System.out.println("  ____/_|_____________|____");
        System.out.println(" |                      O  |");
        System.out.println(" '-(@)----------------(@)--'");
        System.out.println("===============================");
        System.out.println("어떤 차량 타입을 선택할까요?");
        System.out.println("1. " + CarType.SEDAN.getDisplayName());
        System.out.println("2. " + CarType.SUV.getDisplayName());
        System.out.println("3. " + CarType.TRUCK.getDisplayName());
        System.out.println("===============================");
    }

    public void showEngineMenu() {
        System.out.println("어떤 엔진을 탑재할까요?");
        System.out.println("0. 뒤로가기");
        System.out.println("1. " + EngineType.GM.getDisplayName());
        System.out.println("2. " + EngineType.TOYOTA.getDisplayName());
        System.out.println("3. " + EngineType.WIA.getDisplayName());
        System.out.println("4. " + EngineType.BROKEN.getDisplayName());
        System.out.println("===============================");
    }

    public void showBrakeMenu() {
        System.out.println("어떤 제동장치를 선택할까요?");
        System.out.println("0. 뒤로가기");
        System.out.println("1. " + BrakeType.MANDO.getDisplayName());
        System.out.println("2. " + BrakeType.CONTINENTAL.getDisplayName());
        System.out.println("3. " + BrakeType.BOSCH.getDisplayName());
        System.out.println("===============================");
    }

    public void showSteeringMenu() {
        System.out.println("어떤 조향장치를 선택할까요?");
        System.out.println("0. 뒤로가기");
        System.out.println("1. " + SteeringType.BOSCH.getDisplayName());
        System.out.println("2. " + SteeringType.MOBIS.getDisplayName());
        System.out.println("===============================");
    }

    public void showRunTestMenu() {
        System.out.println("멋진 차량이 완성되었습니다.");
        System.out.println("어떤 동작을 할까요?");
        System.out.println("0. 처음 화면으로 돌아가기");
        System.out.println("1. RUN");
        System.out.println("2. Test");
        System.out.println("===============================");
    }

    public void showCarTypeSelected(CarType carType) {
        System.out.printf("차량 타입으로 %s을 선택하셨습니다.\n", carType.getDisplayName());
    }

    public void showEngineSelected(EngineType engineType) {
        System.out.printf("%s 엔진을 선택하셨습니다.\n", engineType.getDisplayName());
    }

    public void showBrakeSelected(BrakeType brakeType) {
        System.out.printf("%s 제동장치를 선택하셨습니다.\n", brakeType.getDisplayName());
    }

    public void showSteeringSelected(SteeringType steeringType) {
        System.out.printf("%s 조향장치를 선택하셨습니다.\n", steeringType.getDisplayName());
    }

    public void showRunResult(Car car) {
        System.out.printf("Car Type : %s\n", car.getCarType().getDisplayName());
        System.out.printf("Engine   : %s\n", car.getEngineType().getDisplayName());
        System.out.printf("Brake    : %s\n", car.getBrakeType().getDisplayName());
        System.out.printf("Steering : %s\n", car.getSteeringType().getDisplayName());
        System.out.println("자동차가 동작됩니다.");
    }

    public void showTestResult(String violation) {
        if (violation != null) {
            System.out.println("자동차 부품 조합 테스트 결과 : FAIL");
            System.out.println(violation);
        } else {
            System.out.println("자동차 부품 조합 테스트 결과 : PASS");
        }
    }

    public void showExit() {
        System.out.println("바이바이");
    }

    public void showBrokenEngine() {
        System.out.println("엔진이 고장나있습니다.");
        System.out.println("자동차가 움직이지 않습니다.");
    }

    public void showInvalidCombination() {
        System.out.println("자동차가 동작되지 않습니다");
    }
}
