package cn.xuguowen.domain.strategy.service.rule.tree.factory;

import cn.xuguowen.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import cn.xuguowen.domain.strategy.model.valobj.RuleTreeVO;
import cn.xuguowen.domain.strategy.service.rule.tree.ILogicTreeNode;
import cn.xuguowen.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import cn.xuguowen.domain.strategy.service.rule.tree.factory.engine.impl.DecisionTreeEngine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * ClassName: DefaultLogicFactory
 * Package: cn.xuguowen.domain.strategy.service.rule.tree.factory
 * Description:规则树工厂
 *
 * @Author 徐国文
 * @Create 2024/7/15 13:26
 * @Version 1.0
 */
@Service
public class DefaultTreeFactory {

    private final Map<String, ILogicTreeNode> logicTreeNodeGroup;

    public DefaultTreeFactory(Map<String, ILogicTreeNode> logicTreeNodeGroup) {
        this.logicTreeNodeGroup = logicTreeNodeGroup;
    }

    public IDecisionTreeEngine openLogicTree(RuleTreeVO ruleTreeVO) {
        return new DecisionTreeEngine(logicTreeNodeGroup, ruleTreeVO);
    }



    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TreeActionEntity {
        private RuleLogicCheckTypeVO ruleLogicCheckType;
        private StrategyAwardVO strategyAwardVO;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StrategyAwardVO {
        /** 抽奖奖品ID - 内部流转使用 */
        private Long awardId;
        /** 抽奖奖品规则 */
        private String awardRuleValue;
    }



}
