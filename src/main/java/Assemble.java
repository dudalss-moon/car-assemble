import domain.Car;
import domain.CarValidator;
import enums.CarType;
import enums.EngineType;
import enums.BrakeType;
import enums.SteeringType;
import handler.InputHandler;
import view.ConsoleView;

import java.util.Scanner;

public class Assemble {

    private static final int CAR_TYPE  = 0;
    private static final int ENGINE    = 1;
    private static final int BRAKE     = 2;
    private static final int STEERING  = 3;
    private static final int RUN_TEST  = 4;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConsoleView view = new ConsoleView();
        InputHandler inputHandler = new InputHandler(scanner);
        CarValidator validator = new CarValidator();
        Car car = new Car();
        int step = CAR_TYPE;

        while (true) {
            view.clearScreen();
            showMenu(step, view);

            int answer = inputHandler.readInput(step);

            if (answer == InputHandler.EXIT) {
                view.showExit();
                break;
            }
            if (answer == InputHandler.INVALID) {
                delay(800);
                continue;
            }
            if (answer == 0) {
                step = moveToPrevious(step);
                continue;
            }

            step = processAnswer(step, answer, car, validator, view);
        }

        scanner.close();
    }

    private static void showMenu(int step, ConsoleView view) {
        switch (step) {
            case CAR_TYPE: view.showCarTypeMenu();  break;
            case ENGINE:   view.showEngineMenu();   break;
            case BRAKE:    view.showBrakeMenu();    break;
            case STEERING: view.showSteeringMenu(); break;
            case RUN_TEST: view.showRunTestMenu();  break;
        }
    }

    private static int moveToPrevious(int step) {
        return (step == RUN_TEST) ? CAR_TYPE : Math.max(step - 1, CAR_TYPE);
    }

    private static int processAnswer(int step, int answer, Car car, CarValidator validator, ConsoleView view) {
        switch (step) {
            case CAR_TYPE:
                car.setCarType(CarType.from(answer));
                view.showCarTypeSelected(car.getCarType());
                delay(800);
                return ENGINE;

            case ENGINE:
                car.setEngineType(EngineType.from(answer));
                view.showEngineSelected(car.getEngineType());
                delay(800);
                return BRAKE;

            case BRAKE:
                car.setBrakeType(BrakeType.from(answer));
                view.showBrakeSelected(car.getBrakeType());
                delay(800);
                return STEERING;

            case STEERING:
                car.setSteeringType(SteeringType.from(answer));
                view.showSteeringSelected(car.getSteeringType());
                delay(800);
                return RUN_TEST;

            case RUN_TEST:
                if (answer == 1) {
                    runCar(car, validator, view);
                    delay(2000);
                } else if (answer == 2) {
                    System.out.println("Test...");
                    delay(1500);
                    view.showTestResult(validator.validate(car));
                    delay(2000);
                }
                return RUN_TEST;

            default:
                return step;
        }
    }

    private static void runCar(Car car, CarValidator validator, ConsoleView view) {
        String violation = validator.validate(car);
        if (violation != null) {
            view.showInvalidCombination();
            return;
        }
        if (car.getEngineType() == EngineType.BROKEN) {
            view.showBrokenEngine();
            return;
        }
        view.showRunResult(car);
    }

    private static void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}
