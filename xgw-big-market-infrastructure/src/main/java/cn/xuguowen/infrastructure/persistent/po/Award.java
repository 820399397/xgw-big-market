package cn.xuguowen.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * ClassName: award
 * Package: cn.xuguowen.infrastructure.persistent.po
 * Description:奖品实体类，对应数据库 award 表
 *
 * @Author 徐国文
 * @Create 2024/7/1 10:07
 * @Version 1.0
 */
@Data
public class Award {

    /** 自增ID */
    private Long id;
    /** 抽奖奖品ID - 内部流转使用 */
    private Long awardId;
    /** 奖品对接标识 - 每一个都是一个对应的发奖策略 */
    private String awardKey;
    /** 奖品配置信息 */
    private String awardConfig;
    /** 奖品内容描述 */
    private String awardDesc;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;

}
