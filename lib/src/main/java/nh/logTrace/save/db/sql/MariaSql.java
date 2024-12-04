package nh.logTrace.save.db.sql;

public class MariaSql extends Sql {

    private String MARIA_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS logtrace (\n" +
            "    id BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
            "    transaction_id VARCHAR(255),\n" +
            "    class_name VARCHAR(255),\n" +
            "    method_name VARCHAR(255),\n" +
            "    args VARCHAR(255),\n" +
            "    result VARCHAR(255),\n" +
            "    throwable_message VARCHAR(255),\n" +
            "    created_at DATETIME,\n" +
            "    INDEX idx_created_at (created_at)\n" +
            ");";

    private String MARIA_INSERT = "INSERT INTO logtrace (\n" +
            "    transaction_id, class_name, method_name, args, result, throwable_message, created_at\n" +
            ") VALUES (\n" +
            "    ?, ?, ?, ?, ?, ?, ?\n" +
            ");";

    private String MARIA_SELECT = "SELECT * FROM logtrace WHERE id = ?;";
    private String MARIA_SELECT_ALL = "SELECT * FROM logtrace";

    private String MARIA_SELECT_BY_CREATED_AT = "SELECT * FROM logtrace WHERE created_at BETWEEN ? AND ?";

    public MariaSql() {
        this.createTableSql = MARIA_CREATE_TABLE;
        this.insertSql = MARIA_INSERT;
        this.selectSql = MARIA_SELECT;
        this.selectAllSql = MARIA_SELECT_ALL;
        this.selectByCreatedAt = MARIA_SELECT_BY_CREATED_AT;
    }
}
