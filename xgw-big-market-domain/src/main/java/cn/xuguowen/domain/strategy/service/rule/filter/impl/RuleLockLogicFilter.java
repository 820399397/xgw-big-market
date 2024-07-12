package cn.xuguowen.domain.strategy.service.rule.filter.impl;

import cn.xuguowen.domain.strategy.annotation.LogicStrategy;
import cn.xuguowen.domain.strategy.model.entity.RuleActionEntity;
import cn.xuguowen.domain.strategy.model.entity.RuleMatterEntity;
import cn.xuguowen.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import cn.xuguowen.domain.strategy.repository.IStrategyRepository;
import cn.xuguowen.domain.strategy.service.rule.filter.ILogicFilter;
import cn.xuguowen.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * ClassName: RuleLockLogicFilter
 * Package: cn.xuguowen.domain.strategy.service.rule.filter.impl
 * Description:用户抽奖n次后，对应奖品可解锁抽奖
 *
 * @Author 徐国文
 * @Create 2024/7/8 10:17
 * @Version 1.0
 */
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_LOCK)
public class RuleLockLogicFilter implements ILogicFilter<RuleActionEntity.RaffleCenterEntity> {

    @Resource
    private IStrategyRepository strategyRepository;

    // 用户抽奖次数，后续完成这部分流程开发的时候，从数据库/Redis中读取
    private Long userRaffleCount = 0L;


    @Override
    public RuleActionEntity<RuleActionEntity.RaffleCenterEntity> filter(RuleMatterEntity ruleMatterEntity) {
        String userId = ruleMatterEntity.getUserId();
        Long strategyId = ruleMatterEntity.getStrategyId();
        String ruleModel = ruleMatterEntity.getRuleModel();
        log.info("规则过滤-次数锁 userId:{} strategyId:{} ruleModel:{}", userId, strategyId, ruleModel);

        // 1.根据规则查询规则的ruleValue值
        String ruleValue = strategyRepository.queryStrategyRuleRuleValue(strategyId, ruleMatterEntity.getAwardId(), ruleModel);
        long raffleCount = Long.parseLong(ruleValue);

        // 用户抽奖次数大于规则限定值，规则放行.说明当前用户的抽奖次数可以抽取到这个需要解锁的奖品了
        if (userRaffleCount>= raffleCount) {
            return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                    .build();
        }

        // 用户抽奖次数小于规则限定值，规则拦截
        return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                .build();
    }
}
