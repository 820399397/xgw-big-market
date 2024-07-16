package cn.xuguowen.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: RuleTreeNodeLineVO
 * Package: cn.xuguowen.domain.strategy.model.valobj
 * Description:规则树节点指向线对象。用于衔接 from->to 节点链路关系.
 * 决策树节点连线，用于标识出怎么从一个节点到下一个节点。
 *
 * @Author 徐国文
 * @Create 2024/7/15 15:31
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RuleTreeNodeLineVO {
    /** 规则树ID */
    private String treeId;
    /** 规则Key节点 From */
    private String ruleNodeFrom;
    /** 规则Key节点 To */
    private String ruleNodeTo;
    /** 限定类型；1:=;2:>;3:<;4:>=;5<=;6:enum[枚举范围] */
    private RuleLimitTypeVO ruleLimitTypeVO;
    /** 限定值（到下个节点） */
    private RuleLogicCheckTypeVO ruleLogicTypeVO;
}
