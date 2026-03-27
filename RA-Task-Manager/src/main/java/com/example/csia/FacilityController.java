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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.xml.transform.Result;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class FacilityController implements Initializable {
    @FXML
    private TableView<Facility> facilityTable;
    @FXML
    private TableColumn<Facility,String> facilityID;
    @FXML
    private TableColumn<Facility,String> roomID;
    @FXML
    private TableColumn<Facility,Integer> floor;
    @FXML
    private TableColumn<Facility,String> maintenanceStatus;
    @FXML
    private TableColumn<Facility,String> type;
    @FXML
    private TextField searchText;

    private String query = null;
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    ObservableList<Facility> facilityList = FXCollections.observableArrayList();

    @FXML
    protected void onStudentsClick(ActionEvent event) throws IOException {
        DBUtils.changeScene(event, "Students.fxml");
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
    protected void onAddFacilityClick(ActionEvent event) throws IOException{
        DBUtils.newWindow(event,"AddFacility.fxml","Add Facility");
    }
    @FXML
    protected void onUpdateFacilityClick(ActionEvent event) throws IOException{
        Facility tempFacility = facilityTable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AddFacility.fxml"));
        try{
            loader.load();
        } catch (Exception e){
            e.printStackTrace();
        }
        AddFacilityController addFacilityController = loader.getController();
        addFacilityController.setUpdate(true);
        addFacilityController.setTextField(tempFacility.getFacilityID(), tempFacility.getRoomID(), tempFacility.getFloor(), tempFacility.getMaintenanceStatus(), tempFacility.getType());
        addFacilityController.setFacility(tempFacility);
        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.setTitle("Update Facility");
        stage.show();
    }
    @FXML
    protected void onDeleteFacilityClick(ActionEvent event) throws IOException{
        Facility tempFacility = facilityTable.getSelectionModel().getSelectedItem();
        try{
            preparedStatement = connection.prepareStatement("DELETE FROM Facilities WHERE facilityID = \"" + tempFacility.getFacilityID() + "\"");
            preparedStatement.execute();
            refreshTable();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void onSearchFacilityClick(){
        try{
            String tempFacilityID = searchText.getText();

            facilityList.clear();

            query = "SELECT * FROM Facilities WHERE facilityID = \"" + tempFacilityID + "\"";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                facilityList.add(new Facility(
                        resultSet.getString("facilityID"),
                        resultSet.getString("roomID"),
                        resultSet.getInt("floor"),
                        resultSet.getInt("maintenanceStatus"),
                        resultSet.getString("type")
                ));
            }

            facilityTable.setItems(facilityList);

            searchText.clear();

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void refreshTable(){
        try{
            facilityList.clear();

            query = "SELECT * FROM Facilities";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                facilityList.add(new Facility(
                        resultSet.getString("facilityID"),
                        resultSet.getString("roomID"),
                        resultSet.getInt("floor"),
                        resultSet.getInt("maintenanceStatus"),
                        resultSet.getString("type")
                ));
            }

            facilityTable.setItems(facilityList);

        } catch(Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection = DBUtils.getConnection();
        refreshTable();

        facilityID.setCellValueFactory(new PropertyValueFactory<>("facilityID"));
        roomID.setCellValueFactory(new PropertyValueFactory<>("roomID"));
        floor.setCellValueFactory(new PropertyValueFactory<>("floor"));
        maintenanceStatus.setCellValueFactory(new PropertyValueFactory<>("maintenanceStatus"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));

        maintenanceStatus.setCellFactory(new Callback<TableColumn<Facility, String>, TableCell<Facility, String>>() {
            public TableCell call(TableColumn<Facility, String> param){
                return new TableCell<Facility, String>() {
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
