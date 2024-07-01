package cn.xuguowen.infrastructure.persistent.dao;

import cn.xuguowen.infrastructure.persistent.po.Award;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ClassName: IAwardDao
 * Package: cn.xuguowen.infrastructure.persistent.dao
 * Description:奖品持久化层
 *
 * @Author 徐国文
 * @Create 2024/7/1 10:55
 * @Version 1.0
 */
@Mapper
public interface IAwardDao {
    List<Award> queryAwardList();
}
