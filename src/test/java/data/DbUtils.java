package data;

import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtils {
    private static final String url= System.getProperty("db.url");
    private static final String user = System.getProperty("db.user");
    private static final String password = System.getProperty("db.password");

    public static void clearTables() {
        val deletePaymentEntity = "DELETE FROM payment_entity";
        val deleteCreditEntity = "DELETE FROM credit_request_entity";
        val deleteOrderEntity = "DELETE FROM order_entity";
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(
                url, user, password)
        ) {
            runner.update(conn, deletePaymentEntity);
            runner.update(conn, deleteCreditEntity);
            runner.update(conn, deleteOrderEntity);

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }
    public static String getPaymentStatus() {
        String statusSQL = "SELECT status FROM payment_entity";
        return getStatus(statusSQL);
    }

    public static String getCreditStatus() {
        String statusSQL = "SELECT status FROM credit_request_entity";
        return getStatus(statusSQL);
    }

    private static String getStatus(String query) {
        String result = "";
        val runner = new QueryRunner();
        try
                (val conn = DriverManager.getConnection(
                        url, user, password)
                ) {

            result = runner.query(conn, query, new ScalarHandler<>());
            System.out.println(result);
            return result;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return result;
    }
}