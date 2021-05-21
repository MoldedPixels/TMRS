package com.gennelloa0.project1.services;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.oss.driver.api.core.cql.Row;
import com.gennelloa0.project1.data.HistoryDao;
import com.gennelloa0.project1.data.cass.FormDaoCass;
import com.gennelloa0.project1.factory.BeanFactory;
import com.gennelloa0.project1.factory.Log;

@Log
public class HistoryServiceImpl implements HistoryService {
	private static final Logger log = LogManager.getLogger(HistoryServiceImpl.class);
	private HistoryDao hd = (HistoryDao) BeanFactory.getFactory().get(HistoryDao.class, FormDaoCass.class);
	
	@Override
	public ArrayList<String> getHistoryByUser(int userID) {
		ArrayList<Row> hl = (ArrayList<Row>) hd.getUserHistory(userID);
		ArrayList<String> results = new ArrayList<String>();
		for (Row r : hl) {
			String result = "";
			result += r.getString("user_id") + " ,";
			result += r.getString("form_id") + " ,";
			result += r.getString("timestamp") + " ,";
			result += r.getString("message");
			results.add(result);
		}
		return results;
	}
	@Override
	public ArrayList<String> getHistoryByForm(int formID) {
		ArrayList<Row> hl = (ArrayList<Row>) hd.getFormHistory(formID);
		ArrayList<String> results = new ArrayList<String>();
		for (Row r : hl) {
			String result = "";
			result += r.getString("form_id") + " ,";
			result += r.getString("user_id") + " ,";
			result += r.getString("timestamp") + " ,";
			result += r.getString("message");
			results.add(result);
		}
		return results;
	}
	@Override
	public ArrayList<String> getHistoryByDate(String date) {
		ArrayList<Row> hl = (ArrayList<Row>) hd.getDateHistory(date);
		ArrayList<String> results = new ArrayList<String>();
		for (Row r : hl) {
			String result = "";
			result += r.getString("timestamp") + " ,";
			result += r.getString("form_id") + " ,";
			result += r.getString("user_id") + " ,";
			result += r.getString("message");
			results.add(result);
		}
		return results;
	}
	@Override
	public void addHistory(int userID, int formID, String message, String timestamp) {
		hd.addHistory(userID, formID, message, timestamp);
	}
	
}
