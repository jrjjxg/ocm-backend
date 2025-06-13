package com.mindskip.xzs.repository;

import com.mindskip.xzs.domain.Resource;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ResourceMapper {

    int insertSelective(Resource record);

    List<Resource> selectByCourseId(Long courseId);

    Resource selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Resource record);

    int deleteByPrimaryKey(Long id);

    int insert(Resource record);

    int updateByPrimaryKey(Resource record);
}