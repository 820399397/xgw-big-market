package cn.xuguowen.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ClassName: StrategyAwardRuleModelVO
 * Package: cn.xuguowen.domain.strategy.model.valobj
 * Description:抽奖策略规则规则值对象；值对象，没有唯一ID，仅限于从数据库查询对象
 *
 * @Author 徐国文
 * @Create 2024/7/8 10:27
 * @Version 1.0
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardRuleModelVO {
    private String ruleModels;
}
