package com.yupi.yupao.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 队伍表
 *
 * @TableName team
 */
@TableName(value = "team")
@Data
public class Team implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 队伍名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 队伍描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 最大人数
     */
    @TableField(value = "maxNum")
    private Integer maxNum;

    /**
     * 过期时间
     */
    @TableField(value = "expireTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 前传后
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 后传前
    private Date expireTime;

    /**
     * 队长id
     */
    @TableField(value = "userId")
    private Long userId;

    /**
     * 0-公开，1-私有，2-加密
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 后传前
    private Date createTime;

    /**
     *
     */
    @TableField(value = "updateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 后传前
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableField(value = "isDelete")
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}