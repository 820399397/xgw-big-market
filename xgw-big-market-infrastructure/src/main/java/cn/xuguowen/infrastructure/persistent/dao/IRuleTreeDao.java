package cn.xuguowen.infrastructure.persistent.dao;

import cn.xuguowen.infrastructure.persistent.po.RuleTree;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * ClassName: IRuleTreeDao
 * Package: cn.xuguowen.infrastructure.persistent.dao
 * Description:规则树持久化层
 *
 * @Author 徐国文
 * @Create 2024/7/16 10:40
 * @Version 1.0
 */
@Mapper
public interface IRuleTreeDao {

    /**
     * 根据规则树的id查询规则树
     * @param treeId    规则树ID
     * @return
     */
    RuleTree queryRuleTreeByTreeId(@Param("treeId") String treeId);
}
