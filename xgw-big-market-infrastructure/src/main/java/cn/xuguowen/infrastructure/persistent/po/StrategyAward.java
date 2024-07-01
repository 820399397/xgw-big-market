package cn.xuguowen.infrastructure.persistent.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ClassName: strategyAward
 * Package: cn.xuguowen.infrastructure.persistent.po
 * Description:抽奖奖品实体类，对应数据库表 strategy_award
 *
 * @Author 徐国文
 * @Create 2024/7/1 9:39
 * @Version 1.0
 */
@Data
public class StrategyAward {

    /** 自增ID */
    private Long id;
    /** 抽奖策略ID */
    private Long strategyId;
    /** 抽奖奖品ID */
    private Long awardId;
    /** 抽奖奖品标题 */
    private String awardTitle;
    /** 抽奖奖品副标题 */
    private String awardSubtitle;
    /** 奖品库存总量 */
    private Integer awardCount;
    /** 奖品库存剩余量 */
    private Integer awardCountSurplus;
    /** 奖品中奖概率 */
    private BigDecimal awardRate;
    /** 规则模型,strategy_rule配置规则记录 */
    private String ruleModels;
    /** 排序 */
    private Integer sort;
    /** 创建时间 */
    private Date createTime;
    /** 修改时间 */
    private Date updateTime;
}
