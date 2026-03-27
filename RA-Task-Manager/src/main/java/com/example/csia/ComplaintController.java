package com.example.csia;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ComplaintController implements Initializable {

    @FXML
    private TableView<Complaint> complaintTable;
    @FXML
    private TableColumn<Complaint,String> studentID;
    @FXML
    private TableColumn<Complaint,String> facilityID;
    @FXML
    private TableColumn<Complaint,Integer> roomID;
    @FXML
    private TableColumn<Complaint,String> description;
    @FXML
    private TableColumn<Complaint,String> urgency;
    @FXML
    private TableColumn<Complaint,String> date;
    @FXML
    private TableColumn<Complaint,String> completion;

    private String query = null;
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    ObservableList<Complaint> complaintList = FXCollections.observableArrayList();

    @FXML
    protected void onStudentsClick(ActionEvent event) throws IOException {
        DBUtils.changeScene(event, "Students.fxml");
    }
    @FXML
    protected void onFacilitiesClick(ActionEvent event) throws IOException{
        DBUtils.changeScene(event, "Facilities.fxml");
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
    protected void onAddComplaintClick(ActionEvent event) throws IOException {
        DBUtils.newWindow(event,"AddComplaint.fxml","Add Complaint");
    }
    @FXML
    protected void onUpdateComplaintClick() throws IOException{
        Complaint tempComplaint = complaintTable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AddComplaint.fxml"));
        try{
            loader.load();
        } catch (Exception e){
            e.printStackTrace();
        }
        AddComplaintController addComplaintController = loader.getController();
        addComplaintController.setUpdate(true);
        addComplaintController.setTextField(tempComplaint.getStudentID(),tempComplaint.getFacilityID(),tempComplaint.getRoomID(),tempComplaint.getDescription(),tempComplaint.getUrgency(), tempComplaint.getCompletion(),tempComplaint.getDate());
        addComplaintController.setComplaint(tempComplaint);
        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.setTitle("Update Complaint");
        stage.show();
    }
    @FXML
    protected void onDeleteComplaintClick() throws IOException{
        Complaint tempComplaint = complaintTable.getSelectionModel().getSelectedItem();
        try{
            preparedStatement = connection.prepareStatement("DELETE FROM Complaints WHERE complaintID = \"" + tempComplaint.getComplaintID() + "\"");
            preparedStatement.execute();
            refreshTable();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void refreshTable(){
        try{
            complaintList.clear();

            query = "SELECT * FROM Complaints";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                complaintList.add(new Complaint(
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

            complaintTable.setItems(complaintList);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection = DBUtils.getConnection();
        refreshTable();

        studentID.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        facilityID.setCellValueFactory(new PropertyValueFactory<>("facilityID"));
        roomID.setCellValueFactory(new PropertyValueFactory<>("roomID"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        urgency.setCellValueFactory(new PropertyValueFactory<>("urgency"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        completion.setCellValueFactory(new PropertyValueFactory<>("completion"));

        urgency.setCellFactory(new Callback<TableColumn<Complaint, String>, TableCell<Complaint, String>>() {
            public TableCell call(TableColumn<Complaint, String> param){
                return new TableCell<Complaint, String>() {
                    @Override
                    protected void updateItem(String s, boolean b) {
                        super.updateItem(s, b);
                        if (this.isEmpty()){
                            this.setTextFill(Color.BLUE);
                        } else {
                            if (s == "Not Urgent"){
                                this.setTextFill(Color.GREEN);
                            } else {
                                if (s == "Slightly Urgent"){
                                    this.setTextFill(Color.GOLD);
                                } else {
                                    if (s == "Urgent"){
                                        this.setTextFill(Color.DARKORANGE);
                                    } else {
                                        if (s == "Very Urgent"){
                                            this.setTextFill(Color.RED);
                                        }
                                    }
                                }
                            }
                        }
                        this.setText(s);
                    }
                };
            }
        });
    }
}
