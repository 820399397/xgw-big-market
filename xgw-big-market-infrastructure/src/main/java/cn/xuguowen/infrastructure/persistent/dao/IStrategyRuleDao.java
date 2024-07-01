package cn.xuguowen.infrastructure.persistent.dao;

import cn.xuguowen.infrastructure.persistent.po.StrategyRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ClassName: IStrategyRuleDao
 * Package: cn.xuguowen.infrastructure.persistent.dao
 * Description:抽奖规则持久化层
 *
 * @Author 徐国文
 * @Create 2024/7/1 13:21
 * @Version 1.0
 */
@Mapper
public interface IStrategyRuleDao {

    List<StrategyRule> queryStrategyRuleList();
}
