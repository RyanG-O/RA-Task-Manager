package com.example.csia;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class HomeController implements Initializable{
    ObservableList<Reminder> reminderList = FXCollections.observableArrayList();
    @FXML
    public TextField reminderField;
    @FXML
    public TableView<Reminder> reminderTable;
    @FXML
    public TableColumn<Reminder,String> reminderDescription;

    @FXML
    public TableView<Complaint> urgentComplaints;
    @FXML
    public TableColumn<Complaint,String> complaintDescription;
    @FXML
    public TableView<Dispute> urgentDisputes;
    @FXML
    public TableColumn<Dispute,String> disputeDescription;
    @FXML
    public TableView<Facility> malfunctioningFacilities;
    @FXML
    public TableColumn<Facility,String> facilityDescription;

    private ObservableList<Complaint> urgentComplaintList = FXCollections.observableArrayList();
    private ObservableList<Dispute> urgentDisputeList = FXCollections.observableArrayList();
    private ObservableList<Facility> urgentFacilityList = FXCollections.observableArrayList();

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    @FXML
    protected void onStudentsClick(ActionEvent event) throws IOException {
        DBUtils.changeScene(event, "Students.fxml");
    }

    @FXML
    protected void onFacilitiesClick(ActionEvent event) throws IOException{
        DBUtils.changeScene(event, "Facilities.fxml");
    }

    @FXML
    protected void onComplaintsClick(ActionEvent event) throws IOException{
        DBUtils.changeScene(event, "Complaints.fxml");
    }
    @FXML
    protected void onDisputesClick(ActionEvent event) throws IOException{
        DBUtils.changeScene(event, "Disputes.fxml");
    }

    @FXML
    protected void onAddReminderClick() throws IOException{
        String tempReminder = reminderField.getText().trim();
        if (tempReminder.isEmpty()){
            DBUtils.errorAlert("Blank Text Field");
        } else{
            try{
                preparedStatement = connection.prepareStatement("INSERT INTO Reminders(Description) VALUES (\"" + tempReminder + "\")");
                preparedStatement.execute();
                loadReminders();
                reminderField.clear();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    @FXML
    protected void onDeleteReminderClick() throws IOException{
        Reminder reminderSelected = reminderTable.getSelectionModel().getSelectedItem();

        try{
            preparedStatement = connection.prepareStatement("DELETE FROM Reminders WHERE description = \"" + reminderSelected.getDescription() + "\"");
            preparedStatement.execute();
            loadReminders();
        } catch (Exception e){
            e.printStackTrace();
        }

        loadReminders();
    }
    private void loadData(){
        try{
            preparedStatement = connection.prepareStatement("SELECT * FROM Complaints WHERE urgency > 2");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                urgentComplaintList.add(new Complaint(
                        resultSet.getString("complaintID"),
                        resultSet.getString("studentID"),
                        resultSet.getString("facilityID"),
                        resultSet.getString("roomID"),
                        resultSet.getString("description"),
                        resultSet.getInt("urgency"),
                        resultSet.getInt("completion"),
                        resultSet.getString("date")
                ));
            }

            urgentComplaints.setItems(urgentComplaintList);

            preparedStatement = connection.prepareStatement("SELECT * FROM Disputes WHERE urgency > 2");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                urgentDisputeList.add(new Dispute(
                        resultSet.getString("disputeID"),
                        resultSet.getString("studentID1"),
                        resultSet.getString("studentID2"),
                        resultSet.getString("description"),
                        resultSet.getInt("urgency"),
                        resultSet.getInt("completion"),
                        resultSet.getString("date")
                ));
            }

            urgentDisputes.setItems(urgentDisputeList);

            preparedStatement = connection.prepareStatement("SELECT * FROM Facilities WHERE maintenanceStatus > 2");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                urgentFacilityList.add(new Facility(
                        resultSet.getString("facilityID"),
                        resultSet.getString("roomID"),
                        resultSet.getInt("floor"),
                        resultSet.getInt("maintenanceStatus"),
                        resultSet.getString("type")
                ));
            }

            malfunctioningFacilities.setItems(urgentFacilityList);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadReminders(){
        try{
            reminderList.clear();
            connection = DBUtils.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM Reminders");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                reminderList.add(new Reminder(resultSet.getString("description")));
            }

            reminderTable.setItems(reminderList);
        } catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reminderDescription.setCellValueFactory(new PropertyValueFactory<Reminder,String>("Description"));
        disputeDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        complaintDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        facilityDescription.setCellValueFactory(new PropertyValueFactory<>("facilityID"));
        loadReminders();
        loadData();
    }
}