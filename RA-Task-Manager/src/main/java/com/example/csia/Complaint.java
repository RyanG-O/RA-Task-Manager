package com.example.csia;

public class Complaint {
    private String complaintID;
    private String studentID;
    private String facilityID;
    private String roomID;
    private String description;
    private String urgency;
    private String completion;
    private String date;

    public Complaint(String complaintID, String studentID, String facilityID, String roomID, String description, int urgency, int completion, String date){
        this.complaintID = complaintID;
        this.studentID = studentID;
        this.facilityID = facilityID;
        this.roomID = roomID;
        this.description = description;
        this.urgency = getStringUrgency(urgency);
        this.completion = getStringCompletion(completion);
        this.date = date;
    }

    public Complaint(){
        this.complaintID = null;
        this.studentID = null;
        this.facilityID = null;
        this.roomID = null;
        this.description = null;
        this.urgency = null;
        this.completion = null;
        this.date = null;
    }

    public String getComplaintID() {
        return complaintID;
    }

    public void setComplaintID(String complaintID) {
        this.complaintID = complaintID;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public void setFacilityID(String facilityID) {
        this.facilityID = facilityID;
    }

    public String getFacilityID() {
        return facilityID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getDate() {
        return date;
    }

    public String getCompletion() {
        return completion;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCompletion(String completion) {
        this.completion = completion;
    }

    public String getStringUrgency(int urgency){
        if (urgency == 1){
            return "Not Urgent";
        }
        if (urgency == 2){
            return "Slightly Urgent";
        }
        if (urgency == 3){
            return "Urgent";
        }
        if (urgency == 4){
            return "Very Urgent";
        } else {
            return null;
        }
    }

    public int getIntUrgency(String urgency){
        if (urgency == "Not Urgent"){
            return 1;
        }
        if (urgency == "Slightly Urgent"){
            return 2;
        }
        if (urgency == "Urgent"){
            return 3;
        }
        if (urgency == "Very Urgent"){
            return 4;
        } else {
            return -1;
        }
    }
    public String getStringCompletion(int completion){
        if (completion == 1){
            return "Completed";
        }
        if (completion == 0){
            return "Not Completed";
        } else {
            return "Not Completed";
        }
    }
    public int getIntCompletion(String completion){
        if (completion == "Completed"){
            return 1;
        }
        if (completion == "Not Completed"){
            return 0;
        } else {
            return 0;
        }
    }
}
