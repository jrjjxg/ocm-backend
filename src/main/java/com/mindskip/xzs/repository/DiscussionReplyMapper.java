package com.mindskip.xzs.repository;

import com.mindskip.xzs.domain.DiscussionReply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussionReplyMapper {

    int deleteByPrimaryKey(Long id);

    int insert(DiscussionReply record);

    int insertSelective(DiscussionReply record);

    DiscussionReply selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DiscussionReply record);

    int updateByPrimaryKey(DiscussionReply record);

    /**
     * 根据讨论ID查询回复列表（分页）
     */
    List<DiscussionReply> selectByDiscussionId(@Param("discussionId") Long discussionId,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);

    /**
     * 根据讨论ID统计回复总数
     */
    int countByDiscussionId(@Param("discussionId") Long discussionId);

    /**
     * 根据父回复ID查询子回复
     */
    List<DiscussionReply> selectByParentId(@Param("parentId") Long parentId);

    /**
     * 删除讨论下的所有回复
     */
    int deleteByDiscussionId(@Param("discussionId") Long discussionId);

    /**
     * 验证用户是否有权限操作该回复（是否为回复者）
     */
    int validateReplyOwner(@Param("id") Long id, @Param("replierId") Integer replierId);

    /**
     * 根据回复者ID查询回复
     */
    List<DiscussionReply> selectByReplierId(@Param("replierId") Integer replierId,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);
}