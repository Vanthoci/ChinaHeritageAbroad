package com.ujcms.cms.ext.domain.base;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

/**
 * This class was generated by MyBatis Generator.
 *
 * @author MyBatis Generator
 */
public class SurveyItemFeedbackBase implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 数据库表名
     */
    public static final String TABLE_NAME = "survey_item_feedback";

    @NotNull
    private Integer id = 0;

    /**
     * 问卷选项ID
     */
    @NotNull
    @Schema(description="问卷选项ID")
    private Integer surveyItemId = 0;

    /**
     * 问卷反馈ID
     */
    @NotNull
    @Schema(description="问卷反馈ID")
    private Integer surveyFeedbackId = 0;

    /**
     * 问卷ID
     */
    @NotNull
    @Schema(description="问卷ID")
    private Integer surveyId = 0;

    /**
     * 回答
     */
    @Nullable
    @Schema(description="回答")
    private String answer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSurveyItemId() {
        return surveyItemId;
    }

    public void setSurveyItemId(Integer surveyItemId) {
        this.surveyItemId = surveyItemId;
    }

    public Integer getSurveyFeedbackId() {
        return surveyFeedbackId;
    }

    public void setSurveyFeedbackId(Integer surveyFeedbackId) {
        this.surveyFeedbackId = surveyFeedbackId;
    }

    public Integer getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Integer surveyId) {
        this.surveyId = surveyId;
    }

    @Nullable
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(@Nullable String answer) {
        this.answer = answer;
    }
}