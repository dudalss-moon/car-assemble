# car-assemble

콘솔 기반 자동차 조립 시뮬레이터입니다.

## 실행 방법

```bash
./gradlew run
```

## 제조 순서

1. **차량 타입 선택** — 총 세 가지 타입을 제작할 수 있으며, 향후 타입이 추가될 수 있습니다.
   - Sedan / SUV / Truck

2. **부품 선택** — 차량에 들어갈 부품을 각각 선택합니다.
   - 엔진: GM / TOYOTA / WIA / 고장난 엔진
   - 제동장치: MANDO / CONTINENTAL / BOSCH
   - 조향장치: BOSCH / MOBIS

3. **완성 및 테스트** — 조립 완료 후 RUN 또는 Test를 선택합니다.
   - RUN: 선택한 부품으로 차량을 동작 (고장난 엔진 선택 시 동작 불가)
   - Test: 부품 조합의 유효성 검사

## 입력 규칙

| 입력 | 동작 |
|------|------|
| 숫자 | 해당 번호의 항목 선택 |
| `0` | 이전 단계로 돌아가기 (차량 타입 선택 단계 제외) |
| `exit` | 프로그램 종료 |

## 부품 조합 제약

| 제약 | 설명 |
|------|------|
| Sedan + CONTINENTAL 제동장치 | Continental은 Sedan용 제동장치를 만들지 않습니다. |
| SUV + TOYOTA 엔진 | TOYOTA는 SUV용 엔진을 만들지 않습니다. |
| Truck + WIA 엔진 | WIA는 Truck용 엔진을 만들지 않습니다. |
| Truck + MANDO 제동장치 | MANDO는 Truck용 제동장치를 만들지 않습니다. |
| BOSCH 제동장치 + 타사 조향장치 | BOSCH 제동장치는 BOSCH 조향장치와만 호환됩니다. |
