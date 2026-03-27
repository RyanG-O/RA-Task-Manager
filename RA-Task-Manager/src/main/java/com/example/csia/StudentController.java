package com.example.csia;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class StudentController implements Initializable {

    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student,String> studentID;
    @FXML
    private TableColumn<Student,String> roomID;
    @FXML
    private TableColumn<Student,String> firstName;
    @FXML
    private TableColumn<Student,String> lastName;
    @FXML
    private TableColumn<Student,String> major;
    @FXML
    private TableColumn<Student,String> email;
    @FXML
    private TableColumn<Student,Integer> phoneNumber;
    @FXML
    private TextField searchText;

    private String query = null;
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    ObservableList<Student> studentList = FXCollections.observableArrayList();

    @FXML
    protected void onFacilitiesClick(ActionEvent event) throws IOException {
        DBUtils.changeScene(event, "Facilities.fxml");
    }
    @FXML
    protected void onComplaintsClick(ActionEvent event) throws IOException{
        DBUtils.changeScene(event, "Complaints.fxml");
    }
    @FXML
    protected void onHomeClick(ActionEvent event) throws IOException{
        DBUtils.changeScene(event, "Home.fxml");
    }
    @FXML
    protected void onDisputesClick(ActionEvent event) throws IOException{
        DBUtils.changeScene(event, "Disputes.fxml");
    }
    @FXML
    protected void onAddStudentClick(ActionEvent event) throws IOException{
        DBUtils.newWindow(event,"AddStudent.fxml","Add Student");
    }

    //opens a new window which allows user to update selected student
    @FXML
    protected void onUpdateStudentClick() throws IOException{
        Student tempStudent = studentTable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AddStudent.fxml"));
        try{
            loader.load();
        } catch (Exception e){
            e.printStackTrace();
        }
        AddStudentController addStudentController = loader.getController();
        addStudentController.setUpdate(true);
        addStudentController.setTextField(tempStudent.getStudentID(),tempStudent.getRoomID(),tempStudent.getFirstName(),tempStudent.getLastName(),tempStudent.getMajor(),tempStudent.getEmail(),Long.toString(tempStudent.getPhoneNumber()));
        addStudentController.setStudent(tempStudent);
        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.setTitle("Update Student");
        stage.show();
    }

    //deletes a student (aka record) that is selected
    @FXML
    protected void onDeleteStudentClick(){
        Student tempStudent = studentTable.getSelectionModel().getSelectedItem();
        try{
            query = "DELETE FROM Students WHERE StudentID = \"" + tempStudent.getStudentID() + "\"";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
            refreshTable();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //reloads all information from the Students datatable into the program's display
    @FXML
    protected void refreshTable(){
        try{
            studentList.clear();

            query = "SELECT * FROM Students";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                studentList.add(new Student(
                        resultSet.getString("studentID"),
                        resultSet.getString("roomID"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("major"),
                        resultSet.getString("email"),
                        resultSet.getLong("phoneNumber")
                ));
            }

            bubbleSort(studentList.size(),studentList);

            studentTable.setItems(studentList);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    protected void onSearchStudentClick(){
        try{
            studentList.clear();

            String tempStudentID = searchText.getText();
            query = "SELECT * FROM Students WHERE studentID = \"" + tempStudentID + "\"";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                studentList.add(new Student(
                        resultSet.getString("studentID"),
                        resultSet.getString("roomID"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("major"),
                        resultSet.getString("email"),
                        resultSet.getInt("phoneNumber")
                ));
            }

            studentTable.setItems(studentList);

            searchText.clear();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void bubbleSort(int arraySize, ObservableList<Student> list){
        if (arraySize == 1) {
            return;
        }
        for (int i = 0; i < arraySize-1;i++){
            if (list.get(i).getLastName().compareTo(list.get(i+1).getLastName()) > 0){
                Student tempStudent = list.get(i);
                list.set(i,list.get(i+1));
                list.set(i+1,tempStudent);
            }
        }
        bubbleSort(arraySize-1, list);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection = DBUtils.getConnection();
        refreshTable();

        studentID.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        roomID.setCellValueFactory(new PropertyValueFactory<>("roomID"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        major.setCellValueFactory(new PropertyValueFactory<>("major"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
    }
}
