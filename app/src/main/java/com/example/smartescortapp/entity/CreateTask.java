package com.example.smartescortapp.entity;


/**
 * @author 章可政
 * @create 2021/4/14
 */

public class CreateTask {
    /**
     * 用户编号，之后上传信息需要用到
     */
    private String user_account;
    /**
     * 任务类型；0：押解；1：就医；2：指证；
     */
    private int TaskType;
    /**
     * 标签ID
     */
    private String terminal_code;
    /**
     * 被监管人员编号；
     */
    private String PersonnelID;

    /**
     * 被监管人员姓名；中文使用Unicode
     */
    private String PersonnelName;

    /**
     * 安全距离（m）
     */
    private String safeDistance;

    /**
     * 目的地；中文使用Unicode
     */
    private String Destination;

    public String getUser_account() {
        return user_account;
    }

    public void setUser_account(String user_account) {
        this.user_account = user_account;
    }

    public int getTaskType() {
        return TaskType;
    }

    public void setTaskType(int taskType) {
        TaskType = taskType;
    }

    public String getTerminal_code() {
        return terminal_code;
    }

    public void setTerminal_code(String terminal_code) {
        this.terminal_code = terminal_code;
    }

    public String getPersonnelID() {
        return PersonnelID;
    }

    public void setPersonnelID(String personnelID) {
        PersonnelID = personnelID;
    }

    public String getPersonnelName() {
        return PersonnelName;
    }

    public void setPersonnelName(String personnelName) {
        PersonnelName = personnelName;
    }

    public String getSafeDistance() {
        return safeDistance;
    }

    public void setSafeDistance(String safeDistance) {
        this.safeDistance = safeDistance;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }
}
