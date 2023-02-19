package DB_Testing.mariaDBTesting.utils;

import org.apache.commons.codec.binary.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static DB_Testing.mariaDBTesting.utils.ObjectUtil.*;

public class DBUtil
{
    public static void connectToMariaDB(String url,String username,String password)
    {
        try  {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();

            System.out.println("Connected to MariaDB database->"+url);


        } catch (SQLException e) {
            System.out.println("Failed to connect to MariaDB database");
            e.printStackTrace();
        }

    }


    public static List<String> getAllTableNames()
    {
        List<String> tableNames = new ArrayList<>();
        String catalog = null;//anlik islem yaptigimiz db'yi temsil eder-> nation
        try {
            catalog = connection.getCatalog();
            databaseMetaData = connection.getMetaData();
            String[] types = {"TABLE"};
            resultSet = databaseMetaData.getTables(catalog,null,  null, types);

            while (resultSet.next()) {
                tableNames.add(resultSet.getString("TABLE_NAME"));
            }

            return tableNames;

        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
            return null;
        }

    }

    public static List<String> getAMetaDataOfTheTable(String tableName,String data)
    {
        List<String> columnNames = new ArrayList<>();

        try {
            resultSet = statement.executeQuery("SELECT "+data+" from information_schema.columns where table_name='"+tableName+"';");
            while (resultSet.next()) {
                String metaData = resultSet.getString(data);
                columnNames.add(metaData);

              //  System.out.printf("%s: %s  %n",data,metaData);
            }
            return columnNames;


        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
            return null;
        }

    }


    public static void sendSQLQueryToDB()
    {
        try {
            resultSet = statement.executeQuery("SELECT * FROM countries;");

            while (resultSet.next()) {
                int country_id = resultSet.getInt("country_id");
                String name = resultSet.getString("name");
                int region_id = resultSet.getInt("region_id");
                System.out.printf("country_id: %d - name: %s - region_id: %d %n",country_id,name,region_id);
            }

        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }

    }

    public static void executeQueryAndPrint(String query) {
        try {
            resultSet = statement.executeQuery(query);

            resultSetMetaData = resultSet.getMetaData();

            int columnCount = resultSetMetaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = resultSetMetaData.getColumnName(i);
                System.out.print(columnName + "\t");
            }
            System.out.println();

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String value = resultSet.getString(i);
                    System.out.print(value + "\t");
                }
                System.out.println();
            }

        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
        }
    }


    public static void printQueryResultsInTableFormat(String query) {
        try {
            resultSet = statement.executeQuery(query);

            resultSetMetaData= resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

            List<String> headers = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                headers.add(resultSetMetaData.getColumnName(i));
            }

            printRow(headers);

            while (resultSet.next()) {
                List<String> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(resultSet.getString(i));
                }
                printRow(row);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
        }
    }

    private static void printRow(List<String> row) {
        System.out.print("| ");
        for (String value : row) {
            System.out.printf("%-30s | ", value);
        }
        System.out.println();
    }


    public static String getAnAttributeOfStoredProcedures(String query,String attribute)
    {
        try {
            resultSet = statement.executeQuery(query);
            resultSet.next();
            return resultSet.getString(attribute);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static boolean compareResultSets(ResultSet resultSet1, ResultSet resultSet2)
    {
        try {
            while (resultSet1.next())
            {
                resultSet2.next();
                int count=resultSet1.getMetaData().getColumnCount();

                for(int i=1;i<=count;i++)
                {
                    String value = resultSet1.getString(i);
                    System.out.println(value);

                    if(!StringUtils.equals(value,resultSet2.getString(i)))
                        return false;

                }
            }
            return true;
        }catch (Exception e)
        {
            return false;
        }

    }



    public static void executeUpdate(String query)  {
        try {
            statement.executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public static List<HashMap<String, String>> getDbResultAsList(String query) {
        List<HashMap<String, String>> resultList = new ArrayList<>();

        try {
             ResultSet rs = statement.executeQuery(query);
             ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            while (rs.next()) {
                HashMap<String, String> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rsmd.getColumnName(i);
                    String columnValue = rs.getString(i);
                    row.put(columnName, columnValue);
                }
                resultList.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }




}
