package com.example.coursefinder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseSearchController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(CourseSearchController.class.getName());

    public TextField tfID;
    public TextField tfTitle;
    public TextField tfAddress;
    public TextField tfPrice;
    public TextField tfEmail;
    public Button btnInsert;
    public Button btnUpdate;
    public Button btnDelete;
    public TextField tfDescription;
    @FXML
    private TableView<CourseSearchModel> courseTableView;
    @FXML
    private TableColumn<CourseSearchModel, Integer> idTableColumn;
    @FXML
    private TableColumn<CourseSearchModel, String> titleTableColumn;
    @FXML
    private TableColumn<CourseSearchModel, String> addressTableColumn;
    @FXML
    private TableColumn<CourseSearchModel, Integer> priceTableColumn;
    @FXML
    private TableColumn<CourseSearchModel, String> emailTableColumn;
    @FXML
    private TableColumn<CourseSearchModel, String> descriptionTableColumn;
    @FXML
    private TextField keyWordTextField;

    @FXML
    private TextField id;

    @FXML
    private Label label;

    ObservableList<CourseSearchModel> courseSearchModelObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleTableColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        addressTableColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        priceTableColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        emailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        reloadAll();
        courseTableView.setItems(courseSearchModelObservableList);
    }

    private void reloadAll() {
        courseSearchModelObservableList.clear();
        courseSearchModelObservableList.addAll(getAllRecords());
        FilteredList<CourseSearchModel> filteredData = new FilteredList<>(courseSearchModelObservableList, b -> true);

        keyWordTextField.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(courseSearchModel -> {

            //Если нет значений в поиске , тогда возвращает все что есть
            if (newValue.isEmpty() || newValue.isBlank()) {
                return true;
            }
            String searchKeyword = newValue.toLowerCase();

            if (courseSearchModel.getTitle().toLowerCase().contains(searchKeyword)) {
                return true; //Означает что мы нашли значение названия курса
            } else if (courseSearchModel.getDescription().toLowerCase().contains(searchKeyword)) {
                return true;
            } else if (courseSearchModel.getAddress().toLowerCase().contains(searchKeyword)) {
                return true;
            } else // ничего не нашлось
                if (courseSearchModel.getEmail().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else return courseSearchModel.getPrice().toString().toLowerCase().contains(searchKeyword);

        }));

        SortedList<CourseSearchModel> sortedData = new SortedList<>(filteredData);

        //связываем отсортированный результат с table view
        sortedData.comparatorProperty().bind(courseTableView.comparatorProperty());

        //применяем фильтрованные данные к table view
        courseTableView.refresh();
        courseTableView.setItems(sortedData);
    }

    private static final String courseViewQuery = "select id, title,address,price,email,description from courses";

    private List<CourseSearchModel> getAllRecords() {

        List<CourseSearchModel> result = new ArrayList<>();
        try (Connection connection = DataBaseConnection.getDBConnection();
             Statement statement = connection.createStatement();
             ResultSet queryOutput = statement.executeQuery(courseViewQuery)) {
            while (queryOutput.next()) {
                Integer queryCourseID = queryOutput.getInt("id");
                String queryTitle = queryOutput.getString("title");
                String queryAddress = queryOutput.getString("address");
                int queryPrice = queryOutput.getInt("price");
                String queryEmail = queryOutput.getString("email");
                String queryDescription = queryOutput.getString("description");
                result.add(new CourseSearchModel(queryCourseID, queryTitle, queryAddress, queryPrice, queryEmail, queryDescription));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
        return result;
    }
    Connection con=null;
    ResultSet rs=null;
    PreparedStatement pstmt=null;
   private final String deleteQuery="delete from courses where id=?";

    public void deleteEmployee(ActionEvent ae){
        try (Connection connection = DataBaseConnection.getDBConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            preparedStatement.setInt(1, Integer.parseInt(id.getText()));
            rs=preparedStatement.executeQuery();
            if(rs!=null){
                label.setText("Record deleted ");
            }else{
                label.setText("please check course id");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }



    private static final String insertQuery = "INSERT INTO courses (title, address, price, email, description ) VALUES (?, ?, ?, ?, ?)";

    public void update(ActionEvent ae) throws IOException{
        Stage primaryStage= new Stage();
        Parent root =FXMLLoader.load(getClass().getResource("/application/Update.fxml"));
//			Parent root = FXMLLoader.load(getClass().getResource(arg0))
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void insertRecord() {
        try (Connection connection = DataBaseConnection.getDBConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            int count = 0;
            preparedStatement.setString(++count, tfTitle.getText());
            preparedStatement.setString(++count, tfAddress.getText());
            preparedStatement.setInt(++count, Integer.parseInt(tfPrice.getText()));
            preparedStatement.setString(++count, tfEmail.getText());
            preparedStatement.setString(++count, tfDescription.getText());
            boolean added = preparedStatement.executeUpdate() > 0;
            if (added) {
                reloadAll();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Title: " + tfTitle.getText());
    }


    public void handleButtonAction() {
        insertRecord();
    }
//    public void handleUpdateButtonAction() {
//        update();
//    }
}