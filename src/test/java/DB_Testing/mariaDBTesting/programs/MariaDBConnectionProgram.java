package DB_Testing.mariaDBTesting.programs;

import DB_Testing.mariaDBTesting.utils.ObjectUtil;
import DB_Testing.mariaDBTesting.utils.ObjectUtil.*;

import java.sql.*;
import java.util.List;

import static DB_Testing.mariaDBTesting.utils.Constants.PROCEDURE1;
import static DB_Testing.mariaDBTesting.utils.DBUtil.*;
import static DB_Testing.mariaDBTesting.utils.PropertiesReadWrite.getProperty;

public class MariaDBConnectionProgram
{

    //CRUD + POJO + EXCEL (QUERIES + EXPECTED DATA)
    public static void main(String[] args) throws SQLException {

        connectToMariaDB(getProperty("NATION_DB_SERVER_URL"),getProperty("USERNAME"),getProperty("PASSWORD"));

        //SCHEMAS OF DATATABLES

        //getAllTableNames().stream().forEach(System.out::println);

        List<String> columnNames= getAMetaDataOfTheTable("continents","column_name");
        System.out.println(columnNames);


        //sendSQLQueryToDB();

        executeQueryAndPrint("SELECT * FROM countries;");

        //printQueryResultsInTableFormat("SELECT * FROM regions;");




    }



}

//"C:\Users\Emre Duman\Downloads\fir-dbtesting-9e136-firebase-adminsdk-evahi-c695e74175.json"