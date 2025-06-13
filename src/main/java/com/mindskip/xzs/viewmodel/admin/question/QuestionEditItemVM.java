package com.mindskip.xzs.viewmodel.admin.question;

import javax.validation.constraints.NotBlank;
import javax.validation.groups.Default;

public class QuestionEditItemVM {
    @NotBlank
    private String prefix;
    @NotBlank(groups = { ChoiceQuestion.class })
    private String content;

    private String score;

    private String itemUuid;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getItemUuid() {
        return itemUuid;
    }

    public void setItemUuid(String itemUuid) {
        this.itemUuid = itemUuid;
    }

}
