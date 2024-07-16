package cn.xuguowen.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * ClassName: RuleTreeNode
 * Package: cn.xuguowen.infrastructure.persistent.po
 * Description:规则树节点实体类，对应数据库 rule_tree_node 表
 *
 * @Author 徐国文
 * @Create 2024/7/16 10:34
 * @Version 1.0
 */
@Data
public class RuleTreeNode {

    /** 自增ID */
    private Long id;
    /** 规则树ID */
    private String treeId;
    /** 规则Key */
    private String ruleKey;
    /** 规则描述 */
    private String ruleDesc;
    /** 规则比值 */
    private String ruleValue;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

}
