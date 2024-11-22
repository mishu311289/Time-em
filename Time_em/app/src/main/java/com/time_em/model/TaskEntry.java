package com.time_em.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TaskEntry implements Parcelable {

    private int id, activityId, taskId, userId;
    private String taskName, comments, startTime, createdDate, endTime, selectedDate, token, attachmentImageFile,Isoffline,
            UniqueNumber,companyId;
    private Double timeSpent, signedInHours,signedOutHours;
    private Boolean isActive = true;

    public int getId() {
        return id;
    }

    public String getAttachmentImageFile() {
        return attachmentImageFile;
    }

    public void setAttachmentImageFile(String attachmentImageFile) {
        this.attachmentImageFile = attachmentImageFile;
    }

    public String getUniqueNumber() {
        return UniqueNumber;
    }

    public void setUniqueNumber(String uniqueNumber) {
        UniqueNumber = uniqueNumber;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Double getSignedOutHours() {
        return signedOutHours;
    }

    public void setSignedOutHours(Double signedOutHours) {
        this.signedOutHours = signedOutHours;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Double getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Double timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Double getSignedInHours() {
        return signedInHours;
    }

    public void setSignedInHours(Double signedInHours) {
        this.signedInHours = signedInHours;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getIsoffline() {
        return Isoffline;
    }

    public void setIsoffline(String isoffline) {
        Isoffline = isoffline;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public TaskEntry() {

    }

    public TaskEntry(Parcel source) {
        id = source.readInt();
        activityId = source.readInt();
        taskId = source.readInt();
        userId = source.readInt();
        taskName = source.readString();
        comments = source.readString();
        startTime = source.readString();
        createdDate = source.readString();
        endTime = source.readString();
        selectedDate = source.readString();
        token = source.readString();
        timeSpent = source.readDouble();
        attachmentImageFile = source.readString();
        isActive = source.readByte() != 0;
        Isoffline = source.readString();
        companyId = source.readString();

        try {
            signedInHours = source.readDouble();
           // signedOutHours = source.readDouble();
            UniqueNumber=source.readString();
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public static final Parcelable.Creator<TaskEntry> CREATOR
            = new Parcelable.Creator<TaskEntry>() {
        public TaskEntry createFromParcel(Parcel in) {
            return new TaskEntry(in);
        }

        public TaskEntry[] newArray(int size) {
            return new TaskEntry[size];
        }
    };

    public static Creator<TaskEntry> getCREATOR() {
        return CREATOR;
    }

    public Boolean getActive() {
        return isActive;

    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public int describeContents() {

        // TODO Auto-generated method stub
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeInt(activityId);
        dest.writeInt(taskId);
        dest.writeInt(userId);
        dest.writeString(taskName);
        dest.writeString(comments);
        dest.writeString(startTime);
        dest.writeString(createdDate);
        dest.writeString(endTime);
        dest.writeString(selectedDate);
        dest.writeString(token);
        dest.writeDouble(timeSpent);
        dest.writeString(attachmentImageFile);
        dest.writeByte((byte) (isActive ? 1 : 0));
        dest.writeString(Isoffline);
        dest.writeString(companyId);
       try {
            dest.writeDouble(signedInHours);
           // dest.writeDouble(signedOutHours);
           dest.writeString(UniqueNumber);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
