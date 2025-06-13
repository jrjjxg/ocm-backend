package com.mindskip.xzs.repository;

import com.mindskip.xzs.domain.Discussion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussionMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Discussion record);

    int insertSelective(Discussion record);

    Discussion selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Discussion record);

    int updateByPrimaryKey(Discussion record);

    /**
     * 根据课程ID分页查询讨论列表
     */
    List<Discussion> selectByCourseId(@Param("courseId") Long courseId,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);

    /**
     * 根据课程ID统计讨论总数
     */
    int countByCourseId(@Param("courseId") Long courseId);

    /**
     * 增加浏览数
     */
    int increaseViewCount(@Param("id") Long id);

    /**
     * 增加回复数
     */
    int increaseReplyCount(@Param("id") Long id);

    /**
     * 减少回复数
     */
    int decreaseReplyCount(@Param("id") Long id);

    /**
     * 查询课程置顶讨论
     */
    List<Discussion> selectTopDiscussions(@Param("courseId") Long courseId);

    /**
     * 查询课程精华讨论
     */
    List<Discussion> selectEssenceDiscussions(@Param("courseId") Long courseId);

    /**
     * 根据创建者ID查询讨论
     */
    List<Discussion> selectByCreatorId(@Param("creatorId") Integer creatorId,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);

    /**
     * 验证用户是否有权限操作该讨论（是否为创建者）
     */
    int validateDiscussionOwner(@Param("id") Long id, @Param("creatorId") Integer creatorId);
}