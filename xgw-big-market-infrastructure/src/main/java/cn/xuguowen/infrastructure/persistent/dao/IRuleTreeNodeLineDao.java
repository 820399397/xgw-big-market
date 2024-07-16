package cn.xuguowen.infrastructure.persistent.dao;

import cn.xuguowen.infrastructure.persistent.po.RuleTreeNodeLine;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ClassName: IRuleTreeNodeLineDao
 * Package: cn.xuguowen.infrastructure.persistent.dao
 * Description:规则节点连线持久化层
 *
 * @Author 徐国文
 * @Create 2024/7/16 10:44
 * @Version 1.0
 */
@Mapper
public interface IRuleTreeNodeLineDao {

    /**
     * 根据规则树ID查询规则节点连线信息
     * @param treeId    规则ID
     * @return
     */
    List<RuleTreeNodeLine> queryRuleTreeNodeLineByTreeId(@Param("treeId") String treeId);
}
