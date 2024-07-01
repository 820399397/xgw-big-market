package cn.xuguowen.test.vo2dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: UserVO
 * Package: cn.xuguowen.test.vo2dto
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/7/1 13:24
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
    private String name;
    private String phone;
}
