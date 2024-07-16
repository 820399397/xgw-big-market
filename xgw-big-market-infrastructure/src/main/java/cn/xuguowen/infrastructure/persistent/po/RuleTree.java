package cn.xuguowen.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * ClassName: RuleTree
 * Package: cn.xuguowen.infrastructure.persistent.po
 * Description:规则树实体类，对应数据库 rule_tree 表
 * CTRL + W 选中一个关键字或者单词
 *
 * @Author 徐国文
 * @Create 2024/7/16 10:26
 * @Version 1.0
 */
@Data
public class RuleTree {

    /** 自增ID */
    private Long id;
    /** 规则树ID */
    private String treeId;
    /** 规则树名称 */
    private String treeName;
    /** 规则树描述 */
    private String treeDesc;
    /** 规则树根入口规则 */
    private String treeNodeRuleKey;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

}
