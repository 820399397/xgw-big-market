package cn.xuguowen.domain.strategy.service.rule.tree.impl;

import cn.xuguowen.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import cn.xuguowen.domain.strategy.service.rule.tree.ILogicTreeNode;
import cn.xuguowen.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import cn.xuguowen.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * ClassName: RuleLuckAwardLogicTreeNode
 * Package: cn.xuguowen.domain.strategy.service.rule.tree.impl
 * Description:兜底奖励节点
 *
 * @Author 徐国文
 * @Create 2024/7/15 13:23
 * @Version 1.0
 */
@Slf4j
@Component("rule_luck_award")// @Component("rule_stock") 设置的属性值，就是规则的Key。这样可以更方便使用。
public class RuleLuckAwardLogicTreeNode implements ILogicTreeNode {

    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Long awardId,String ruleValue) {
        log.info("规则过滤-兜底奖品 userId:{} strategyId:{} awardId:{} ruleValue:{}", userId, strategyId, awardId, ruleValue);

        // ruleValue=>101:1,100
        String[] split = ruleValue.split(Constants.COLON);
        if (split.length == 0) {
            log.error("规则过滤-兜底奖品，兜底奖品未配置告警 userId:{} strategyId:{} awardId:{}", userId, strategyId, awardId);
            throw new RuntimeException("兜底奖品未配置 " + ruleValue);
        }

        // 兜底奖励配置
        Long luckAwardId = Long.valueOf(split[0]);
        String awardRuleValue = split.length > 1 ? split[1] : "";

        // 返回兜底奖品
        log.info("规则过滤-兜底奖品 userId:{} strategyId:{} awardId:{} awardRuleValue:{}", userId, strategyId, luckAwardId, awardRuleValue);
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                .strategyAwardVO(DefaultTreeFactory.StrategyAwardVO.builder()
                        .awardId(luckAwardId)
                        .awardRuleValue(awardRuleValue)
                        .build())
                .build();

    }
}
