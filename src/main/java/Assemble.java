import enums.BrakeType;
import enums.CarType;
import enums.EngineType;
import enums.SteeringType;

import java.util.Scanner;

public class Assemble {
    private static final String CLEAR_SCREEN = "\033[H\033[2J";

    private static final int CarType_Q        = 0;
    private static final int Engine_Q         = 1;
    private static final int BrakeSystem_Q    = 2;
    private static final int SteeringSystem_Q = 3;
    private static final int Run_Test         = 4;

    static int[] stack = new int[5];

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int step = CarType_Q;

        while (true) {
            System.out.print(CLEAR_SCREEN);
            System.out.flush();

            switch (step) {
                case CarType_Q:        showCarTypeMenu();   break;
                case Engine_Q:         showEngineMenu();    break;
                case BrakeSystem_Q:    showBrakeMenu();     break;
                case SteeringSystem_Q: showSteeringMenu();  break;
                case Run_Test:         showRunTestMenu();   break;
            }

            System.out.print("INPUT > ");
            String buf = sc.nextLine().trim();

            if (buf.equalsIgnoreCase("exit")) {
                System.out.println("바이바이");
                break;
            }

            int answer;
            try {
                answer = Integer.parseInt(buf);
            } catch (NumberFormatException e) {
                System.out.println("ERROR :: 숫자만 입력 가능");
                delay(800);
                continue;
            }

            if (!isValidRange(step, answer)) {
                delay(800);
                continue;
            }

            if (answer == 0) {
                if (step == Run_Test) {
                    step = CarType_Q;
                } else if (step > CarType_Q) {
                    step--;
                }
                continue;
            }

            switch (step) {
                case CarType_Q:
                    selectCarType(answer);
                    delay(800);
                    step = Engine_Q;
                    break;
                case Engine_Q:
                    selectEngine(answer);
                    delay(800);
                    step = BrakeSystem_Q;
                    break;
                case BrakeSystem_Q:
                    selectBrakeSystem(answer);
                    delay(800);
                    step = SteeringSystem_Q;
                    break;
                case SteeringSystem_Q:
                    selectSteeringSystem(answer);
                    delay(800);
                    step = Run_Test;
                    break;
                case Run_Test:
                    if (answer == 1) {
                        runProducedCar();
                        delay(2000);
                    } else if (answer == 2) {
                        System.out.println("Test...");
                        delay(1500);
                        testProducedCar();
                        delay(2000);
                    }
                    break;
            }
        }

        sc.close();
    }

    private static void showCarTypeMenu() {
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

    private static void showEngineMenu() {
        System.out.println("어떤 엔진을 탑재할까요?");
        System.out.println("0. 뒤로가기");
        System.out.println("1. " + EngineType.GM.getDisplayName());
        System.out.println("2. " + EngineType.TOYOTA.getDisplayName());
        System.out.println("3. " + EngineType.WIA.getDisplayName());
        System.out.println("4. " + EngineType.BROKEN.getDisplayName());
        System.out.println("===============================");
    }

    private static void showBrakeMenu() {
        System.out.println("어떤 제동장치를 선택할까요?");
        System.out.println("0. 뒤로가기");
        System.out.println("1. " + BrakeType.MANDO.getDisplayName());
        System.out.println("2. " + BrakeType.CONTINENTAL.getDisplayName());
        System.out.println("3. " + BrakeType.BOSCH.getDisplayName());
        System.out.println("===============================");
    }

    private static void showSteeringMenu() {
        System.out.println("어떤 조향장치를 선택할까요?");
        System.out.println("0. 뒤로가기");
        System.out.println("1. " + SteeringType.BOSCH.getDisplayName());
        System.out.println("2. " + SteeringType.MOBIS.getDisplayName());
        System.out.println("===============================");
    }

    private static void showRunTestMenu() {
        System.out.println("멋진 차량이 완성되었습니다.");
        System.out.println("어떤 동작을 할까요?");
        System.out.println("0. 처음 화면으로 돌아가기");
        System.out.println("1. RUN");
        System.out.println("2. Test");
        System.out.println("===============================");
    }

    private static boolean isValidRange(int step, int ans) {
        switch (step) {
            case CarType_Q:
                if (ans < 1 || ans > CarType.values().length) {
                    System.out.println("ERROR :: 차량 타입은 1 ~ " + CarType.values().length + " 범위만 선택 가능");
                    return false;
                }
                break;
            case Engine_Q:
                if (ans < 0 || ans > EngineType.values().length) {
                    System.out.println("ERROR :: 엔진은 1 ~ " + EngineType.values().length + " 범위만 선택 가능");
                    return false;
                }
                break;
            case BrakeSystem_Q:
                if (ans < 0 || ans > BrakeType.values().length) {
                    System.out.println("ERROR :: 제동장치는 1 ~ " + BrakeType.values().length + " 범위만 선택 가능");
                    return false;
                }
                break;
            case SteeringSystem_Q:
                if (ans < 0 || ans > SteeringType.values().length) {
                    System.out.println("ERROR :: 조향장치는 1 ~ " + SteeringType.values().length + " 범위만 선택 가능");
                    return false;
                }
                break;
            case Run_Test:
                if (ans < 0 || ans > 2) {
                    System.out.println("ERROR :: Run 또는 Test 중 하나를 선택 필요");
                    return false;
                }
                break;
        }
        return true;
    }

    private static void selectCarType(int a) {
        stack[CarType_Q] = a;
        System.out.printf("차량 타입으로 %s을 선택하셨습니다.\n", CarType.from(a).getDisplayName());
    }

    private static void selectEngine(int a) {
        stack[Engine_Q] = a;
        System.out.printf("%s 엔진을 선택하셨습니다.\n", EngineType.from(a).getDisplayName());
    }

    private static void selectBrakeSystem(int a) {
        stack[BrakeSystem_Q] = a;
        System.out.printf("%s 제동장치를 선택하셨습니다.\n", BrakeType.from(a).getDisplayName());
    }

    private static void selectSteeringSystem(int a) {
        stack[SteeringSystem_Q] = a;
        System.out.printf("%s 조향장치를 선택하셨습니다.\n", SteeringType.from(a).getDisplayName());
    }

    static boolean isValidCheck() {
        if (stack[CarType_Q] == CarType.SEDAN.getCode()         && stack[BrakeSystem_Q] == BrakeType.CONTINENTAL.getCode()) return false;
        if (stack[CarType_Q] == CarType.SUV.getCode()           && stack[Engine_Q] == EngineType.TOYOTA.getCode())          return false;
        if (stack[CarType_Q] == CarType.TRUCK.getCode()         && stack[Engine_Q] == EngineType.WIA.getCode())             return false;
        if (stack[CarType_Q] == CarType.TRUCK.getCode()         && stack[BrakeSystem_Q] == BrakeType.MANDO.getCode())       return false;
        if (stack[BrakeSystem_Q] == BrakeType.BOSCH.getCode()  && stack[SteeringSystem_Q] != SteeringType.BOSCH.getCode()) return false;
        return true;
    }

    private static void runProducedCar() {
        if (!isValidCheck()) {
            System.out.println("자동차가 동작되지 않습니다");
            return;
        }
        if (stack[Engine_Q] == EngineType.BROKEN.getCode()) {
            System.out.println("엔진이 고장나있습니다.");
            System.out.println("자동차가 움직이지 않습니다.");
            return;
        }

        System.out.printf("Car Type : %s\n", CarType.from(stack[CarType_Q]).getDisplayName());
        System.out.printf("Engine   : %s\n", EngineType.from(stack[Engine_Q]).getDisplayName());
        System.out.printf("Brake    : %s\n", BrakeType.from(stack[BrakeSystem_Q]).getDisplayName());
        System.out.printf("Steering : %s\n", SteeringType.from(stack[SteeringSystem_Q]).getDisplayName());
        System.out.println("자동차가 동작됩니다.");
    }

    private static void testProducedCar() {
        if (stack[CarType_Q] == CarType.SEDAN.getCode() && stack[BrakeSystem_Q] == BrakeType.CONTINENTAL.getCode()) {
            fail("Sedan에는 Continental제동장치 사용 불가");
        } else if (stack[CarType_Q] == CarType.SUV.getCode() && stack[Engine_Q] == EngineType.TOYOTA.getCode()) {
            fail("SUV에는 TOYOTA엔진 사용 불가");
        } else if (stack[CarType_Q] == CarType.TRUCK.getCode() && stack[Engine_Q] == EngineType.WIA.getCode()) {
            fail("Truck에는 WIA엔진 사용 불가");
        } else if (stack[CarType_Q] == CarType.TRUCK.getCode() && stack[BrakeSystem_Q] == BrakeType.MANDO.getCode()) {
            fail("Truck에는 Mando제동장치 사용 불가");
        } else if (stack[BrakeSystem_Q] == BrakeType.BOSCH.getCode() && stack[SteeringSystem_Q] != SteeringType.BOSCH.getCode()) {
            fail("Bosch제동장치에는 Bosch조향장치 이외 사용 불가");
        } else {
            System.out.println("자동차 부품 조합 테스트 결과 : PASS");
        }
    }

    private static void fail(String msg) {
        System.out.println("자동차 부품 조합 테스트 결과 : FAIL");
        System.out.println(msg);
    }

    private static void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}
