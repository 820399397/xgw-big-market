package cn.xuguowen.domain.strategy.service.rule.impl;

import cn.xuguowen.domain.strategy.annotation.LogicStrategy;
import cn.xuguowen.domain.strategy.model.entity.RuleActionEntity;
import cn.xuguowen.domain.strategy.model.entity.RuleMatterEntity;
import cn.xuguowen.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import cn.xuguowen.domain.strategy.repository.IStrategyRepository;
import cn.xuguowen.domain.strategy.service.rule.ILogicFilter;
import cn.xuguowen.domain.strategy.service.rule.factory.DefaultLogicFactory;
import cn.xuguowen.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * ClassName: RuleBackListLogicFilter
 * Package: cn.xuguowen.domain.strategy.service.rule.impl
 * Description:【抽奖前规则】黑名单用户过滤规则
 *
 * @Author 徐国文
 * @Create 2024/7/5 12:02
 * @Version 1.0
 */
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_BLACKLIST)
public class RuleBackListLogicFilter implements ILogicFilter<RuleActionEntity.RaffleBeforeEntity> {

    @Resource
    private IStrategyRepository strategyRepository;

    @Override
    public RuleActionEntity filter(RuleMatterEntity ruleMatterEntity) {
        String userId = ruleMatterEntity.getUserId();
        Long strategyId = ruleMatterEntity.getStrategyId();
        String ruleModel = ruleMatterEntity.getRuleModel();
        log.info("规则过滤-黑名单 userId:{} strategyId:{} ruleModel:{}", userId, strategyId, ruleModel);

        // 查询规则值配置
        // ruleValue  ==> 100:user001,user002,user003
        String ruleValue = strategyRepository.queryStrategyRuleRuleValue(strategyId,ruleMatterEntity.getAwardId(),ruleModel);
        String[] splitRuleValue = ruleValue.split(Constants.COLON);
        Long awardId = Long.parseLong(splitRuleValue[0]);

        // 黑名单过滤
        String[] userBlackIds = splitRuleValue[1].split(Constants.SPLIT);
        for (String userBlackId : userBlackIds) {
            if (userId.equals(userBlackId)) {
                return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                        .ruleModel(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode())
                        .data(RuleActionEntity.RaffleBeforeEntity.builder()
                                .strategyId(ruleMatterEntity.getStrategyId())
                                .awardId(awardId)
                                .build())
                        .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                        .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                        .build();
            }
        }

        return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                .build();
    }
}
