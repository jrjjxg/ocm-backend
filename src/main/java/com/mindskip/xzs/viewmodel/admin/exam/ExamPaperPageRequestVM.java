package com.mindskip.xzs.viewmodel.admin.exam;

import com.mindskip.xzs.base.BasePage;



public class ExamPaperPageRequestVM extends BasePage {

    private Integer id;
    private Integer subjectId;
    private Integer level;
    private Integer paperType;
    private Integer taskExamId;
    private Integer createUser;
    private Boolean deleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getPaperType() {
        return paperType;
    }

    public void setPaperType(Integer paperType) {
        this.paperType = paperType;
    }

    public Integer getTaskExamId() {
        return taskExamId;
    }

    public void setTaskExamId(Integer taskExamId) {
        this.taskExamId = taskExamId;
    }
    
    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
