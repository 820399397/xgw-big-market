package cn.xuguowen.domain.strategy.service.rule.filter;

import cn.xuguowen.domain.strategy.model.entity.RuleActionEntity;
import cn.xuguowen.domain.strategy.model.entity.RuleMatterEntity;

/**
 * ClassName: ILogicFilter
 * Package: cn.xuguowen.domain.strategy.service.rule
 * Description:抽奖规则过滤接口
 *
 * @Author 徐国文
 * @Create 2024/7/5 11:51
 * @Version 1.0
 */
public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity> {

    RuleActionEntity<T> filter(RuleMatterEntity ruleMatterEntity);
}
