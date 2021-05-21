package com.gennelloa0.project1.services;

import java.util.ArrayList;


public interface HistoryService {

	ArrayList<String> getHistoryByUser(int userID);

	ArrayList<String> getHistoryByForm(int formID);

	ArrayList<String> getHistoryByDate(String date);
	
	void addHistory(int userID, int formID, String message, String timestamp);


}