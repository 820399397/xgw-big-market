package cn.xuguowen.domain.strategy.model.valobj;

import cn.xuguowen.domain.strategy.service.rule.factory.DefaultLogicFactory;
import cn.xuguowen.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: StrategyAwardRuleModelVO
 * Package: cn.xuguowen.domain.strategy.model.valobj
 * Description:抽奖策略规则规则值对象；值对象，没有唯一ID，仅限于从数据库查询对象
 *
 * @Author 徐国文
 * @Create 2024/7/8 10:27
 * @Version 1.0
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardRuleModelVO {
    private String ruleModels;

    /**
     * 获取抽奖中规则；或者使用 lambda 表达式
     * <p>
     * List<String> ruleModelList = Arrays.stream(ruleModels.split(Constants.SPLIT))
     * .filter(DefaultLogicFactory.LogicModel::isCenter)
     * .collect(Collectors.toList());
     * return ruleModelList;
     * <p>
     * List<String> collect = Arrays.stream(ruleModelValues).filter(DefaultLogicFactory.LogicModel::isCenter).collect(Collectors.toList());
     */
    public String[] raffleCenterRuleModelList() {
        List<String> ruleModelList = new ArrayList<>();
        String[] ruleModelValues = ruleModels.split(Constants.SPLIT);
        for (String ruleModelValue : ruleModelValues) {
            if (DefaultLogicFactory.LogicModel.isCenter(ruleModelValue)) {
                ruleModelList.add(ruleModelValue);
            }
        }
        return ruleModelList.toArray(new String[0]);
    }

    public String[] raffleAfterRuleModelList() {
        List<String> ruleModelList = new ArrayList<>();
        String[] ruleModelValues = ruleModels.split(Constants.SPLIT);
        for (String ruleModelValue : ruleModelValues) {
            if (DefaultLogicFactory.LogicModel.isAfter(ruleModelValue)) {
                ruleModelList.add(ruleModelValue);
            }
        }
        return ruleModelList.toArray(new String[0]);
    }

}
