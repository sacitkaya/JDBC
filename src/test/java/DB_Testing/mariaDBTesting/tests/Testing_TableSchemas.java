package DB_Testing.mariaDBTesting.tests;

import DB_Testing.mariaDBTesting.utils.ExcelUtil;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static DB_Testing.mariaDBTesting.utils.DBUtil.*;
import static DB_Testing.mariaDBTesting.utils.ExcelUtil.getExcelRow;
import static DB_Testing.mariaDBTesting.utils.ObjectUtil.*;
import static DB_Testing.mariaDBTesting.utils.PropertiesReadWrite.getProperty;

public class Testing_TableSchemas
{

    @BeforeClass
    public void setConnection()
    {
        connectToMariaDB(getProperty("NATION_DB_SERVER_URL"),getProperty("USERNAME"),getProperty("PASSWORD"));
    }

    @Test(priority = 1)
    public void checkTables()
    {
        List<String> table_Names_From_Excel = ExcelUtil.getColumnValues(getProperty("TESTDATA_EXCELPATH"), "DB_nation", "Table_Names");
        System.out.println(table_Names_From_Excel);
        System.out.println(table_Names_From_Excel.size());

        List<String> table_Names_From_DB = getAllTableNames();
        System.out.println(table_Names_From_DB);
        System.out.println(table_Names_From_DB.size());

        Assert.assertTrue(table_Names_From_Excel.equals(table_Names_From_DB));

    }

    @Test(priority = 2)
    public void checkColumnNamesOfTable() throws IOException {


        Map<String, String> rowData = getExcelRow(getProperty("TESTDATA_EXCELPATH"), "DB_nation", "countries",false);

        //Map icindeki degerleri keylerine gore sirala
        Map<String, String> sortedMap = new TreeMap<>(rowData);

        //siralanmis map'in sadece value'larini al ve bir list olustur
        List<String> rowValues = new ArrayList<>(sortedMap.values());

        //List'deki null degerleri(eger varsa) list'den cikar
        rowValues=rowValues.stream().filter(str -> str!="").collect(Collectors.toList());

        System.out.println(rowValues);


        List<String> columnNames= getAMetaDataOfTheTable("countries","column_name");
        System.out.println(columnNames);

        Assert.assertTrue(rowValues.equals(columnNames));



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
