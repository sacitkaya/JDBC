package DB_Testing.mariaDBTesting.tests;

import org.apache.commons.codec.binary.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import static DB_Testing.mariaDBTesting.utils.Constants.PROCEDURE1;
import static DB_Testing.mariaDBTesting.utils.DBUtil.*;
import static DB_Testing.mariaDBTesting.utils.ObjectUtil.*;
import static DB_Testing.mariaDBTesting.utils.PropertiesReadWrite.getProperty;

public class Testing_StoredProcedures
{

    @BeforeClass
    public void setConnection()
    {
        connectToMariaDB(getProperty("NATION_DB_SERVER_URL"),getProperty("USERNAME"),getProperty("PASSWORD"));
    }

    @Test(priority = 1)
    public void checkIfTheExpectedProcedureExist()
    {

        //STORED PROCEDURES
        String procedureName=getAnAttributeOfStoredProcedures("SHOW PROCEDURE STATUS WHERE name='"+PROCEDURE1+"'", "Name");
        Assert.assertEquals(procedureName,PROCEDURE1);
    }


    @Test(priority = 2)
    public void checkIfTheProcedureBehavesAsExpected()
    {

        try {
            String regionName = "Middle East";
            callableStatement=connection.prepareCall("{CALL getAllCountryNameInGivenRegion(?)}");
            callableStatement.setString(1, regionName);

            resultSet1=callableStatement.executeQuery();

            resultSet2=statement.executeQuery("SELECT c.name FROM countries c, regions r WHERE c.region_id = r.region_id AND r.name='Middle East';");
            compareResultSets(resultSet1,resultSet2);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


    @Test(priority = 3)
    public void checkIfTheProcedureBehavesAsExpected2()
    {

        try {
            String regionName = "Middle East";
            callableStatement=connection.prepareCall("{CALL getCountryAmountInARegion(?,?)}");
            callableStatement.setString(1, regionName);
            callableStatement.registerOutParameter(2, Types.INTEGER);

            resultSet1=callableStatement.executeQuery();

            int cAmount =callableStatement.getInt(2);
            System.out.println(cAmount);

            resultSet2=statement.executeQuery("SELECT count(*) as cAmount FROM countries c, regions r WHERE c.region_id = r.region_id AND r.name='"+regionName+"';");
            resultSet2.next();

            int cAmount2=resultSet2.getInt("cAmount");
            Assert.assertTrue(cAmount==cAmount2);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

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
