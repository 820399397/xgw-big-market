package cn.xuguowen.domain.strategy.service.rule.tree.factory.engine;

import cn.xuguowen.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * ClassName: IDecisionTreeEngine
 * Package: cn.xuguowen.domain.strategy.service.rule.tree.factory.engine
 * Description:规则树组合接口
 *
 * @Author 徐国文
 * @Create 2024/7/15 15:38
 * @Version 1.0
 */
public interface IDecisionTreeEngine {

    DefaultTreeFactory.StrategyAwardVO process(String userId, Long strategyId, Long awardId);
}
