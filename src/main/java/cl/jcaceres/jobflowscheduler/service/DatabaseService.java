package cl.jcaceres.jobflowscheduler.service;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import cl.jcaceres.jobflowscheduler.util.JobLogger;

public class DatabaseService {

    private DatabaseService() {
    }

    public static void executeQueries(String host, int port, String dbName, String user, String password,
                                      String sqlContent, JobLogger log) {

        DataSource ds = createDataSource(host, port, dbName, user, password);
        JdbcTemplate jdbc = new JdbcTemplate(ds);

        String[] statements = sqlContent.split(";");

        for (String stmt : statements) {
            String trimmed = stmt.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("--")) {
                continue;
            }

            String upper = trimmed.toUpperCase();
            if (upper.startsWith("SELECT") || upper.startsWith("WITH")) {
                List<Map<String, Object>> rows = jdbc.queryForList(trimmed);
                log.info("Query ejecutada: {} ({} filas)", trimmed.substring(0, Math.min(60, trimmed.length())), rows.size());
                for (Map<String, Object> row : rows) {
                    log.info("  {}", row);
                }
            } else {
                int affected = jdbc.update(trimmed);
                log.info("Query ejecutada: {} ({} filas afectadas)", trimmed.substring(0, Math.min(60, trimmed.length())), affected);
            }
        }
    }

    private static DataSource createDataSource(String host, int port, String dbName, String user, String password) {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setServerName(host);
        ds.setPortNumber(port);
        ds.setDatabaseName(dbName);
        ds.setUser(user);
        ds.setPassword(password);
        ds.setEncrypt(false);
        return ds;
    }
}
