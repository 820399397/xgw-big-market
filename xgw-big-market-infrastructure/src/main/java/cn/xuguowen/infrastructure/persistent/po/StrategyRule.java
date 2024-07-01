package cn.xuguowen.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * ClassName: StrategyRule
 * Package: cn.xuguowen.infrastructure.persistent.po
 * Description:抽奖规则实体类，对应数据库表 strategy_rule
 *
 * @Author 徐国文
 * @Create 2024/7/1 9:42
 * @Version 1.0
 */
@Data
public class StrategyRule {

    /** 自增ID */
    private Long id;
    /** 抽奖策略ID */
    private Long strategyId;
    /** 抽奖奖品ID【规则类型为策略，则不需要奖品ID】 */
    private Long awardId;
    /** 抽奖规则类型；1-策略规则、2-奖品规则 */
    private Integer ruleType;
    /** 抽奖规则类型【rule_random - 随机值计算、rule_lock - 抽奖几次后解锁、rule_luck_award - 幸运奖(兜底奖品)】 */
    private String ruleModel;
    /** 抽奖规则比值 */
    private String ruleValue;
    /** 抽奖规则描述 */
    private String ruleDesc;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
}
