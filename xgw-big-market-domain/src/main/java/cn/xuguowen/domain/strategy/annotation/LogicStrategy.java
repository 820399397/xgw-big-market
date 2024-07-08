package cn.xuguowen.domain.strategy.annotation;

import cn.xuguowen.domain.strategy.service.rule.factory.DefaultLogicFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: LogicStrategy
 * Package: cn.bugstack.chatgpt.data.domain.openai.annotation
 * Description:规则枚举对象，只有加了当前注解的规则才会被规则工厂进行实例化
 *
 * @Author 徐国文
 * @Create 2024/5/17 10:09
 * @Version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogicStrategy {
    DefaultLogicFactory.LogicModel logicMode();
}
