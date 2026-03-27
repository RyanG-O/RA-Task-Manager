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

public class DisputeController implements Initializable {
    @FXML
    private TableView<Dispute> disputeTable;
    @FXML
    private TableColumn<Dispute,String> studentID1;
    @FXML
    private TableColumn<Dispute,String> studentID2;
    @FXML
    private TableColumn<Dispute,String> description;
    @FXML
    private TableColumn<Dispute,String> urgency;
    @FXML
    private TableColumn<Dispute,String> completion;
    @FXML
    private TableColumn<Dispute,String> date;

    private String query = null;
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    ObservableList<Dispute> disputeList = FXCollections.observableArrayList();

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
    protected void onComplaintsClick(ActionEvent event) throws IOException{
        DBUtils.changeScene(event, "Complaints.fxml");
    }
    @FXML
    protected void onAddDisputeClick(ActionEvent event) throws IOException {
        DBUtils.newWindow(event,"AddDispute.fxml","Add Dispute");
    }
    @FXML
    protected void onUpdateDisputeClick() throws IOException{
        Dispute tempDispute = disputeTable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AddDispute.fxml"));
        try{
            loader.load();
        } catch (Exception e){
            e.printStackTrace();
        }
        AddDisputeController addDisputeController = loader.getController();
        addDisputeController.setUpdate(true);
        addDisputeController.setTextField(tempDispute.getStudentID1(),tempDispute.getStudentID2(),tempDispute.getDescription(),tempDispute.getUrgency(), tempDispute.getCompletion(), tempDispute.getDate());
        addDisputeController.setDispute(tempDispute);
        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.setTitle("Update Dispute");
        stage.show();
    }
    @FXML
    protected void onDeleteDisputeClick() throws IOException{
        Dispute tempDispute = disputeTable.getSelectionModel().getSelectedItem();
        try{
            preparedStatement = connection.prepareStatement("DELETE FROM Disputes WHERE disputeID = \"" + tempDispute.getDisputeID() + "\"");
            preparedStatement.execute();
            refreshTable();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void refreshTable(){
        try{
            disputeList.clear();

            query = "SELECT * FROM Disputes";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                disputeList.add(new Dispute(
                        resultSet.getString("disputeID"),
                        resultSet.getString("studentID1"),
                        resultSet.getString("studentID2"),
                        resultSet.getString("description"),
                        resultSet.getInt("urgency"),
                        resultSet.getInt("completion"),
                        resultSet.getString("date")
                ));
            }

            disputeTable.setItems(disputeList);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection = DBUtils.getConnection();
        refreshTable();

        studentID1.setCellValueFactory(new PropertyValueFactory<>("studentID1"));
        studentID2.setCellValueFactory(new PropertyValueFactory<>("studentID2"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        urgency.setCellValueFactory(new PropertyValueFactory<>("urgency"));
        completion.setCellValueFactory(new PropertyValueFactory<>("completion"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));

        urgency.setCellFactory(new Callback<TableColumn<Dispute, String>, TableCell<Dispute, String>>() {
            public TableCell call(TableColumn<Dispute, String> param){
                return new TableCell<Dispute, String>() {
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
