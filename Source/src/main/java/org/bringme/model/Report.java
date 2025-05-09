package org.bringme.model;

/**
 * Model of how the report entity represented in the database
 */
public class Report {
    private Long id;
    /**
     * On which request, implemented report
     */
    private int requestId;
    /**
     * User, who sent\wrote the report
     */
    private int reporterUserId;

    /**
     * User who has been reported
     */
    private int reportedUserId;

    /**
     * Admin who answered the report (it can be null if the report isn't answered yet)
     */
    private int answeredById;
    private String content;
    private String answer;

    public Report(){}

    public Report(Long id, int requestId, int reporterUserId, int reportedUserId, int answeredById, String content, String answer) {
        this.id = id;
        this.requestId = requestId;
        this.reporterUserId = reporterUserId;
        this.reportedUserId = reportedUserId;
        this.answeredById = answeredById;
        this.content = content;
        this.answer = answer;
    }

    public Report(int requestId, int reporterUserId, int reportedUserId, String content) {
        this.requestId = requestId;
        this.reporterUserId = reporterUserId;
        this.reportedUserId = reportedUserId;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getReporterUserId() {
        return reporterUserId;
    }

    public void setReporterUserId(int reporterUserId) {
        this.reporterUserId = reporterUserId;
    }

    public int getReportedUserId() {
        return reportedUserId;
    }

    public void setReportedUserId(int reportedUserId) {
        this.reportedUserId = reportedUserId;
    }

    public int getAnsweredById() {
        return answeredById;
    }

    public void setAnsweredById(int answeredById) {
        this.answeredById = answeredById;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", requestId=" + requestId +
                ", reporterUserId=" + reporterUserId +
                ", reportedUserId=" + reportedUserId +
                ", answeredById=" + answeredById +
                ", content='" + content + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
