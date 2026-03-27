package com.example.csia;

public class Facility {
    private String facilityID;
    private String roomID;
    private int floor;
    private String maintenanceStatus;
    private String type;

    public Facility(String facilityID, String roomID, int floor, int maintenanceStatus, String type){
        this.facilityID = facilityID;
        this.roomID = roomID;
        this.floor = floor;
        this.maintenanceStatus = getStringMaintenanceStatus(maintenanceStatus);
        this.type = type;
    }
    public Facility(){}

    public String getFacilityID() {
        return facilityID;
    }

    public void setFacilityID(String facilityID) {
        this.facilityID = facilityID;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getMaintenanceStatus() {
        return maintenanceStatus;
    }

    public void setMaintenanceStatus(String maintenanceStatus) {
        this.maintenanceStatus = maintenanceStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    protected String getStringMaintenanceStatus(int maintenanceStatus){
        if (maintenanceStatus == 1){
            return "Not Urgent";
        }
        if(maintenanceStatus == 2){
            return "Slightly Urgent";
        }
        if (maintenanceStatus == 3){
            return "Urgent";
        }
        if (maintenanceStatus == 4){
            return "Very Urgent";
        } else {
            return null;
        }
    }
    protected int getIntMaintenanceStatus(String maintenanceStatus){
        if (maintenanceStatus == "Not Urgent"){
            return 1;
        }
        if(maintenanceStatus == "Slightly Urgent"){
            return 2;
        }
        if (maintenanceStatus == "Urgent"){
            return 3;
        }
        if (maintenanceStatus == "Very Urgent"){
            return 4;
        } else {
            return -1;
        }
    }
}