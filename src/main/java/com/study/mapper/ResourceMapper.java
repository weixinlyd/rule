package com.study.mapper;

import com.study.entity.Resources;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceMapper {
    /**
     * @Author dw
     * @Description 获取所有的资源
     * @Date 2020/4/15 11:16
     * @Param
     * @return
     */
    public List<Resources> getAllResources();

}
