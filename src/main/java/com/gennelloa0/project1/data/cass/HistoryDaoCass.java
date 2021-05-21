package com.gennelloa0.project1.data.cass;

import java.util.ArrayList;
import java.util.List;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import com.gennelloa0.project1.data.HistoryDao;
import com.gennelloa0.project1.factory.Log;
import com.gennelloa0.project1.utils.CassandraUtil;

@Log
public class HistoryDaoCass implements HistoryDao{
	private CqlSession session = CassandraUtil.getInstance().getSession();

	@Override
	public List<Row> getUserHistory(int userID) {
			List<Row> userHistory = new ArrayList<Row>();
			String query = "select form_id, user_id, message, timestamp from logs where user_id = " + userID;
			ResultSet rs = session.execute(query);
			userHistory = rs.all();
			return userHistory;
	}

	@Override
	public List<Row> getFormHistory(int formID) {
		List<Row> formHistory = new ArrayList<Row>();
		String query = "select form_id, user_id, message, timestamp from logs where form_id = " + formID;
		ResultSet rs = session.execute(query);
		formHistory = rs.all();
		return formHistory;
	}

	@Override
	public List<Row> getDateHistory(String date) {
		List<Row> dateHistory = new ArrayList<Row>();
		String query = "select form_id, user_id, message, timestamp from logs where timestamp LIKE " + date;
		ResultSet rs = session.execute(query);
		dateHistory = rs.all();
		return dateHistory;
	}

	@Override
	public void addHistory(int userID, int formID, String message, String timestamp) {
		String query = "Insert into logs (form_id, user_id, message, timestamp) values (?,?,?,?); ";
		SimpleStatement s = new SimpleStatementBuilder(query)
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = session.prepare(s).bind(formID, userID, message, timestamp);
		session.execute(bound);
		
	}
	
	
}
