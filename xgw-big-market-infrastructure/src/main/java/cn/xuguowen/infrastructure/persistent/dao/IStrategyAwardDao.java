package cn.xuguowen.infrastructure.persistent.dao;

import cn.xuguowen.infrastructure.persistent.po.StrategyAward;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ClassName: IStrategyAwardDao
 * Package: cn.xuguowen.infrastructure.persistent.dao
 * Description:抽奖奖品持久化
 *
 * @Author 徐国文
 * @Create 2024/7/1 10:56
 * @Version 1.0
 */
@Mapper
public interface IStrategyAwardDao {

    List<StrategyAward> queryStrategyAwardList();
}
