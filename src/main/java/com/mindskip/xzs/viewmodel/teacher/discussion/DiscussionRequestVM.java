package com.mindskip.xzs.viewmodel.teacher.discussion;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class DiscussionRequestVM {

    @NotBlank(message = "讨论标题不能为空")
    private String title;

    @NotBlank(message = "讨论内容不能为空")
    private String content;

    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    private Boolean isTop = false;

    private Boolean isEssence = false;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Boolean getIsTop() {
        return isTop;
    }

    public void setIsTop(Boolean isTop) {
        this.isTop = isTop;
    }

    public Boolean getIsEssence() {
        return isEssence;
    }

    public void setIsEssence(Boolean isEssence) {
        this.isEssence = isEssence;
    }
}