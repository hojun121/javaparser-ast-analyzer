package analyzer.ast;

import java.lang.*;
import java.sql.*;

public class InsertAst
{
    public static void createStagingTable(Connection con)
    {
        try
        {
            Statement stmt = con.createStatement();

            //create base table
            stmt.executeUpdate(
                    "  CREATE TABLE t " +
                            "    (c1 SMALLINT NOT NULL, " +
                            "     c2 SMALLINT NOT NULL, " +
                            "     c3 SMALLINT, " +
                            "     c4 SMALLINT)");
            System.out.println("  COMMIT");
            con.commit();

            System.out.println(
                    "\nCreating summary table d_ast \n" +
                            "  CREATE SUMMARY TABLE d_ast AS\n" +
                            "    (SELECT c1, c2, COUNT(*) AS count\n" +
                            "      FROM t\n" +
                            "      GROUP BY c1, c2)\n" +
                            "    DATA INITIALLY DEFERRED\n" +
                            "    REFRESH DEFERRED \n");
            stmt.executeUpdate(
                    "CREATE SUMMARY TABLE d_ast AS " +
                            "  (SELECT c1, c2, COUNT(*) AS count " +
                            "    FROM t " +
                            "    GROUP BY c1, c2) " +
                            "  DATA INITIALLY DEFERRED " +
                            "  REFRESH DEFERRED ");
            System.out.println("  COMMIT");
            con.commit();

            // create staging table
            stmt.executeUpdate("CREATE TABLE g FOR d_ast PROPAGATE IMMEDIATE");
            System.out.println("\n  COMMIT");
            con.commit();

            stmt.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
//            JdbcException jdbcExc = new JdbcException(e) ;
//            jdbcExc.handle();
        }
    } // createStagingTable

    public static void propagateStagingToAst(Connection con)
    {
        try
        {
            Statement stmt = con.createStatement();

            stmt.executeUpdate("SET INTEGRITY FOR g IMMEDIATE CHECKED");
            con.commit();

            stmt.executeUpdate("REFRESH TABLE d_ast NOT INCREMENTAL");
            con.commit();

            stmt.executeUpdate(
                    "INSERT INTO t VALUES(1,1,1,1)," +
                            "                    (2,2,2,2)," +
                            "                    (1,1,1,1)," +
                            "                    (3,3,3,3)");
            con.commit();

            displayTable(con, "g");

            stmt.executeUpdate("REFRESH TABLE d_ast INCREMENTAL");
            con.commit();

            displayTable(con, "g");

            displayTable(con, "d_ast");

            stmt.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
//            JdbcException jdbcExc = new JdbcException(e) ;
//            jdbcExc.handle();
        }
    } // propagateStagingToAst

    // Shows how to restore the data in a summary table
    public static void restoreSummaryTable(Connection con)
    {
        try
        {
            Statement stmt = con.createStatement();

            stmt.executeUpdate("SET INTEGRITY FOR g OFF");
            con.commit();

            stmt.executeUpdate("SET INTEGRITY FOR d_ast OFF CASCADE IMMEDIATE");
            con.commit();

            stmt.executeUpdate("SET INTEGRITY FOR g IMMEDIATE CHECKED PRUNE");
            con.commit();

            stmt.executeUpdate("SET INTEGRITY FOR g STAGING IMMEDIATE UNCHECKED");
            con.commit();

            stmt.executeUpdate(
                    "SET INTEGRITY FOR d_ast MATERIALIZED QUERY IMMEDIATE UNCHECKED");
            con.commit();

            stmt.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
//            JdbcException jdbcExc = new JdbcException(e) ;
//            jdbcExc.handle();
        }
    } // restoreSummaryTable

    public static void displayTable(Connection con, String tableName)
    {
        try
        {
            Statement stmt = con.createStatement();
            String sqlString = null;
            ResultSet rs;
            int c1 = 0;
            int c2 = 0;
            int count = 0;

            if (tableName.equals("g"))
            {
                sqlString = "  SELECT c1, c2, count FROM g" ;
            }
            else if (tableName.equals("d_ast"))
            {
                sqlString = "  SELECT c1, c2, count FROM d_ast" ;
            }

            rs = stmt.executeQuery(sqlString);

            while (rs.next())
            {
                c1 = rs.getInt("c1");
                c2 = rs.getInt("c2");
                count = rs.getInt("count");

                System.out.println("   " + c1 + "    " + c2 + "    " + count );
            }

            rs.close();
            stmt.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
//            JdbcException jdbcExc = new JdbcException(e) ;
//            jdbcExc.handle();
        }
    } // displayTable

    // Drops the staging table, summary table and base table
    public static void dropTables(Connection con)
    {
        try
        {
            Statement stmt = con.createStatement();

            stmt.executeUpdate("DROP TABLE t");
            System.out.println("  COMMIT");
            con.commit();

            stmt.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
//            JdbcException jdbcExc = new JdbcException(e) ;
//            jdbcExc.handle();
        }
    } // dropTables
} // TbAST