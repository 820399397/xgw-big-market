package cn.xuguowen.domain.strategy.service.rule.tree.impl;

import cn.xuguowen.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import cn.xuguowen.domain.strategy.service.rule.tree.ILogicTreeNode;
import cn.xuguowen.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * ClassName: RuleStockLogicTreeNode
 * Package: cn.xuguowen.domain.strategy.service.rule.tree.impl
 * Description:库存扣减节点
 *
 * @Author 徐国文
 * @Create 2024/7/15 13:23
 * @Version 1.0
 */
@Slf4j
@Component("rule_stock")// @Component("rule_stock") 设置的属性值，就是规则的Key。这样可以更方便使用。
public class RuleStockLogicTreeNode implements ILogicTreeNode {

    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Long arardId) {
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                .build();

    }
}
