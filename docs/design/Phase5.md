# Phase 5. UI 분리

## 목적

`Assemble.java`에 혼재된 메뉴 출력, 입력 처리, 흐름 제어 책임을 각각의 클래스로 분리하여 SRP를 충족합니다.

---

## 현재 문제

`Assemble.java`의 `main()`이 세 가지 책임을 모두 담당합니다.

```java
public static void main(String[] args) {
    // 1. 입력 처리
    String buf = sc.nextLine().trim();
    if (buf.equalsIgnoreCase("exit")) { ... }
    answer = Integer.parseInt(buf);
    if (!isValidRange(step, answer)) { ... }

    // 2. 메뉴 출력
    showCarTypeMenu();
    showEngineMenu();
    ...

    // 3. 흐름 제어
    switch (step) {
        case CarType_Q: ...; step = Engine_Q; break;
        ...
    }
}
```

---

## 설계

### 파일 구조

```
src/main/java/
├── Assemble.java          ← 흐름 제어만 담당
├── view/
│   └── ConsoleView.java   ← 메뉴 출력 담당
├── handler/
│   └── InputHandler.java  ← 입력 파싱 / 범위 검증 / exit 처리
├── domain/
│   ├── Car.java
│   └── CarValidator.java
└── enums/
    └── ...
```

---

### ConsoleView 클래스

메뉴 출력만 담당합니다. 반환값 없이 출력만 수행합니다.

```java
public class ConsoleView {

    public void showCarTypeMenu() {
        System.out.println("        ______________");
        System.out.println("       /|            |");
        ...
        System.out.println("어떤 차량 타입을 선택할까요?");
        System.out.println("1. Sedan");
        System.out.println("2. SUV");
        System.out.println("3. Truck");
        System.out.println("===============================");
    }

    public void showEngineMenu() { ... }
    public void showBrakeMenu() { ... }
    public void showSteeringMenu() { ... }
    public void showRunTestMenu() { ... }

    public void showResult(Car car) { ... }      // RUN 결과 출력
    public void showTestResult(String violation) { ... } // Test 결과 출력
}
```

---

### InputHandler 클래스

입력 파싱, 범위 검증, exit 처리만 담당합니다. 입력 결과를 명확한 값으로 반환합니다.

```java
public class InputHandler {

    private final Scanner scanner;

    public InputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    // exit 입력 시 -1 반환, 유효하지 않은 입력 시 -2 반환
    public int readInput(int step) {
        System.out.print("INPUT > ");
        String buf = scanner.nextLine().trim();

        if (buf.equalsIgnoreCase("exit")) return -1;

        int answer;
        try {
            answer = Integer.parseInt(buf);
        } catch (NumberFormatException e) {
            System.out.println("ERROR :: 숫자만 입력 가능");
            return -2;
        }

        if (!isValidRange(step, answer)) return -2;
        return answer;
    }

    private boolean isValidRange(int step, int answer) { ... }
}
```

| 반환값 | 의미 |
|--------|------|
| `-1` | `exit` 입력 — 프로그램 종료 |
| `-2` | 잘못된 입력 — 재입력 필요 |
| `0` | 뒤로가기 |
| `1 이상` | 유효한 선택 번호 |

---

### Assemble.main() 변경 후

흐름 제어(단계 전환)만 담당합니다.

```java
public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    ConsoleView view = new ConsoleView();
    InputHandler inputHandler = new InputHandler(scanner);
    CarValidator validator = new CarValidator();
    Car car = new Car();

    int step = 0;

    while (true) {
        view.showMenu(step);

        int answer = inputHandler.readInput(step);
        if (answer == -1) { System.out.println("바이바이"); break; }
        if (answer == -2) continue;
        if (answer == 0)  { step = moveToPrevious(step); continue; }

        step = processAnswer(step, answer, car, validator, view);
    }

    scanner.close();
}
```

---

## 책임 분리 요약

| 클래스 | 책임 | 변경 이유 |
|--------|------|----------|
| `ConsoleView` | 메뉴 및 결과 출력 | UI 문구가 바뀔 때 |
| `InputHandler` | 입력 파싱 / 검증 | 입력 방식이 바뀔 때 |
| `CarValidator` | 부품 조합 제약 검사 | 제약 조건이 바뀔 때 |
| `Car` | 조립 상태 보관 | 부품 종류가 추가될 때 |
| `Assemble` | 단계 흐름 제어 | 조립 순서가 바뀔 때 |

---

## 완료 기준

- [ ] `ConsoleView` 클래스 생성 — 메뉴 출력 메서드 이동
- [ ] `InputHandler` 클래스 생성 — 입력 파싱 / 범위 검증 / exit 처리
- [ ] `Assemble.java`에 `System.out.print` / 입력 파싱 코드 없음
- [ ] Phase 1 테스트 GREEN 유지
- [ ] `./gradlew build` 통과
