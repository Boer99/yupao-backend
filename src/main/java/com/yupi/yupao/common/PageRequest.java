package com.yupi.yupao.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用分页请求类，继承
 */
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = -2013479450862907350L;
    /**
     * 页大小
     */
    protected int pageSize=10;

    /**
     * 当前页
     */
    protected int pageNum=1;
}
