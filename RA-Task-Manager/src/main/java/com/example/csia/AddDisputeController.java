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

public class AddDisputeController implements Initializable {
    @FXML
    private TextField studentID1Text;
    @FXML
    private TextField studentID2Text;
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
    protected Dispute dispute;

    @FXML
    protected void onSaveClicked(){
        connection = DBUtils.getConnection();

        String studentID1 = studentID1Text.getText().trim();
        String studentID2 = studentID2Text.getText().trim();
        String description = descriptionText.getText().trim();
        String tempDate = datePicker.getEditor().getText().trim();

        if (studentID1.isEmpty()||studentID2.isEmpty()||description.isEmpty()|| tempDate.isEmpty()){
            DBUtils.errorAlert("Blank Text Fields");
        } else {
            if (checkStudentID(studentID1) == false){
                DBUtils.errorAlert("Student ID 1 does not exist");
            }
            else {
                if (checkStudentID(studentID2) == false){
                    DBUtils.errorAlert("Student ID 2 does not exist");
                } else{
                    getQuery();
                    insert();
                    clear();
                }
            }
        }
    }

    private void getQuery(){
        if (update == false){
            query = "INSERT INTO Disputes (studentID1, studentID2, description, urgency, date, completion) VALUES (?,?,?,?,?,?)";
        } else {
            query = "UPDATE Disputes SET "
                    + "studentID1 = ?, "
                    + "studentID2 = ?, "
                    + "description = ?, "
                    + "urgency = ?, "
                    + "date = ?, "
                    + "completion = ? WHERE disputeID = \"" + dispute.getDisputeID() + "\"";
        }
    }

    private void insert(){
        try {
            Dispute instanceDispute = new Dispute();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,studentID1Text.getText());
            preparedStatement.setString(2,studentID2Text.getText());
            preparedStatement.setString(3,descriptionText.getText());
            preparedStatement.setString(4,Integer.toString(instanceDispute.getIntUrgency(urgencyChoice.getValue())));
            preparedStatement.setString(5,datePicker.getEditor().getText());
            preparedStatement.setString(6,Integer.toString(instanceDispute.getIntCompletion(completionChoice.getValue())));
            preparedStatement.execute();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void clear(){
        studentID1Text.clear();
        studentID2Text.clear();
        descriptionText.clear();
        urgencyChoice.setValue("Not Urgent");
        completionChoice.setValue("Not Completed");
        datePicker.getEditor().clear();
    }

    protected void setDispute(Dispute dispute){
        this.dispute = dispute;
    }

    protected void setTextField(String studentID1, String studentID2, String description, String urgency, String completion,String date){
        studentID1Text.setText(studentID1);
        studentID2Text.setText(studentID2);
        descriptionText.setText(description);
        urgencyChoice.setValue(urgency);
        completionChoice.setValue(completion);
        datePicker.getEditor().setText(date);
    }

    protected void setUpdate(boolean b){
        this.update = b;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        urgencyChoice.getItems().addAll("Not Urgent","Slightly Urgent","Urgent","Very Urgent");
        urgencyChoice.setValue("Not Urgent");
        completionChoice.getItems().addAll("Completed","Not Completed");
        completionChoice.setValue("Not Completed");
    }
}
