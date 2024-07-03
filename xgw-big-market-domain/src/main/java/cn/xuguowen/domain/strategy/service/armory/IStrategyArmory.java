package cn.xuguowen.domain.strategy.service.armory;

/**
 * ClassName: IStrategyArmory
 * Package: cn.xuguowen.domain.strategy.service.armory
 * Description:策略装配库(兵工厂)，负责初始化策略计算
 *
 * @Author 徐国文
 * @Create 2024/7/2 13:21
 * @Version 1.0
 */
public interface IStrategyArmory {

    /**
     * 装配抽奖策略：空间换时间
     *
     * @param strategyId 抽奖策略ID
     */
    void assembleLotteryStrategy(Long strategyId);

    /**
     * 装配抽奖策略的另一种实现方式：时间换空间
     *
     * @param strategyId 抽奖策略ID
     */
    void assemblyLotteryStrategyAlternative(Long strategyId);

    /**
     * 根据抽奖策略ID获取奖品
     *
     * @param strategyId 抽奖策略ID
     * @return
     */
    Long getRandomArard(Long strategyId);

    /**
     * 针对于累加概率范围的抽奖
     *
     * @param strategyId 抽奖策略ID
     * @return
     */
    Long getRandomAwardAlternative(Long strategyId);

}
