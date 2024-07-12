package cn.xuguowen.domain.strategy.service.rule.chain.impl;

import cn.xuguowen.domain.strategy.service.armory.IStrategyDispatch;
import cn.xuguowen.domain.strategy.service.rule.chain.AbstractLogicChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * ClassName: DefaultLogicChain
 * Package: cn.xuguowen.domain.strategy.service.rule.chain.impl
 * Description:默认的责任链「作为最后一个链」。兜底使用
 *
 * @Author 徐国文
 * @Create 2024/7/12 10:21
 * @Version 1.0
 */
@Slf4j
@Component("default")
public class DefaultLogicChain extends AbstractLogicChain {

    @Resource
    private IStrategyDispatch strategyDispatch;

    /**
     * 默认的责任链接口
     *
     * @param userId     用户ID
     * @param strategyId 策略ID
     * @return 奖品ID
     */
    @Override
    public Long logic(String userId, Long strategyId) {
        Long awardId = strategyDispatch.getRandomArard(strategyId);
        log.info("抽奖责任链-默认处理 userId: {} strategyId: {} ruleModel: {} awardId: {}", userId, strategyId, ruleModel(), awardId);
        return awardId;
    }


    @Override
    protected String ruleModel() {
        return "default";
    }
}
