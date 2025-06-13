package com.mindskip.xzs.viewmodel.student.discussion;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class DiscussionReplyRequestVM {

    @NotNull(message = "讨论ID不能为空")
    private Long discussionId;

    @NotBlank(message = "回复内容不能为空")
    private String content;

    private Long parentId;

    public Long getDiscussionId() {
        return discussionId;
    }

    public void setDiscussionId(Long discussionId) {
        this.discussionId = discussionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}