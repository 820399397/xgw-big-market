package cn.xuguowen.test.vo2dto;

import org.junit.Test;

/**
 * ClassName: TestPlugs
 * Package: cn.xuguowen.test.vo2dto
 * Description:测试小傅哥的插件
 *
 * @Author 徐国文
 * @Create 2024/7/1 13:25
 * @Version 1.0
 */
public class TestPlugs {

    @Test
    public void testVO2DTO(UserVO userVO) {
        UserDTO userDTO = UserDTO.builder()
                .name(userVO.getName())
                .phone(userVO.getPhone())
                .build();
    }
}
