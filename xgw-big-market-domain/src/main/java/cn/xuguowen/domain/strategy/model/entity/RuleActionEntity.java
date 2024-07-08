package cn.xuguowen.domain.strategy.model.entity;

import cn.xuguowen.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * ClassName: RuleActionEntity
 * Package: cn.xuguowen.domain.strategy.model.entity
 * Description:规则动作实体
 *
 * @Author 徐国文
 * @Create 2024/7/5 11:54
 * @Version 1.0
 */
@Data
@Builder
public class RuleActionEntity<T extends RuleActionEntity.RaffleEntity> {

    private String code = RuleLogicCheckTypeVO.ALLOW.getCode();
    private String info = RuleLogicCheckTypeVO.ALLOW.getInfo();
    private String ruleModel;
    private T data;


    public static class RaffleEntity {

    }

    // 抽奖之前
    @EqualsAndHashCode(callSuper = true) // 意味着 RaffleBeforeEntity 的 equals 和 hashCode 方法会首先调用父类 RaffleEntity 的 equals 和 hashCode 方法，然后再处理 RaffleBeforeEntity 自己的字段。这是为了确保在比较两个 RaffleBeforeEntity 对象时，不仅比较它们自己的字段，还比较它们从父类继承来的字段。
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RaffleBeforeEntity extends RaffleEntity {
        /**
         * 策略ID
         */
        private Long strategyId;

        /**
         * 权重值Key；用于抽奖时可以选择权重抽奖。
         */
        private String ruleWeightValueKey;

        /**
         * 奖品ID；
         */
        private Long awardId;
    }

    // 抽奖之中
    public static class RaffleCenterEntity extends RaffleEntity {

    }

    // 抽奖之后
    public static class RaffleAfterEntity extends RaffleEntity {

    }


}
