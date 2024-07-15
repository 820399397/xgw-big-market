package cn.xuguowen.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName: RuleLimitTypeVO
 * Package: cn.xuguowen.domain.strategy.model.valobj
 * Description:规则限定枚举值
 *
 * @Author 徐国文
 * @Create 2024/7/15 15:33
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum RuleLimitTypeVO {

    EQUAL(1, "等于"),
    GT(2, "大于"),
    LT(3, "小于"),
    GE(4, "大于&等于"),
    LE(5, "小于&等于"),
    ENUM(6, "枚举"),
    ;

    private final Integer code;
    private final String info;

}
