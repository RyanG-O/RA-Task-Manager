package com.example.csia;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class AddFacilityController implements Initializable{
    @FXML
    private TextField facilityIDText;
    @FXML
    private TextField roomIDText;
    @FXML
    private TextField floorText;
    @FXML
    private TextField typeText;
    @FXML
    private ChoiceBox<String> maintenanceStatusChoice;

    private String query = null;
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private boolean update = false;
    protected Facility facility;
    @FXML

    //When the save button is clicked, validation checks are performed
    //Information is saved to the database if data passes the validation checks
    protected void onSaveClicked(){
        connection = DBUtils.getConnection();
        String facilityID = facilityIDText.getText().trim();
        String roomID = roomIDText.getText().trim();
        String floor = floorText.getText().trim();
        String type = typeText.getText().trim();

        if (facilityID.isEmpty()||roomID.isEmpty()||floor.isEmpty()||type.isEmpty()){
            DBUtils.errorAlert("Blank Text Fields");
        } else {
            try {
                Double.parseDouble(floor);
                if (Integer.valueOf(floor) <= 20 && Integer.valueOf(floor) >= -5){
                    if (update == true){
                        getQuery();
                        insert();
                        clear();
                    } else {
                        if (checkFacilityID(facilityID) == false){
                            getQuery();
                            insert();
                            clear();
                        } else{
                            DBUtils.errorAlert("Facility ID already exists");
                        }
                    }
                }
                else{
                    DBUtils.errorAlert("The floor does not exist");
                }
            } catch (NumberFormatException e) {
                DBUtils.errorAlert("Floor must be an integer");
            }
        }
    }
    private void getQuery(){
        if (update == false){
            query = "INSERT INTO Facilities (facilityID, roomID, floor, maintenanceStatus, type) VALUES (?,?,?,?,?)";
        } else {
            query = "UPDATE Facilities SET "
                    + "facilityID = ?, "
                    + "roomID = ?, "
                    + "floor = ?, "
                    + "maintenanceStatus = ?, "
                    + "type = ? WHERE facilityID = \"" + facility.getFacilityID() + "\"";
        }
    }
    private void insert(){
        try {
            Facility instanceFacility = new Facility();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,facilityIDText.getText());
            preparedStatement.setString(2,roomIDText.getText());
            preparedStatement.setString(3,floorText.getText());
            preparedStatement.setString(4,Integer.toString(instanceFacility.getIntMaintenanceStatus(maintenanceStatusChoice.getValue())));
            preparedStatement.setString(5,typeText.getText());
            preparedStatement.execute();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private void clear(){
        facilityIDText.clear();
        roomIDText.clear();
        floorText.clear();
        maintenanceStatusChoice.setValue("Not Urgent");
        typeText.clear();
    }

    protected void setUpdate(boolean b){
        this.update = b;
    }
    protected void setFacility(Facility facility){
        this.facility = facility;
    }
    protected void setTextField(String facilityID, String roomID, int floor, String maintenanceStatus, String type){
        facilityIDText.setText(facilityID);
        roomIDText.setText(roomID);
        floorText.setText(Integer.toString(floor));
        maintenanceStatusChoice.setValue(maintenanceStatus);
        typeText.setText(type);
    }
    protected boolean checkFacilityID(String facilityID){
        int count = 0;
        try{
            query = "SELECT * FROM Facilities WHERE facilityID = \"" + facilityID + "\"";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                count += 1;
            }

            if (count == 0){
                return false;
            } else{
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        maintenanceStatusChoice.getItems().addAll("Not Urgent","Slightly Urgent","Urgent","Very Urgent");
        maintenanceStatusChoice.setValue("Not Urgent");
    }
}