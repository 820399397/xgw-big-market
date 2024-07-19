package cn.xuguowen.domain.strategy.service.rule.tree;

import cn.xuguowen.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * ClassName: ILogicTreeNode
 * Package: cn.xuguowen.domain.strategy.service.rule.tree
 * Description:规则树接口
 *
 * @Author 徐国文
 * @Create 2024/7/15 13:21
 * @Version 1.0
 */
public interface ILogicTreeNode {

    DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Long awardId,String ruleValue);
}
