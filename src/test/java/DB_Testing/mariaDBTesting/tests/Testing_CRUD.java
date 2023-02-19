package DB_Testing.mariaDBTesting.tests;

import DB_Testing.mariaDBTesting.utils.ExcelUtil;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static DB_Testing.mariaDBTesting.utils.DBUtil.*;
import static DB_Testing.mariaDBTesting.utils.ExcelUtil.getExcelRow;
import static DB_Testing.mariaDBTesting.utils.ObjectUtil.connection;
import static DB_Testing.mariaDBTesting.utils.ObjectUtil.statement;
import static DB_Testing.mariaDBTesting.utils.PropertiesReadWrite.getProperty;

public class Testing_CRUD
{

    @BeforeTest
    public void setConnection() throws ClassNotFoundException {

        connectToMariaDB(getProperty("DB_SERVER_URL"),getProperty("USERNAME"),getProperty("PASSWORD"));

    }
    @BeforeClass
    public void createDB() throws SQLException, ClassNotFoundException {

        executeUpdate("CREATE DATABASE IF NOT EXISTS MY_DATABASE");
        connectToMariaDB(getProperty("MY_DATABASE_SERVER_URL"),getProperty("USERNAME"),getProperty("PASSWORD"));

    }

    @Test(priority = 1)
    public void createTable()
    {
        executeUpdate("CREATE TABLE IF NOT EXISTS student (id INT PRIMARY KEY, name VARCHAR(50), age INT)");
        executeUpdate("CREATE TABLE IF NOT EXISTS address_info (sehir VARCHAR(100), ilce VARCHAR(100), mahalle VARCHAR(100), sokak VARCHAR(100), student_id INT UNIQUE, FOREIGN KEY (student_id) REFERENCES student(id))");
    }

    @Test(priority = 2)
    public void insertNewRecord() throws SQLException {

        insertRecordIntoStudent(1, "John", 20);
        insertRecordIntoStudent(2, "Emma", 30);
        insertRecordIntoAddressInfo("Istanbul", "Besiktas", "Levent","Barbaros",1);
        insertRecordIntoAddressInfo("Istanbul", "Sariyer", "Ayazaga","cicek",2);

    }

    public void insertRecordIntoAddressInfo(Object... values)
    {
        String sqlInsertRecord = "INSERT INTO address_info (sehir, ilce, mahalle, sokak, student_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt  = null;
        try {
            pstmt = connection.prepareStatement(sqlInsertRecord);
            pstmt.setString(1, (String) values[0]);
            pstmt.setString(2, (String) values[1]);
            pstmt.setString(3, (String) values[2]);
            pstmt.setString(4, (String) values[3]);
            pstmt.setInt(5, (int) values[4]);
            pstmt.executeUpdate();
            System.out.println("Record inserted Into AddressInfo successfully.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    public void insertRecordIntoStudent(Object... values)
    {

        String sql = "INSERT INTO student (id, name, age) VALUES (?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, (int)values[0]);
            statement.setString(2, (String) values[1]);
            statement.setInt(3, (int)values[2]);
            statement.executeUpdate();
            System.out.println("Record inserted Into Student successfully.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }


    @Test(priority = 3)
    public void readRecord()  {
        List<HashMap<String, String>> result=getDbResultAsList("select * from address_info;");
        System.out.println(result);

    }




    @Test(priority = 4)
    public void updateRecord()
    {
   //DIYELIM KI, ID'SI 1 OLAN OGRENCININ YASININ SISTEMDE YANLIS OLDUGU TESPIT EDILDI.
    int studentId = 1;
    int newAge = 25;


        try {
            //BIR SQL QUERY TASLAGI HAZIRLAYALIM
            PreparedStatement statement = connection.prepareStatement("UPDATE student SET age = ? WHERE id = ?");


            // ? ISARETLERININ OLDUGU YERLERI DOLDURALIM
            statement.setInt(1, newAge);
            statement.setInt(2, studentId);

           // ISLEM BASARILI BIR SEKILDE TAMAMLANDIYSA 1 RETURN EDER
            int rowsUpdated = statement.executeUpdate();


            //IF - ELSE KULLANIMININ KISASI -> JAVA TERNARY KONUSU
            System.out.println((rowsUpdated > 0) ? "Update successful" : "Update failed");



        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }




    }


    @Test(priority = 5)
    public void deleteRecord()  {
        // ID'si 1 OLAN OGRENCININ OKUL DEGISTIRDIGINI DUSUNELIM
        int studentId = 1;

        try {
            //PARENT  ROWU SILMEK ICIN ILK DEPENDENT OLDUGU RECORDLARI SILMEK GEREKIR
           // PreparedStatement statement = connection.prepareStatement("DELETE FROM address_info WHERE student_id = ?");
            PreparedStatement statement2 = connection.prepareStatement("DELETE FROM student WHERE id = ?");

            statement2.setInt(1, studentId);

            int rowsDeleted = statement2.executeUpdate();

            System.out.println((rowsDeleted > 0) ? "Deletion successful" : "UpdDeletionate failed");


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }



    }

    @Test(priority = 6)
    public void deleteTable()  {
        executeUpdate( "DROP TABLE IF EXISTS address_info;");



    }

    @Test(priority = 7)
    public void deleteDB()  {
        executeUpdate("DROP DATABASE IF EXISTS MY_DATABASE");


    }




    @AfterClass
    public void closeConnection()
    {
        try {
            statement.close();
            connection.close();
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }


    }

}
