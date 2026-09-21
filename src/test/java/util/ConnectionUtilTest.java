//package util;
//
//import org.junit.jupiter.api.Test;
//
//import java.sql.Connection;
//import java.sql.DatabaseMetaData;
//import java.sql.SQLException;
//
//public class ConnectionUtilTest {
//
//    @Test
//    void testConnectionSuccess() throws SQLException {
//
//        Connection connection = ConnectionUtil.getDataSource().getConnection();
//        DatabaseMetaData databaseMetaData = connection.getMetaData();
//
//        System.out.println(databaseMetaData.getDriverName());
//        System.out.println(databaseMetaData.getDatabaseProductVersion());
//
//        connection.close();
//    }
//}
