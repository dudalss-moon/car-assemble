package handler;

import enums.BrakeType;
import enums.CarType;
import enums.EngineType;
import enums.SteeringType;

import java.util.Scanner;

public class InputHandler {

    public static final int EXIT    = -1;
    public static final int INVALID = -2;

    private final Scanner scanner;

    public InputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public int readInput(int step) {
        System.out.print("INPUT > ");
        String buf = scanner.nextLine().trim();

        if (buf.equalsIgnoreCase("exit")) return EXIT;

        int answer;
        try {
            answer = Integer.parseInt(buf);
        } catch (NumberFormatException e) {
            System.out.println("ERROR :: 숫자만 입력 가능");
            return INVALID;
        }

        if (!isValidRange(step, answer)) return INVALID;
        return answer;
    }

    private boolean isValidRange(int step, int ans) {
        switch (step) {
            case 0:
                if (ans < 1 || ans > CarType.values().length) {
                    System.out.println("ERROR :: 차량 타입은 1 ~ " + CarType.values().length + " 범위만 선택 가능");
                    return false;
                }
                break;
            case 1:
                if (ans < 0 || ans > EngineType.values().length) {
                    System.out.println("ERROR :: 엔진은 1 ~ " + EngineType.values().length + " 범위만 선택 가능");
                    return false;
                }
                break;
            case 2:
                if (ans < 0 || ans > BrakeType.values().length) {
                    System.out.println("ERROR :: 제동장치는 1 ~ " + BrakeType.values().length + " 범위만 선택 가능");
                    return false;
                }
                break;
            case 3:
                if (ans < 0 || ans > SteeringType.values().length) {
                    System.out.println("ERROR :: 조향장치는 1 ~ " + SteeringType.values().length + " 범위만 선택 가능");
                    return false;
                }
                break;
            case 4:
                if (ans < 0 || ans > 2) {
                    System.out.println("ERROR :: Run 또는 Test 중 하나를 선택 필요");
                    return false;
                }
                break;
        }
        return true;
    }
}
