package cn.xuguowen.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * ClassName: Strategy
 * Package: cn.xuguowen.infrastructure.persistent.po
 * Description:抽奖策略实体类，对应数据库表 strategy
 *
 * @Author 徐国文
 * @Create 2024/7/1 9:25
 * @Version 1.0
 */
@Data
public class Strategy {

    /** 自增ID */
    private Long id;
    /** 抽奖策略ID */
    private Long strategyId;
    /** 抽象策略描述 */
    private String strategyDesc;
    /** 规则模型,strategy_rule配置规则记录 */
    private String ruleModels;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
}
