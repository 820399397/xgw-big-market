package cn.xuguowen.infrastructure.persistent.dao;

import cn.xuguowen.infrastructure.persistent.po.RuleTreeNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ClassName: IRuleTreeNodeDao
 * Package: cn.xuguowen.infrastructure.persistent.dao
 * Description:规则树节点持久化层
 *
 * @Author 徐国文
 * @Create 2024/7/16 10:42
 * @Version 1.0
 */
@Mapper
public interface IRuleTreeNodeDao {
    /**
     * 根据规则树ID查询规则树节点信息
     * @param treeId    规则树ID
     * @return
     */
    List<RuleTreeNode> queryRuleTreeNodeByTreeId(@Param("treeId") String treeId);
}
