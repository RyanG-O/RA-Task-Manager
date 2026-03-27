package com.example.csia;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddStudentController {
    @FXML
    private TextField studentIDText;
    @FXML
    private TextField roomIDText;
    @FXML
    private TextField firstNameText;
    @FXML
    private TextField lastNameText;
    @FXML
    private TextField majorText;
    @FXML
    private TextField emailText;
    @FXML
    private TextField phoneNumberText;

    private String query = null;
    private Connection connection = null;
    private ResultSet resultSet = null;
    private PreparedStatement preparedStatement = null;
    private boolean update = false;
    Student student;

    @FXML
    protected void onSaveClicked(){
        connection = DBUtils.getConnection();
        String studentID = studentIDText.getText().trim();
        String roomID = roomIDText.getText().trim();
        String firstName = firstNameText.getText().trim();
        String lastName = lastNameText.getText().trim();
        String major = majorText.getText().trim();
        String email = emailText.getText().trim();
        String phoneNumber = phoneNumberText.getText().trim();
        if (studentID.isEmpty()||roomID.isEmpty()||firstName.isEmpty()||lastName.isEmpty()||major.isEmpty()||email.isEmpty()||phoneNumber.isEmpty()){
            DBUtils.errorAlert("Blank Text Fields");
        } else {
            try {
                Double.parseDouble(phoneNumber);
                if (phoneNumber.length() == 10){
                    if (email.contains("@")){
                        if (update == true){
                            getQuery();
                            insert();
                            clear();
                        } else{
                            if (checkStudentID(studentID) == false){
                                getQuery();
                                insert();
                                clear();
                            } else {
                                DBUtils.errorAlert("Student ID already exists");
                            }
                        }
                    } else{
                        DBUtils.errorAlert("Email is not in the right format");
                    }
                } else {
                    DBUtils.errorAlert("Phone number is not 10 digits");
                }
            } catch (NumberFormatException e) {
                DBUtils.errorAlert("Phone number must be an integer");
            }
        }
    }
    private void getQuery(){
        if (update == false){
            query = "INSERT INTO Students (studentID, roomID, firstName, lastName, major, email, phoneNumber) VALUES (?,?,?,?,?,?,?)";
        } else {
            query = "UPDATE Students SET "
                    + "studentID = ?, "
                    + "roomID = ?, "
                    + "firstName = ?, "
                    + "lastName = ?, "
                    + "major = ?, "
                    + "email = ?, "
                    + "phoneNumber = ? WHERE studentID = \"" + student.getStudentID() + "\"";
        }
    }
    private void insert(){
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,studentIDText.getText());
            preparedStatement.setString(2,roomIDText.getText());
            preparedStatement.setString(3,firstNameText.getText());
            preparedStatement.setString(4,lastNameText.getText());
            preparedStatement.setString(5,majorText.getText());
            preparedStatement.setString(6,emailText.getText());
            preparedStatement.setString(7,phoneNumberText.getText());
            preparedStatement.execute();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private void clear(){
        studentIDText.clear();
        roomIDText.clear();
        firstNameText.clear();
        lastNameText.clear();
        majorText.clear();
        emailText.clear();
        phoneNumberText.clear();
    }

    protected void setTextField(String studentID, String roomID, String firstName, String lastName, String major, String email, String phoneNumber){
        studentIDText.setText(studentID);
        roomIDText.setText(roomID);
        firstNameText.setText(firstName);
        lastNameText.setText(lastName);
        majorText.setText(major);
        emailText.setText(email);
        phoneNumberText.setText(phoneNumber);
    }
    protected void setUpdate(boolean b){
        this.update = b;
    }

    protected void setStudent(Student student){
        this.student = student;
    }

    protected boolean checkStudentID(String studentID){
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
}
