package com.gennelloa0.project1.services;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gennelloa0.project1.beans.Forms;
import com.gennelloa0.project1.data.cass.FormDaoCass;
import com.gennelloa0.project1.data.FormDao;
import com.gennelloa0.project1.factory.BeanFactory;
import com.gennelloa0.project1.factory.Log;
@Log
public class FormsServiceImpl implements FormsService {
	private static Logger log = LogManager.getLogger(FormsServiceImpl.class);
	private FormDao fd = (FormDao) BeanFactory.getFactory().get(FormDao.class, FormDaoCass.class);
	private HistoryService hs = (HistoryService) BeanFactory.getFactory().get(HistoryService.class,
			HistoryServiceImpl.class);

	@Override
	public List<Forms> getForms() {
		return fd.getForms();
	}

	@Override
	public String submitForm(Forms f) {
		String result = "";
		try {
			int ds = f.getDirectSupID();
			int dh = f.getDeptHeadID();
			int bco = fd.assignBco(f);
			f.setAssignedToID(bco);
			fd.submitForm(f);
			
			//hs.addHistory(f.getRequestedBy(), f.getFormID(), "Form #" + f.getFormID() + " has been submitted", "");
			//hs.addHistory(f.getRequestedBy(), f.getFormID(),
					//"Notified the following: DS- " + ds + ", DH- " + dh + ", BCO- " + bco, "");
			result = "Form #" + f.getFormID() + " has been submitted. Notified the following: DS- " + ds + ", DH- " + dh + ", BCO- " + bco;
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result = "FAILED";
			return result;
		}
	}

	@Override
	public String updateStatus(int formID, int status, int userID, String reason) {
		String result = "";
		try {
			if (fd.getFormsByID(formID).getRequestedBy() == userID) {
				return "No, you can't approve your own request.";
			}
			if (status == 2) // status == Information Requested
			{
				result += "Notified User " + fd.getFormsByID(formID).getRequestedBy() + "of the request.";
			}
			if (status == 3 && fd.getGrade(formID)) // status == Completed
			{
				status = 4; // status = Dispensed
			}
			fd.updateStatus(formID, status, userID, reason);
			//hs.addHistory(userID, formID, "Form #" + formID + "'s status has been updated", "");
			result += "Form #" + formID + "'s status has been updated";
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Failed to update status for Form #" + formID);
			return "Failed to update status for Form #" + formID;
		}
	}

	@Override
	public String updateAmount(int formID, int newAmount, int userID, String justification) {
		try {
			fd.updateAmount(formID, newAmount, userID, justification);
			hs.addHistory(userID, formID, "Form #" + formID + "'s request amount has been updated", "");
			return "Form #" + formID + "'s request amount has been updated";
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Failed to update amount for Form #" + formID);
			return "Failed to update amount for Form #" + formID;
		}
	}

	@Override
	public String approveGrade(int formID, int userID) {
		try {
			fd.approveGrade(formID);
			//hs.addHistory(userID, formID, "Form #" + formID + "'s grade has been approved", "");
			return "Form #" + formID + "'s grade has been approved";
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Failed to update grade for Form #" + formID);
			return "Failed to update grade for Form #" + formID;
		}
	}
	@Override
	public Forms getFormByID(int formID)
	{
		return fd.getFormsByID(formID);
	}
	
}
