package com.github.sparkzxl.authorization.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.sparkzxl.database.annonation.InjectionField;
import com.github.sparkzxl.database.entity.Entity;
import com.github.sparkzxl.database.entity.RemoteData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import static com.github.sparkzxl.authorization.infrastructure.constant.InjectionFieldConstants.ORG_ID_CLASS;
import static com.github.sparkzxl.authorization.infrastructure.constant.InjectionFieldConstants.ORG_ID_METHOD;

/**
 * description: 岗位
 *
 * @author zhouxinlei
 * @date 2020-06-07 13:23:52
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("u_core_station")
@ApiModel(value = "CCoreStationDO对象", description = "岗位")
public class CoreStation extends Entity<Long> {

    private static final long serialVersionUID = -4924681990812046498L;

    @ApiModelProperty(value = "名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "组织ID")
    @TableField("org_id")
    @InjectionField(api = ORG_ID_CLASS, method = ORG_ID_METHOD, beanClass = CoreOrg.class)
    private RemoteData<Long, CoreOrg> org;

    @ApiModelProperty(value = "状态")
    @TableField("status")
    private Boolean status;

    @ApiModelProperty(value = "描述")
    @TableField("describe_")
    private String describe;

}
