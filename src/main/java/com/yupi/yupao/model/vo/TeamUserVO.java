package com.yupi.yupao.model.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 队伍表
 *
 * @TableName team
 */
@Data
public class TeamUserVO implements Serializable {
    /**
     * 队伍id
     */
    private Long id;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 队伍描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    @TableField(value = "expireTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 后传前
    private Date expireTime;

    /**
     * 队长id
     */
    private Long userId;

    /**
     * 0-公开，1-私有，2-加密
     */
    private Integer status;

//    /**
//     * 密码
//     */
//    @TableField(value = "password")
//    private String password;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 后传前
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 后传前
    private Date updateTime;

//    /**
//     * 是否删除
//     */
//    @TableField(value = "isDelete")
//    @TableLogic
//    private Integer isDelete;

    /**
     * 创建人用户信息
     */
    private UserVo createUser;

    /**
     * 当前用户是否加入
     */
    private boolean hasJoin = false;

    /**
     * 已加入人数
     */
    private int hasJoinNum;

    private static final long serialVersionUID = 1L;
}