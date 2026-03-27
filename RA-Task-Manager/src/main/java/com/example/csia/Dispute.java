package com.example.csia;

public class Dispute extends Complaint{

    private String disputeID;
    private String studentID1;
    private String studentID2;
    private String description;
    private String urgency;
    private String completion;
    private String date;

    public Dispute(String disputeID, String studentID1, String studentID2, String description, int urgency, int completion, String date){
        this.disputeID = disputeID;
        this.studentID1 = studentID1;
        this.studentID2 = studentID2;
        this.description = description;
        this.urgency = getStringUrgency(urgency);
        this.completion = getStringCompletion(completion);
        this.date = date;
    }

    public Dispute(){}

    public String getDisputeID() {
        return disputeID;
    }

    public void setDisputeID(String disputeID) {
        this.disputeID = disputeID;
    }

    public String getStudentID1() {
        return studentID1;
    }

    public void setStudentID1(String studentID1) {
        this.studentID1 = studentID1;
    }

    public String getStudentID2() {
        return studentID2;
    }

    public void setStudentID2(String studentID2) {
        this.studentID2 = studentID2;
    }
    public String getCompletion(){
        return this.completion;
    }
    public void setCompletion(String completion){
        this.completion = completion;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getUrgency() {
        return urgency;
    }

    @Override
    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }
}
