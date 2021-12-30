package com.example.coursefinder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateController {
    @FXML
    private TextField cTitle;
    @FXML
    private TextField cAddress;
    @FXML
    private TextField cPrice;
    @FXML
    private TextField cEmail;
    @FXML
    private TextField id;
    @FXML
    private TextField cDescription;

    @FXML
    private Label lavel;

    @FXML
    private TableView<Course> table;

    @FXML
    private TableColumn<Course, Integer> cId;
    @FXML
    private TableColumn<Course, String> title;
    @FXML
    private TableColumn<Course, String> address;
    @FXML
    private TableColumn<Course, Integer> price;
    @FXML
    private TableColumn<Course, String> email;
    @FXML
    private TableColumn<Course, String> description;



    Course course;
    Connection con=null;
    ResultSet rs=null;
    PreparedStatement pstmt=null;

    public void updateCourse(ActionEvent ae){
        course=new Course();
        course.setId(Integer.parseInt(id.getText()));
        course.setTitle(cTitle.getText());
        course.setAddress(cAddress.getText());
        course.setPrice(Integer.parseInt(cPrice.getText()));
        course.setEmail(cEmail.getText());
        course.setDescription(cDescription.getText());


        try {
            con=DataBaseConnection.getDBConnection();
            String sql="update courses set title=?,address=?,price=?,email=?,description=?  where id=?";
            pstmt=con.prepareStatement(sql);
            pstmt.setString(1,course.getTitle());
            pstmt.setString(2,course.getAddress());
            pstmt.setInt(3,course.getPrice());
            pstmt.setString(4,course.getEmail());
            pstmt.setString(5, course.getDescription());
            pstmt.setInt(6, course.getId());
            rs=pstmt.executeQuery();
            if(rs.next()){
                lavel.setText("Update Sucessfully.");
                cId.setCellValueFactory(new PropertyValueFactory<Course, Integer>("CId"));
                title.setCellValueFactory(new PropertyValueFactory<Course, String>("title"));
                address.setCellValueFactory(new PropertyValueFactory<Course, String>("address"));
                price.setCellValueFactory(new PropertyValueFactory<Course, Integer>("price"));
                email.setCellValueFactory(new PropertyValueFactory<Course, String>("email"));
                email.setCellValueFactory(new PropertyValueFactory<Course, String>("description"));
                ObservableList<Course> data = FXCollections.observableArrayList(
                        new Course(course.getId(),course.getTitle(),course.getAddress(),
                                course.getPrice(),course.getEmail(),course.getDescription())
                );

                table.getItems().addAll(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }}
//    public void update(ActionEvent ae) throws IOException {
//        Stage primaryStage= new Stage();
//        Parent root = FXMLLoader.load(getClass().getResource("update.fxml"));
////			Parent root = FXMLLoader.load(getClass().getResource(arg0))
//        Scene scene = new Scene(root);
//        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//        public void handleUpdateButtonAction() {
//           update();
//        }
    }


