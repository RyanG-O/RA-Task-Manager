package com.example.csia;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class AddComplaintController implements Initializable{
    @FXML
    private TextField studentIDText;
    @FXML
    private TextField facilityIDText;
    @FXML
    private TextField roomIDText;
    @FXML
    private TextArea descriptionText;
    @FXML
    private ChoiceBox<String> urgencyChoice;
    @FXML
    private ChoiceBox<String> completionChoice;
    @FXML
    private DatePicker datePicker;

    private String query = null;
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private boolean update = false;
    protected Complaint complaint;

    @FXML
    protected void onSaveClicked(){
        connection = DBUtils.getConnection();

        String studentID = studentIDText.getText().trim();
        String facilityID = facilityIDText.getText().trim();
        String roomID = roomIDText.getText().trim();
        String description = descriptionText.getText().trim();
        String tempDate = datePicker.getEditor().getText().trim();

        if (studentID.isEmpty()||facilityID.isEmpty()||roomID.isEmpty()||description.isEmpty()||tempDate.isEmpty()){
            DBUtils.errorAlert("Blank Text Fields");
        } else {
            if (checkFacilityID(facilityID) == false){
                DBUtils.errorAlert("Facility ID does not exist");
            } else {
                if (checkStudentID(studentID) == false){
                    DBUtils.errorAlert("Student ID does not exist");
                } else {
                    getQuery();
                    insert();
                    clear();
                }
            }
        }
    }

    private void getQuery(){
        if (update == false){
            query = "INSERT INTO Complaints (studentID, facilityID, roomID, description, urgency, completion, date) VALUES (?,?,?,?,?,?,?)";
        } else {
            query = "UPDATE Complaints SET "
                    + "studentID = ?, "
                    + "facilityID = ?, "
                    + "roomID = ?, "
                    + "description = ?, "
                    + "urgency = ?, "
                    + "completion = ?, "
                    + "date = ? WHERE complaintID = \"" + complaint.getComplaintID() + "\"";
        }
    }

    private void insert(){
        try {
            Complaint instanceComplaint = new Complaint();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,studentIDText.getText());
            preparedStatement.setString(2,facilityIDText.getText());
            preparedStatement.setString(3,roomIDText.getText());
            preparedStatement.setString(4,descriptionText.getText());
            preparedStatement.setString(5,Integer.toString(instanceComplaint.getIntUrgency(urgencyChoice.getValue())));
            preparedStatement.setString(6,Integer.toString(instanceComplaint.getIntCompletion(completionChoice.getValue())));
            preparedStatement.setString(7,datePicker.getEditor().getText());
            preparedStatement.execute();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void clear(){
        studentIDText.clear();
        facilityIDText.clear();
        roomIDText.clear();
        descriptionText.clear();
        urgencyChoice.setValue("Not Urgent");
        completionChoice.setValue("Not Completed");
        datePicker.getEditor().clear();
    }

    protected void setUpdate(boolean b){
        this.update = b;
    }
    protected void setComplaint(Complaint complaint){
        this.complaint = complaint;
    }

    protected void setTextField(String studentID, String facilityID, String roomID, String description, String urgency, String completion, String date){
        studentIDText.setText(studentID);
        facilityIDText.setText(facilityID);
        roomIDText.setText(roomID);
        descriptionText.setText(description);
        urgencyChoice.setValue(urgency);
        completionChoice.setValue(completion);
        datePicker.getEditor().setText(date);
    }

    private boolean checkStudentID(String studentID){
        int count = 0;
        try{
            query = "SELECT * FROM Students WHERE studentID = \"" + studentID + "\"";
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

    private boolean checkFacilityID(String facilityID){
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
        urgencyChoice.getItems().addAll("Not Urgent","Slightly Urgent","Urgent","Very Urgent");
        urgencyChoice.setValue("Not Urgent");
        completionChoice.getItems().addAll("Not Completed","Completed");
        completionChoice.setValue("Not Completed");
    }
}
