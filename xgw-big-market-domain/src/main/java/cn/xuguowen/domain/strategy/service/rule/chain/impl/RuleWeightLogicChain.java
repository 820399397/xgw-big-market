package cn.xuguowen.domain.strategy.service.rule.chain.impl;

import cn.xuguowen.domain.strategy.repository.IStrategyRepository;
import cn.xuguowen.domain.strategy.service.armory.IStrategyDispatch;
import cn.xuguowen.domain.strategy.service.rule.chain.AbstractLogicChain;
import cn.xuguowen.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import cn.xuguowen.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * ClassName: RuleWeightLogicChain
 * Package: cn.xuguowen.domain.strategy.service.rule.chain.impl
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/7/12 10:23
 * @Version 1.0
 */
@Slf4j
@Component("rule_weight")
public class RuleWeightLogicChain extends AbstractLogicChain {

    // 根据用户ID查询用户抽奖消耗的积分值，本章节我们先写死为固定的值。后续需要从数据库中查询。
    public Long userScore = 0L;

    @Resource
    private IStrategyRepository strategyRepository;

    @Resource
    private IStrategyDispatch strategyDispatch;

    /**
     * 权重责任链接口
     *
     * @param userId     用户ID
     * @param strategyId 策略ID
     * @return 奖品ID
     */
    @Override
    public DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId) {
        log.info("抽奖责任链-权重开始 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());

        // 1.查询strategy_rule表中rule_model=rule_weight的rule_value
        // 查询出来的结果是这样的：4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108,109
        String ruleValue = strategyRepository.queryStrategyRuleValue(strategyId, ruleModel());

        // 2.处理ruleValue。
        // {5000：“102,103,104,105,106,107”，4000：“102,103,104,105”}==>{4000=4000:102,103,104,105, 5000=5000:102,103,104,105,106,107, 6000=6000:102,103,104,105,106,107,108,109}
        Map<Long, String> analyticalValueGroup = getAnalyticalValue(ruleValue);
        if (CollectionUtils.isEmpty(analyticalValueGroup)) {
            return null;
        }

        // 3. 转换Keys值，analyticalValueGroup是TreeMap类型的，所以就不需要排序了
        List<Long> analyticalSortedKeys = new ArrayList<>(analyticalValueGroup.keySet());

        // 4.找出最小符合的值，也就是【4500 积分，能找到 4000:102,103,104,105】、【5000 积分，能找到 5000:102,103,104,105,106,107】
        /* 找到最后一个符合的值[如用户传了一个 5900 应该返回正确结果为 5000]，如果使用 Lambda findFirst 需要注意使用 sorted 反转结果
         *         Long nextValue = null;
         *         for (Long analyticalSortedKeyValue : analyticalSortedKeys) {
         *             if (userScore >= analyticalSortedKeyValue){
         *                 nextValue = analyticalSortedKeyValue;
         *             }
         *         }
         *
         * Long nextValue = analyticalSortedKeys.stream()
         *      .filter(key -> userScore >= key)
         *      .max(Comparator.naturalOrder())
         *      .orElse(null);
         */
        // nextValue = 5000 / 4000 /6000
        Long nextValue = analyticalSortedKeys.stream()
                .sorted(Comparator.reverseOrder())
                .filter(key -> userScore >= key)
                .findFirst()
                .orElse(null);

        // 5. 权重抽奖
        if (null != nextValue) {
            Long awardId = strategyDispatch.getRandomArard(strategyId, analyticalValueGroup.get(nextValue));
            log.info("抽奖责任链-权重接管 userId: {} strategyId: {} ruleModel: {} awardId: {}", userId, strategyId, ruleModel(), awardId);
            return DefaultChainFactory.StrategyAwardVO.builder().awardId(awardId).logicModel(ruleModel()).build();
        }

        // 6. 过滤其他责任链
        log.info("抽奖责任链-权重放行 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());
        return next().logic(userId, strategyId);

    }

    @Override
    protected String ruleModel() {
        return DefaultChainFactory.LogicModel.RULE_WEIGHT.getCode();
    }

    private Map<Long, String> getAnalyticalValue(String ruleValue) {
        String[] ruleValueGroups = ruleValue.split(Constants.SPACE);
        // 使用TreeMap来解析值
        Map<Long, String> ruleValueMap = new TreeMap<>();

        for (String ruleValueKey : ruleValueGroups) {
            // 检查输入是否为空
            if (ruleValueKey == null || ruleValueKey.isEmpty()) {
                return ruleValueMap;
            }
            // 分割字符串以获取键和值
            String[] parts = ruleValueKey.split(Constants.COLON);
            if (parts.length != 2) {
                throw new IllegalArgumentException("rule_weight rule_rule invalid input format" + ruleValueKey);
            }
            ruleValueMap.put(Long.parseLong(parts[0]), ruleValueKey);
        }
        return ruleValueMap;
    }

    public static void main(String[] args) {
        RuleWeightLogicChain ruleWeightLogicChain = new RuleWeightLogicChain();
        Map<Long, String> analyticalValue = ruleWeightLogicChain.getAnalyticalValue("4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108,109");
        System.out.println(analyticalValue);

        List<Long> analyticalSortedKeys = new ArrayList<>(analyticalValue.keySet());

        long userScore = 5900L;

        // 方式1
        /*Long nextValue = null;
        for (Long analyticalSortedKey : analyticalSortedKeys) {
            if (userScore >= analyticalSortedKey) {
                nextValue = analyticalSortedKey;
            }
        }*/

        // 方式2:它用于找到流中元素的最大值
        /*
        Long nextValue = analyticalSortedKeys.stream()  // 创建一个 Stream 对象，它包含 analyticalSortedKeys 列表中的所有元素。
                .filter(key -> userScore >= key) // 过滤出所有小于或等于 userScore 的键。
                .max(Comparator.naturalOrder()) // 在过滤后的键中找到最大的那个（即小于或等于 userScore 的键中最大的一个）。Comparator.naturalOrder() 返回一个自然顺序的比较器，用于比较元素的大小。
                .orElse(null);              // 如果找到了这样的键，返回该键；否则返回 null。

         */

        // 方式3：
        // 排序的目的是为了在找到第一个满足条件的元素时，确保它是最大的满足条件的元素。如果不进行降序排序，流中的第一个元素可能不是最大的满足条件的元素。因此，降序排序确保 findFirst() 找到的元素是最大的一个。
        Long nextValue = analyticalSortedKeys.stream()
                .sorted(Comparator.reverseOrder())
                .filter(analyticalSortedKeyValue -> userScore >= analyticalSortedKeyValue)
                .findFirst()
                .orElse(null);

        System.out.println(nextValue);

    }
}
