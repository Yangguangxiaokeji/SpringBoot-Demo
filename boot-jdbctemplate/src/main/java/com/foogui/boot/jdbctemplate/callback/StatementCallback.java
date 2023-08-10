package com.foogui.boot.jdbctemplate.callback;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Statement执行后的回调
 *
 * @author Foogui
 * @date 2023/08/09
 */
@FunctionalInterface
public interface StatementCallback {
   List<Object> handleResultSet(ResultSet resultSet) throws SQLException;
}
