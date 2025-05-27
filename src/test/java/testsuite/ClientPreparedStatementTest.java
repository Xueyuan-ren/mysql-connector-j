package testsuite;

import com.mysql.cj.BindValue;
import com.mysql.cj.DataStoreMetadata;
import com.mysql.cj.MessageBuilder;
import com.mysql.cj.NativeSession;
import com.mysql.cj.PreparedQuery;
import com.mysql.cj.QueryAttributesBindings;
import com.mysql.cj.QueryBindings;
import com.mysql.cj.QueryInfo;
import com.mysql.cj.ServerVersion;
import com.mysql.cj.Session;
import com.mysql.cj.conf.DatabaseUrlContainer;
import com.mysql.cj.conf.DefaultPropertySet;
import com.mysql.cj.conf.HostInfo;
import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.exceptions.ExceptionInterceptor;
import com.mysql.cj.interceptors.QueryInterceptor;
import com.mysql.cj.jdbc.result.CachedResultSetMetaData;
import com.mysql.cj.jdbc.result.ResultSetInternalMethods;
import com.mysql.cj.log.Log;
import com.mysql.cj.log.ProfilerEventHandler;
import com.mysql.cj.protocol.Message;
import com.mysql.cj.protocol.ServerSession;
import com.mysql.cj.protocol.ServerSessionStateController;
import com.mysql.cj.telemetry.TelemetryHandler;
import com.mysql.cj.xdevapi.Client;

import testsuite.QueryInfoTest.TestServerSession;
import testsuite.QueryInfoTest.TestSession;

import com.mysql.cj.jdbc.ClientInfoProvider;
import com.mysql.cj.jdbc.ClientPreparedStatement;
import com.mysql.cj.jdbc.CloseOption;
import com.mysql.cj.jdbc.ConnectionImpl;
import com.mysql.cj.jdbc.JdbcConnection;
import com.mysql.cj.jdbc.JdbcPreparedStatement;
import com.mysql.cj.jdbc.JdbcPropertySet;
import com.mysql.cj.jdbc.JdbcPropertySetImpl;
import com.mysql.cj.jdbc.JdbcStatement;

import org.junit.jupiter.api.Test;

import java.net.SocketAddress;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;

import javax.management.Query;

import testsuite.BaseQueryInterceptor;
import testsuite.BaseTestCase;
import testsuite.BufferingLogger;
import testsuite.TestUtils;

import static org.junit.jupiter.api.Assertions.*;

class ClientPreparedStatementTest extends BaseTestCase {

    // Minimal stub classes to allow instantiation
    // Use a mock or minimal stub for JdbcConnection to avoid implementing all abstract methods
    // Use Mockito for a minimal mock, or provide a minimal stub for only the methods used in the test.
    // Here is a minimal stub for only the methods used in the test:
    static class DummyJdbcConnection extends ConnectionImpl {
        private final HostInfo hostInfo;

        DummyJdbcConnection(HostInfo hostInfo) throws SQLException {
            super(hostInfo);
            this.hostInfo = hostInfo;
        }
    }

    @Test
    void testPrepareBatchedInsertSQL() throws SQLException {

        PropertySet propertySet = new DefaultPropertySet();
        String key = "rewriteBatchedStatements";
        boolean val = true;
        propertySet.getProperty(key).setValue(val);
        Map<String, String> properties = new java.util.HashMap<String, String>();
        properties.put(key, String.valueOf(val));
        // Provide non-null dummy values for HostInfo parameters to avoid NullPointerException
        DatabaseUrlContainer url = new DatabaseUrlContainer() {
            @Override
            public String getDatabaseUrl() {
                return "jdbc:mysql://localhost:3306/testdb";
            }
        };
        HostInfo hostInfo = new HostInfo(url, "localhost", 3306, "testuser", "testpass", properties);
        ServerSession serverSession = new TestServerSession();
        Session session = new TestSession(hostInfo, propertySet, serverSession);
        String encoding = "UTF-8";

      
        // String sql = "INSERT INTO test_table (col1, col2) VALUES (?, ?)";
        // QueryInfo queryInfo = new QueryInfo(sql, session, encoding);
        int numBatches = 3;
        // String batchedInsert = queryInfo.getSqlForBatch(numBatches);

        // assertNotNull(batchedInsert);
        // assertEquals("INSERT INTO test_table (col1, col2) VALUES (?, ?),(?, ?),(?, ?)", batchedInsert);

        //ClientPreparedStatement stmt = new ClientPreparedStatement((JdbcConnection) this.conn, sql, "testdb", queryInfo);
        // stmt.setString(1, "value1");
        // stmt.setString(2, "value2");
        // QueryBindings queryBindings = stmt.getQueryBindings();
        // BindValue[] bindValues = queryBindings.getBindValues();

        // assertEquals(2, bindValues.length);
        // assertEquals("value1", bindValues[0].getValue());
        // assertEquals("value2", bindValues[1].getValue());

        // Test the batched insert SQL
        // stmt.setString(1, "value1");
        // stmt.setString(2, "value2");
        // stmt.addBatch();
        // stmt.setString(1, "value3");
        // stmt.setString(2, "value4");
        // stmt.addBatch();
        // stmt.setString(1, "value5");
        // stmt.setString(2, "value6");
        // stmt.addBatch();
        // ClientPreparedStatement batchedInsertStmt = stmt.prepareBatchedInsertSQLWrap((JdbcConnection) this.conn, numBatches);
        // assertNotNull(batchedInsertStmt);
        // int[] batchResults = stmt.executeBatch();
        // assertNotNull(batchResults);

        // String batchedInsert = batchedInsertStmt.getPreparedSql();
        // String expectedBatchedInsert = "INSERT INTO test_table (col1, col2) VALUES (?, ?),(?, ?),(?, ?)";
        // assertNotNull(batchedInsert);
        // assertEquals(expectedBatchedInsert, batchedInsert);

        // QueryAttributesBindings batchedInsertQueryBindings = batchedInsertStmt.getQueryAttributesBindings();
        // int batchedInsertCount = batchedInsertQueryBindings.getCount();
        // assertEquals(6, batchedInsertCount);
        // // BindValue batchedInsertBindValue = batchedInsertQueryBindings.getAttributeValue(0);
        // // assertEquals("value1", batchedInsertBindValue.getValue());
        // for (int i = 0; i < batchedInsertCount; i++) {
        //     System.out.println("Bind value at index " + i + ": " + batchedInsertQueryBindings.getAttributeValue(i).getValue());
        // }
        // for (int i = 0; i < batchedInsertCount; i++) {
        //     System.out.println("Bind name at index " + i + ": " + batchedInsertQueryBindings.getAttributeValue(i).getName());
        // }

        // assertEquals("value1", batchedInsertBindValues[0].getValue());
        // assertEquals("value2", batchedInsertBindValues[1].getValue());
        // assertEquals("value3", batchedInsertBindValues[2].getValue());
        // assertEquals("value4", batchedInsertBindValues[3].getValue());
        // assertEquals("value5", batchedInsertBindValues[4].getValue());
        // assertEquals("value6", batchedInsertBindValues[5].getValue());

        // Test the batched update SQL
        String updateSql = "UPDATE test_table SET col1 = ?, col2 = ?, col3 = ? WHERE id1 = ?";
        QueryInfo updateQueryInfo = new QueryInfo(updateSql, session, encoding);

        ClientPreparedStatement updatestmt = new ClientPreparedStatement((JdbcConnection) this.conn, updateSql, "testdb", updateQueryInfo);
        updatestmt.setString(1, "batch1_col1");
        updatestmt.setString(2, "batch1_col2");
        updatestmt.setString(3, "batch1_col3");
        updatestmt.setString(4, "batch1_id1");
        // updatestmt.setString(5, "batch1_id2");
        updatestmt.addBatch();
        String sql1 = ((PreparedQuery) updatestmt.getQuery()).asSql();
        System.out.println("Prepared SQL1: " + sql1);
        updatestmt.setString(1, "batch2_col1");
        updatestmt.setString(2, "batch2_col2");
        updatestmt.setString(3, "batch2_col3");
        updatestmt.setString(4, "batch2_id1");
        // updatestmt.setString(5, "batch2_id2");
        updatestmt.addBatch();
        String sql2 = ((PreparedQuery) updatestmt.getQuery()).asSql();
        System.out.println("Prepared SQL2: " + sql2);
        updatestmt.setString(1, "batch3_col1");
        updatestmt.setString(2, "batch3_col2");
        updatestmt.setString(3, "batch3_col3");
        updatestmt.setString(4, "batch3_id1");
        // updatestmt.setString(5, "batch3_id2");
        updatestmt.addBatch();
        String sql3 = ((PreparedQuery) updatestmt.getQuery()).asSql();
        System.out.println("Prepared SQL3: " + sql3);
        // List<Object> batchedArgs = updatestmt.getBatchedArgs();
        // int batchedCount = batchedArgs.size();
        // for (int i = 0; i < batchedCount; i++) {
        //     QueryBindings queryBindings = (QueryBindings) batchedArgs.get(i);
        //     for (int j = 0; j < queryBindings.getBindValues().length; j++) {
        //         BindValue bindValue = queryBindings.getBindValues()[j];
        //         System.out.println("updatestmt: Bind value at index " + (i*queryBindings.getBindValues().length + j) + ": " + bindValue.getValue());
        //     }
        // }
        
        int[] updateBatchResults = updatestmt.executeBatch();

        // String batchedUpdate = updateQueryInfo.getBatchedSqlForUpdate(numBatches);
        // String batchedUpdateSql = "UPDATE test_table SET " +
        //         "col1 = CASE WHEN id1 = ? AND id2 = ? THEN ? WHEN id1 = ? AND id2 = ? THEN ? WHEN id1 = ? AND id2 = ? THEN ? ELSE col1 END, " +
        //         "col2 = CASE WHEN id1 = ? AND id2 = ? THEN ? WHEN id1 = ? AND id2 = ? THEN ? WHEN id1 = ? AND id2 = ? THEN ? ELSE col2 END " +
        //         "WHERE (id1, id2) IN ((?,?), (?,?), (?,?))";
        // assertNotNull(batchedUpdate);
        // assertEquals(batchedUpdateSql, batchedUpdate);


    }
}