package com.github.sparkzxl.authorization.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.sparkzxl.database.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * description: 角色
 *
 * @author zhouxinlei
 * @date  2020-06-07 13:25:40
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("u_auth_role")
@ApiModel(value="CAuthRoleDO对象", description="角色")
public class AuthRole extends Entity<Long> {

    private static final long serialVersionUID = 6879339693496966527L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "编码")
    @TableField("code")
    private String code;

    @ApiModelProperty(value = "描述")
    @TableField("describe_")
    private String describe;

    @ApiModelProperty(value = "状态")
    @TableField("status")
    private Boolean status;

    @ApiModelProperty(value = "是否内置角色")
    @TableField("readonly")
    private Boolean readonly;

    @ApiModelProperty(value = "数据权限类型 {ALL:1,全部;THIS_LEVEL:2,本级;THIS_LEVEL_CHILDREN:3,本级以及子级;CUSTOMIZE:4,自定义;SELF:5,个人;} ")
    @TableField("ds_type")
    private String dsType;

}
