package com.gennelloa0.project1.services;

import java.util.List;

import com.gennelloa0.project1.beans.Forms;

public interface FormsService {

	List<Forms> getForms();

	String submitForm(Forms f);

	String updateAmount(int formID, int newAmount, int userID, String justification);

	String approveGrade(int formID, int userID);

	String updateStatus(int formID, int status, int userID, String reason);

	Forms getFormByID(int formID);

}
