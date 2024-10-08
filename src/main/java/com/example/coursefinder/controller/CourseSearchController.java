package com.example.coursefinder.controller;

import com.example.coursefinder.DB.DataBaseConnection;
import com.example.coursefinder.model.CourseSearchModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CourseSearchController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(CourseSearchController.class.getName());
    @FXML
    public TextField tfID;
    @FXML
    public TextField tfTitle;
    @FXML
    public TextField tfAddress;
    @FXML
    public TextField tfPrice;
    @FXML
    public TextField tfEmail;
    @FXML
    public Button btnInsert;
    @FXML
    public Button btnUpdate;
    @FXML
    public Button btnDelete;
    @FXML
    public Button btnDeleteAll;

    @FXML
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
        courseSearchModelObservableList.addAll(getAllCourses());
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

    private List<CourseSearchModel> getAllCourses() {

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


    private static final String deleteQuery = "delete from courses where id=?";

    public boolean deleteCourse() {
        try (Connection connection = DataBaseConnection.getDBConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            preparedStatement.setInt(1, Integer.parseInt(tfID.getText()));
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static final String deleteAllQuery = "truncate table courses";

    public boolean deleteAllCourses() {
        try (Connection connection = DataBaseConnection.getDBConnection();
             Statement statement = connection.createStatement()) {
            int affectedRows = statement.executeUpdate(deleteAllQuery);
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private static final String updateQuery = "update courses set title=?,address=?,price=?,email=?,description=?  where id=?";

    private boolean updateCourse() {

        try (Connection connection = DataBaseConnection.getDBConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            int count = 0;
            preparedStatement.setString(++count, tfTitle.getText());
            preparedStatement.setString(++count, tfAddress.getText());
            preparedStatement.setInt(++count, Integer.parseInt(tfPrice.getText()));
            preparedStatement.setString(++count, tfEmail.getText());
            preparedStatement.setString(++count, tfDescription.getText());
            preparedStatement.setInt(++count, Integer.parseInt(tfID.getText()));
        return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static final String insertQuery =
            "INSERT INTO courses (title, address, price, email, description ) VALUES (?, ?, ?, ?, ?)";

    private boolean insertRecord() {
        try (Connection connection = DataBaseConnection.getDBConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            int count = 0;
            preparedStatement.setString(++count, tfTitle.getText());
            preparedStatement.setString(++count, tfAddress.getText());
            preparedStatement.setInt(++count, Integer.parseInt(tfPrice.getText()));
            preparedStatement.setString(++count, tfEmail.getText());
            preparedStatement.setString(++count, tfDescription.getText());
            return preparedStatement.executeUpdate() > 0;


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void handleButtonAction(ActionEvent ae) {
        if (ae.getSource() == btnInsert) {
            boolean added = insertRecord();
            if (added) {
                reloadAll();
            } else {
                System.out.println("Error in insert");
            }
        } else if (ae.getSource() == btnUpdate) {
            boolean updated = updateCourse();
            if (updated){
                reloadAll();
            }
            else {
                System.out.println("Error in update method");
            }
        } else if (ae.getSource() == btnDelete) {
            boolean isDeleted = deleteCourse();
            if (isDeleted) {
                reloadAll();
            } else {
                System.out.println("Error in Button delete by id");
            }
        } else if (ae.getSource() == btnDeleteAll) {
            deleteAllCourses();
            reloadAll();
        }
    }
}