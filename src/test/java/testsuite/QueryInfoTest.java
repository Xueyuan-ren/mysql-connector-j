// File: mysql-connector-j/src/test/core-api/java/com/mysql/cj/QueryInfoTest.java
package testsuite;


import static org.junit.jupiter.api.Assertions.*;

import java.net.SocketAddress;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.Lock;

import org.junit.jupiter.api.Test;

import com.mysql.cj.protocol.Message;
import com.mysql.cj.protocol.ServerCapabilities;
import com.mysql.cj.protocol.ServerSession;
import com.mysql.cj.telemetry.TelemetryHandler;
import com.mysql.cj.CharsetSettings;
import com.mysql.cj.DataStoreMetadata;
import com.mysql.cj.MessageBuilder;
import com.mysql.cj.Messages;
import com.mysql.cj.NativeSession;
import com.mysql.cj.PlaceholderPurpose;
import com.mysql.cj.QueryInfo;
import com.mysql.cj.QueryReturnType;
import com.mysql.cj.ServerVersion;
import com.mysql.cj.Session;
import com.mysql.cj.Session.SessionEventListener;
import com.mysql.cj.conf.*;
import com.mysql.cj.exceptions.*;
import com.mysql.cj.log.Log;
import com.mysql.cj.log.ProfilerEventHandler;
import com.mysql.cj.util.StringUtils;


class QueryInfoTest {

    static class TestSession extends NativeSession {
        private final ServerSession serverSession;

        TestSession(HostInfo hostInfo, PropertySet propertySet, ServerSession serverSession) {
            super(hostInfo, propertySet);
            this.serverSession = serverSession;
        }

        @Override
        public ServerSession getServerSession() {
            return serverSession;
        }
    }

    static class TestServerSession implements ServerSession {

        public TestServerSession() {
        }

        @Override
        public int getStatusFlags() {
            return 0;
        }

        @Override
        public void setSessionTimeZone(java.util.TimeZone tz) {
        }

        @Override
        public boolean noGoodIndexUsed() {
            return false;
        }

        @Override
        public boolean inTransactionOnServer() {
            return false;
        }

        @Override
        public boolean hasMoreResults() {
            return false;
        }

        @Override
        public boolean queryWasSlow() {
            return false;
        }

        @Override
        public void setAutoCommit(boolean autoCommitFlag) {
        }

        @Override
        public CharsetSettings getCharsetSettings() {
            return null;
        }

        @Override
        public void setCharsetSettings(CharsetSettings cs) {
        }

        @Override
        public boolean supportsQueryAttributes() {
            return false;
        }

        @Override
        public int getTransactionState() {
            return 0;
        }

        @Override
        public void setCapabilities(ServerCapabilities capabilities) {
        }

        @Override
        public boolean isServerTruncatesFracSecs() {
            return false;
        }

        @Override
        public void setClientParam(long clientParam) {
        }

        @Override
        public java.util.TimeZone getDefaultTimeZone() {
            return null;
        }

        @Override
        public boolean isEOFDeprecated() {
            return false;
        }

        @Override
        public boolean storesLowerCaseTableNames() {
            return false;
        }

        @Override
        public void setStatusFlags(int statusFlags, boolean saveOldStatusFlags) {
        }

        @Override
        public void setStatusFlags(int statusFlags) {
        }

        @Override
        public boolean isLowerCaseTableNames() {
            return false;
        }

        @Override
        public boolean noIndexUsed() {
            return false;
        }

        @Override
        public boolean isAutocommit() {
            return false;
        }

        @Override
        public java.util.Map<String, String> getServerVariables() {
            return null;
        }

        @Override
        public void setServerVariables(java.util.Map<String, String> serverVariables) {
        }

        @Override
        public boolean isQueryCacheEnabled() {
            return false;
        }

        @Override
        public boolean useMultiResults() {
            return false;
        }

        @Override
        public boolean hasLongColumnInfo() {
            return false;
        }

        @Override
        public boolean isVersion(ServerVersion version) {
            return false;
        }

        @Override
        public int getServerVariable(String name, int fallbackValue) {
            return fallbackValue;
        }

        @Override
        public String getServerVariable(String name) {
            return null;
        }

        @Override
        public ServerVersion getServerVersion() {
            return null;
        }

        @Override
        public long getClientParam() {
            return 0;
        }

        @Override
        public boolean useAnsiQuotedIdentifiers() {
            return false;
        }

        @Override
        public boolean cursorExists() {
            return false;
        }

        @Override
        public boolean isSessionStateTrackingEnabled() {
            return false;
        }

        @Override
        public boolean isAutoCommit() {
            return false;
        }

        @Override
        public boolean isNoBackslashEscapesSet() {
            return false;
        }

        @Override
        public java.util.TimeZone getSessionTimeZone() {
            return null;
        }

        @Override
        public boolean isLastRowSent() {
            return false;
        }

        @Override
        public ServerCapabilities getCapabilities() {
            return null;
        }
    }


    @Test
    void testBatchedUpdateStatementParsing() {
        // Original SQL: multi-column SET and multi-column WHERE
        String sql = "UPDATE test SET col1=?, col2=? WHERE id1=? AND id2=?";
        PropertySet propertySet = new DefaultPropertySet();
        String key = "rewriteBatchedStatements";
        boolean val = true;
        propertySet.getProperty(key).setValue(val);
        HostInfo hostInfo = new HostInfo();
        ServerSession serverSession = new TestServerSession();
        Session session = new TestSession(hostInfo, propertySet, serverSession);
        String encoding = "UTF-8";
        

        // Step 1: Create base QueryInfo
        QueryInfo baseQueryInfo = new QueryInfo(sql, session, encoding);

        // Step 2: Create batched QueryInfo (simulate batching 2 updates)
        int batchCount = 2;
        // generate the batched queryinfo
        QueryInfo batchedQueryInfo = baseQueryInfo.getQueryInfoForBatchedUpdate(batchCount);
        String batchedSql = baseQueryInfo.getBatchedSqlForUpdate(batchCount);

        // Step 3: Check the batched SQL
        String expectedSql =
            "UPDATE test SET col1 = CASE WHEN id1 = ? AND id2 = ? THEN ? WHEN id1 = ? AND id2 = ? THEN ? ELSE col1 END, " +
            "col2 = CASE WHEN id1 = ? AND id2 = ? THEN ? WHEN id1 = ? AND id2 = ? THEN ? ELSE col2 END " +
            "WHERE (id1, id2) IN ((?,?), (?,?))";
        assertEquals(expectedSql.replaceAll("\\s+", " "), batchedSql.replaceAll("\\s+", " "));

        // Step 4: Check staticSqlParts and placeholders
        byte[][] staticParts = batchedQueryInfo.getStaticSqlParts(); 
        int expectedPlaceholders = batchCount * (2 + 2 + 1 + 2 + 1); // 
        assertEquals(expectedPlaceholders, staticParts.length - 1);

        // Step 5: Check placeholder purposes
        List<PlaceholderPurpose> purposes = batchedQueryInfo.getPlaceholderPurposes();
        assertEquals(expectedPlaceholders, purposes.size());
        for (PlaceholderPurpose p : purposes) {
            assertEquals(PlaceholderPurpose.GENERIC, p);
        }
    }
}