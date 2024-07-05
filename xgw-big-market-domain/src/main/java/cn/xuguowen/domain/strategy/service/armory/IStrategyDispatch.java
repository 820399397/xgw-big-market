package cn.xuguowen.domain.strategy.service.armory;

/**
 * ClassName: IStrategyDispatch
 * Package: cn.xuguowen.domain.strategy.service.armory
 * Description:策略抽奖调度
 *
 * @Author 徐国文
 * @Create 2024/7/4 12:02
 * @Version 1.0
 */
public interface IStrategyDispatch {

    /**
     * 根据抽奖策略ID获取奖品
     *
     * @param strategyId 抽奖策略ID
     * @return
     */
    Long getRandomArard(Long strategyId);


    /**
     * 权重抽奖
     * @param strategyId        抽奖策略ID
     * @param ruleWeightValue   权重值
     * @return
     */
    Long getRandomArard(Long strategyId,String ruleWeightValue);


    /**
     * 针对于累加概率范围的抽奖
     *
     * @param strategyId 抽奖策略ID
     * @return
     */
    Long getRandomAwardAlternative(Long strategyId);

}
