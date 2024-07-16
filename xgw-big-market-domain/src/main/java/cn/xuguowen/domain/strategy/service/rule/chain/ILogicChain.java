package cn.xuguowen.domain.strategy.service.rule.chain;

import cn.xuguowen.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

/**
 * ClassName: ILogicChain
 * Package: cn.xuguowen.domain.strategy.service.rule.chain
 * Description:抽奖策略规则责任链接口
 *
 * @Author 徐国文
 * @Create 2024/7/12 10:18
 * @Version 1.0
 */
public interface ILogicChain extends ILogicChainArmory{

    /**
     * 责任链接口
     *
     * @param userId     用户ID
     * @param strategyId 策略ID
     * @return 奖品ID
     */
    DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId);


}
