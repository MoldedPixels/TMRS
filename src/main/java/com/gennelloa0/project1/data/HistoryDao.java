package com.gennelloa0.project1.data;

import java.util.List;

import com.datastax.oss.driver.api.core.cql.Row;

public interface HistoryDao {
	List<Row> getUserHistory(int userID);
	List<Row> getFormHistory(int formID);
	List<Row> getDateHistory(String date);
	void addHistory(int userID, int formID, String message, String timestamp);
}
