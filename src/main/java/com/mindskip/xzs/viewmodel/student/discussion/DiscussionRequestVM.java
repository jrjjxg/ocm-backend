package com.mindskip.xzs.viewmodel.student.discussion;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class DiscussionRequestVM {

    @NotBlank(message = "讨论标题不能为空")
    private String title;

    @NotBlank(message = "讨论内容不能为空")
    private String content;

    @NotNull(message = "课程ID不能为空")
    private Long courseId;

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
}