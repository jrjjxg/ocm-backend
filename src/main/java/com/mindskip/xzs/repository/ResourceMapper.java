package com.mindskip.xzs.repository;

import com.mindskip.xzs.domain.Resource;
import com.mindskip.xzs.repository.BaseMapper; // Assuming you have a BaseMapper
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {
    
    int insertSelective(Resource record);

    List<Resource> selectByCourseId(Long courseId);
    
    Resource selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Resource record);

    int deleteByPrimaryKey(Long id);

    // If BaseMapper is used and provides these, these explicit declarations might be redundant
    // unless specific XML mapping is desired that differs from BaseMapper's provider.
} 