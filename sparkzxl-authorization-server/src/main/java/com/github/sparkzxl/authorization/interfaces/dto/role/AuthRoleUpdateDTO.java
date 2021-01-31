package com.github.sparkzxl.authorization.interfaces.dto.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * description：角色更新对象
 *
 * @author zhouxinlei
 * @date 2020/6/16 0016
 */
@Data
@ApiModel("角色更新对象")
public class AuthRoleUpdateDTO {

    @ApiModelProperty(value = "id")
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "描述")
    private String describe;

    @ApiModelProperty(value = "状态")
    private Boolean status;

    @ApiModelProperty(value = "是否内置角色")
    private Boolean readonly;

    @ApiModelProperty(value = "数据权限类型 {ALL:1,全部;THIS_LEVEL:2,本级;THIS_LEVEL_CHILDREN:3,本级以及子级;CUSTOMIZE:4,自定义;SELF:5,个人;} ")
    private String dsType;

}
