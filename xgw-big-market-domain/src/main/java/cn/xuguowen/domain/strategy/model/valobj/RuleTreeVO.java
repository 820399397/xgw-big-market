package cn.xuguowen.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * ClassName: RuleTreeVO
 * Package: cn.xuguowen.domain.strategy.model.valobj
 * Description:规则树对象【注意；不具有唯一ID，不需要改变数据库结果的对象，可以被定义为值对象】
 * 决策树的树根信息，标记出最开始从哪个节点执行「treeRootRuleNode」。
 *
 * @Author 徐国文
 * @Create 2024/7/15 15:27
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RuleTreeVO {
    /** 规则树ID */
    private Integer treeId;
    /** 规则树名称 */
    private String treeName;
    /** 规则树描述 */
    private String treeDesc;
    /** 规则根节点 */
    private String treeRootRuleNode;
    /** 规则节点 */
    private Map<String,RuleTreeNodeVO> treeNodeMap;

}
