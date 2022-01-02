package com.example.coursefinder;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseConnection {
  public static Connection getDBConnection(){
      String databaseName = "postgres";
      String databaseUser = "newuser";
      String databasePassword = "newuser1234";
      String url = "jdbc:postgresql://localhost:5433/"+databaseName;
      Connection databaseLink = null;
      try {
          Class.forName("org.postgresql.Driver");
          databaseLink = DriverManager.getConnection(url,databaseUser,databasePassword);
      }catch (Exception e){
          e.printStackTrace();
      }
      return databaseLink;
  }
}
