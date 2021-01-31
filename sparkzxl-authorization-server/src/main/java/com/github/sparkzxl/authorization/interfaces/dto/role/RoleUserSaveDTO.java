package com.github.sparkzxl.authorization.interfaces.dto.role;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * description：
 *
 * @author zhouxinlei
 * @date 2020/6/16 0016
 */
@Data
public class RoleUserSaveDTO {

    @NotNull(message = "角色id不能为空")
    private Long roleId;

    private List<Long> userIds;

}
