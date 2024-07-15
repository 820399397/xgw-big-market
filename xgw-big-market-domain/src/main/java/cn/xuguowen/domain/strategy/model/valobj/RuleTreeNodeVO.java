package cn.xuguowen.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ClassName: RuleTreeNodeVO
 * Package: cn.xuguowen.domain.strategy.model.valobj
 * Description:规则树节点对象:决策树的节点，这些节点可以组合出任意需要的规则树。
 *
 * @Author 徐国文
 * @Create 2024/7/15 15:29
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RuleTreeNodeVO {
    /** 规则树ID */
    private Integer treeId;
    /** 规则Key */
    private String ruleKey;
    /** 规则比值 */
    private String ruleValue;
    /** 规则描述 */
    private String ruleDesc;
    /** 规则连线 */
    private List<RuleTreeNodeLineVO> treeNodeLineVOList;
}
