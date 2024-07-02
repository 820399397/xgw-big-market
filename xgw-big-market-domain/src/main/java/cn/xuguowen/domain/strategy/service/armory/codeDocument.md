# 装配抽奖策略代码说明文档：cn.xuguowen.domain.strategy.service.armory.IStrategyArmory.assembleLotteryStrategy(Long strategyId)

## 1.概率范围详解
```java
BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);
log.info("rateRange:{}", rateRange);
```
- totalAwardRate：这是所有奖品的概率值的总和。根据前面的代码片段，这个值是在遍历 strategyAwardEntityList 的过程中累积计算出来的。

- minAwardRate：这是所有奖品中最小的概率值。在遍历 strategyAwardEntityList 的过程中，通过比较来确定最小的概率值。

- divide 方法：totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING) 将 totalAwardRate 除以 minAwardRate。第二个参数 0 指定结果的小数位数，即结果将被舍入到整数。 RoundingMode.CEILING 指定舍入模式为向上舍入，即结果总是向上舍入到最近的整数。

例子：假设你的 strategyAwardEntityList 中有三个奖品，其 award_rate 分别是 80.0000, 10.0000, 5.0000。
totalAwardRate = 80.0000 + 10.0000 + 5.0000 = 95.0000
minAwardRate = 5.0000
除法计算：
95.0000 / 5.0000 = 19.0000
舍入：
由于使用了 RoundingMode.CEILING 并且指定小数位数为 0，结果向上舍入到最近的整数，所以 19.0000 直接取整，结果仍然是 19。

## 2.生成策略奖品概率查找表「这里指需要在list集合中，存放上对应的奖品占位即可，占位越多等于概率越高」详解
```java
List<Long> strategyAwardSearchRateTables = new ArrayList<>(rateRange.intValue());
for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntityList) {
    Long awardId = strategyAwardEntity.getAwardId();
    BigDecimal awardRate = strategyAwardEntity.getAwardRate();
    // 计算出每个概率值需要存放到查找表的数量，循环填充
    for (int i = 0; i < rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue(); i++) {
        strategyAwardSearchRateTables.add(awardId);
    }
}
```
假设你有以下奖品及其概率：
奖品A，awardRate = 80.0000
奖品B，awardRate = 10.0000
奖品C，awardRate = 5.0000
前面的计算：
totalAwardRate = 95.0000
minAwardRate = 5.0000
rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING) = 95.0000 / 5.0000 = 19
填充过程
奖品A:
awardRate = 80.0000
占位数量：rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue() = 19 * 80.0000 = 1520
填充 strategyAwardSearchRateTables 列表 1520 次 awardId 为奖品A的ID。

奖品B:
awardRate = 10.0000
占位数量：rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue() = 19 * 10.0000 = 190
填充 strategyAwardSearchRateTables 列表 190 次 awardId 为奖品B的ID。

奖品C:
awardRate = 5.0000
占位数量：rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue() = 19 * 5.0000 = 95
填充 strategyAwardSearchRateTables 列表 95 次 awardId 为奖品C的ID。

最终，strategyAwardSearchRateTables 列表的大小是 1520 + 190 + 95 = 1805。虽然初始容量设为 19，但 ArrayList 会自动扩展以容纳所有填充的元素。