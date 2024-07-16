package cn.xuguowen.domain.strategy.service.rule.chain.impl;

import cn.xuguowen.domain.strategy.repository.IStrategyRepository;
import cn.xuguowen.domain.strategy.service.rule.chain.AbstractLogicChain;
import cn.xuguowen.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import cn.xuguowen.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * ClassName: BlackListLogicChain
 * Package: cn.xuguowen.domain.strategy.service.rule.chain.impl
 * Description:黑名单责任链
 *
 * @Author 徐国文
 * @Create 2024/7/12 10:20
 * @Version 1.0
 */
@Slf4j
@Component("rule_blacklist")
public class BlackListLogicChain extends AbstractLogicChain {

    @Resource
    private IStrategyRepository strategyRepository;

    /**
     * 黑名单责任链接口
     *
     * @param userId     用户ID
     * @param strategyId 策略ID
     * @return 奖品ID
     */
    @Override
    public DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId) {
        log.info("抽奖责任链-黑名单开始 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());
        // 1.查询strategy_rule表中rule_model=rule_weight的rule_value
        // 查询出来的结果是这样的：100:user001,user002,user003
        String ruleValue = strategyRepository.queryStrategyRuleValue(strategyId, ruleModel());

        // 2.处理查询出来的结果
        String[] splitRuleValue = ruleValue.split(Constants.COLON);
        Long awardId = Long.parseLong(splitRuleValue[0]);

        String[] userBlackIds = splitRuleValue[1].split(Constants.SPLIT);
        for (String userBlackId : userBlackIds) {
            if (userId.equals(userBlackId)) {
                log.info("抽奖责任链-黑名单接管 userId: {} strategyId: {} ruleModel: {} awardId: {}", userId, strategyId, ruleModel(), awardId);
                return DefaultChainFactory.StrategyAwardVO.builder().awardId(awardId).logicModel(ruleModel()).build();
            }
        }

        // 3.过滤其他责任链
        log.info("抽奖责任链-黑名单放行 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());
        return next().logic(userId, strategyId);

    }

    @Override
    protected String ruleModel() {
        return DefaultChainFactory.LogicModel.RULE_BLACKLIST.getCode();
    }
}
