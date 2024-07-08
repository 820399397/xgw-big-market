package cn.xuguowen.domain.strategy.model.entity;

import cn.xuguowen.types.common.Constants;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * ClassName: StrategyEntity
 * Package: cn.xuguowen.domain.strategy.model.entity
 * Description:抽奖策略实体
 *
 * @Author 徐国文
 * @Create 2024/7/4 12:30
 * @Version 1.0
 */
@Data
@Builder
public class StrategyEntity {
    /** 抽奖策略ID */
    private Long strategyId;
    /** 抽象策略描述 */
    private String strategyDesc;
    /** 规则模型,strategy_rule配置规则记录 */
    private String ruleModels;

    public String[] ruleModels() {
        if (StringUtils.isBlank(ruleModels)) return null;
        return ruleModels.split(Constants.SPLIT);
    }

    public String getRuleWeight() {
        String[] ruleModels = this.ruleModels();
        if (null == ruleModels) return null;
        for (String ruleModel : ruleModels) {
            if ("rule_weight".equals(ruleModel)) return ruleModel;
        }
        return null;
    }

}
