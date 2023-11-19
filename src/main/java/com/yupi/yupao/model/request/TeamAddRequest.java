package com.yupi.yupao.model.request;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class TeamAddRequest implements Serializable {
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 前传后
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 后传前
    private Date expireTime;

    /**
     * 0-公开，1-私有，2-加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;

    private static final long serialVersionUID = 1L;
}