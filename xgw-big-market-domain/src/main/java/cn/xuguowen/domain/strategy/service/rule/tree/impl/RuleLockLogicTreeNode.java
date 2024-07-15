package cn.xuguowen.domain.strategy.service.rule.tree.impl;

import cn.xuguowen.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import cn.xuguowen.domain.strategy.service.rule.tree.ILogicTreeNode;
import cn.xuguowen.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * ClassName: RuleLockLogicTreeNode
 * Package: cn.xuguowen.domain.strategy.service.rule.tree.impl
 * Description:抽奖次数锁节点
 *
 * @Author 徐国文
 * @Create 2024/7/15 13:22
 * @Version 1.0
 */
@Slf4j
@Component("rule_lock")// @Component("rule_stock") 设置的属性值，就是规则的Key。这样可以更方便使用。
public class RuleLockLogicTreeNode implements ILogicTreeNode {

    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Long arardId) {
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.ALLOW)
                .build();

    }
}
