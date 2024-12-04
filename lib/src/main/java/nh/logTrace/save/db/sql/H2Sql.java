package nh.logTrace.save.db.sql;

public class H2Sql extends Sql{

    private String H2_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS logtrace (\n" +
            "    id BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
            "    transaction_id VARCHAR(255),\n" +
            "    class_name VARCHAR(255),\n" +
            "    method_name VARCHAR(255),\n" +
            "    args VARCHAR(255),\n" +
            "    result VARCHAR(255),\n" +
            "    throwable_message VARCHAR(255),\n" +
            "    created_at TIMESTAMP,\n" +
            "    INDEX idx_created_at (created_at)\n" +
            ");";

    private String H2_INSERT = "INSERT INTO logtrace (\n" +
            "    transaction_id, class_name, method_name, args, result, throwable_message, created_at\n" +
            ") VALUES (\n" +
            "    ?, ?, ?, ?, ?, ?, ?\n" +
            ");";

    private String H2_SELECT = "SELECT * FROM logtrace WHERE id = ?;";
    private String H2_SELECT_ALL = "SELECT * FROM logtrace";

    private String H2_SELECT_BY_CREATED_AT = "SELECT * FROM logtrace WHERE created_at BETWEEN ? AND ?";

    public H2Sql() {
        this.createTableSql = H2_CREATE_TABLE;
        this.insertSql = H2_INSERT;
        this.selectSql = H2_SELECT;
        this.selectAllSql = H2_SELECT_ALL;
        this.selectByCreatedAt = H2_SELECT_BY_CREATED_AT;
    }
}
