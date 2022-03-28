package io.seata.rm.datasource.exec.oracle;

import io.seata.config.Configuration;
import io.seata.config.ConfigurationFactory;
import io.seata.rm.datasource.StatementProxy;
import io.seata.rm.datasource.exec.AbstractDMLBaseExecutor;
import io.seata.rm.datasource.exec.StatementCallback;
import io.seata.rm.datasource.sql.struct.TableRecords;
import io.seata.sqlparser.SQLRecognizer;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * The type oracle multi insert recognizer.
 *
 * @author renliangyu857
 */
public class OracleMultiInsertExecutor <T, S extends Statement> extends AbstractDMLBaseExecutor<T, S> {
    private static final Configuration CONFIG = ConfigurationFactory.getInstance();

    /**
     * Instantiates a new Multi update executor.
     *
     * @param statementProxy    the statement proxy
     * @param statementCallback the statement callback
     * @param sqlRecognizers    the sql recognizers
     */
    public OracleMultiInsertExecutor(StatementProxy<S> statementProxy, StatementCallback<T, S> statementCallback, List<SQLRecognizer> sqlRecognizers) {
        super(statementProxy, statementCallback, sqlRecognizers);
    }

    @Override
    protected TableRecords beforeImage() throws SQLException {
        return TableRecords.empty(getTableMeta(sqlRecognizers.get(0).getTableName()));
    }

    @Override
    protected TableRecords afterImage(TableRecords beforeImage) throws SQLException {
        if (sqlRecognizers.size() == 1) {
            OracleInsertExecutor executor = new OracleInsertExecutor(statementProxy, statementCallback, sqlRecognizers.get(0));
            return executor.afterImage(beforeImage);
        }
        TableRecords tableRecords = new TableRecords(getTableMeta(sqlRecognizers.get(0).getTableName()));
        for(SQLRecognizer sqlRecognizer : sqlRecognizers){
            OracleInsertExecutor executor = new OracleInsertExecutor(statementProxy,statementCallback,sqlRecognizer);
            TableRecords itemRecord = executor.afterImage(beforeImage);
            tableRecords.addRows(itemRecord.getRows());
        }
        return tableRecords;
    }
}
