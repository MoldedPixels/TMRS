package com.gennelloa0.project1.data;

import java.util.List;

import com.gennelloa0.project1.beans.Forms;

public interface FormDao {

	void submitForm(Forms f);

	void updateAmount(int formID, int newAmount, int userID, String justification);

	List<Forms> getForms();

	Forms getFormsByID(int formID);

	boolean getGrade(int formID);

	void approveGrade(int formID);

	int assignBco(Forms f);
	
	void updateStatus(int formID, int status, int userID, String reason);
	
}
