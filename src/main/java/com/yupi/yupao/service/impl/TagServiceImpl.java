package com.yupi.yupao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yupao.model.domain.Tag;
import com.yupi.yupao.service.TagService;
import com.yupi.yupao.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
* @author Boer
* @description 针对表【tag(标签表)】的数据库操作Service实现
* @createDate 2023-10-27 17:03:40
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




