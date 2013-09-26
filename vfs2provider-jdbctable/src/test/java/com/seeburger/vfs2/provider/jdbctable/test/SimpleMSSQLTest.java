package com.seeburger.vfs2.provider.jdbctable.test;


import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sourceforge.jtds.jdbc.Driver;
import net.sourceforge.jtds.jdbcx.JtdsDataSource;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.googlecode.flyway.core.Flyway;
import com.seeburger.vfs2.provider.jdbctable.JdbcDialectBase;
import com.seeburger.vfs2.provider.jdbctable.JdbcDialectMSSQL;


public class SimpleMSSQLTest extends SimpleTestsBase
{
    static boolean skip = false;

    @BeforeClass
    public static void setupDatabase() throws SQLException
    {
        System.out.println("Starting MSSQL database");
        JtdsDataSource ds = new JtdsDataSource();
        ds.setUser("VFSTEST");
        ds.setPassword("secret");
        ds.setServerName("127.0.0.1"); ds.setPortNumber(49762);
        ds.setDatabaseName("VFSTEST");
        // ds.setInstance("SQLEXPRESS"); - if you set this, it will always look up the port from SQLBrowser
        ds.setAutoCommit(false);
        ds.setMacAddress("010203040506"); // 20x speedup
        ds.setServerType(Driver.SQLSERVER);

        Flyway flyway = new Flyway();
        flyway.setDataSource(ds);
        flyway.setLocations("db/migration/mssql");
        flyway.setValidateOnMigrate(true);
        flyway.setCleanOnValidationError(true);
        flyway.migrate();

        Connection c = ds.getConnection();
        DatabaseMetaData md = c.getMetaData();
        ResultSet rs = md.getTables(null, null, null, new String[] { "TABLE" });
        while(rs.next())
        {
            System.out.println(rs.getString(1)+"|"+rs.getString(2)+"|"+rs.getString(3)+"|"+rs.getString(4));
        }
        rs.close(); c.close();

        SimpleTestsBase.dataSource = ds;

        try
        {
            SimpleTestsBase.dialect = JdbcDialectBase.getDialect(dataSource);
        }
        catch (SQLException e)
        {
            fail("Exception while determining database dialect:" + e.getMessage());
        }
    }

    @AfterClass
    public static void destroyDatabase()
    {
        SimpleTestsBase.dataSource = null;
    }

    void verifyDatabase()
    {
/*        if (skip)
            return;

        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try
        {
            c = dataSource.getConnection();
            ps = c.prepareStatement("SELECT * FROM INFORMATION_SCHEMA.SESSIONS");
            rs = ps.executeQuery();
            int count = 0;
            while(rs.next())
            {
                count++;
                //System.out.println(" " + count + "  " + rs.getString(1)+"|"+rs.getString(2)+"|"+rs.getString(3)+"|"+rs.getString(4));
            }

            if (count > 1)
            {
                skip = true;
                fail("After this test there are " + count + " sessions active");
            }
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            safeClose(rs, ps, c);
        }
*/
    }

/*    private void safeClose(ResultSet rs, PreparedStatement ps, Connection c)
    {
        try { rs.close(); } catch (Exception ignored) { }
        try { ps.close(); } catch (Exception ignored) { }
        try { c.close(); } catch (Exception ignored) { }
    }*/
}



