package com.gennelloa0.project1.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gennelloa0.project1.beans.Forms;
import com.gennelloa0.project1.beans.User;
import com.gennelloa0.project1.factory.BeanFactory;
import com.gennelloa0.project1.services.FormsService;
import com.gennelloa0.project1.services.FormsServiceImpl;
import com.gennelloa0.project1.services.UserService;
import com.gennelloa0.project1.services.UserServiceImpl;

import io.javalin.http.Context;

public class FormController {
	private static Logger log = LogManager.getLogger(FormController.class);
	private static FormsService fs = (FormsService) BeanFactory.getFactory().get(FormsService.class,
			FormsServiceImpl.class);
	private static UserService us = (UserService) BeanFactory.getFactory().get(UserService.class,
			UserServiceImpl.class);

	public static void getForms(Context ctx) {
		log.trace("Get Forms");
		log.debug(ctx.method() + " called for " + ctx.fullUrl());
		User u = (User) ctx.sessionAttribute("User");
		if (u == null || u.getRoleID() == 3) {
			ctx.status(403);
			return;
		}
		List<Forms> forms = fs.getForms();
		ctx.json(forms);
	};

	public static void submitForm(Context ctx) {
		log.trace("Submit Forms");
		log.debug(ctx.method() + " called for " + ctx.fullUrl());
		User u = (User) ctx.sessionAttribute("User");
		if (u == null || u.getRoleID() == 0) {
			ctx.status(403);
			return;
		}
		Forms f = new Forms();
		;
		f.setAssignedToID(-1);
		f.setBcoDate("");
		f.setBcoStatus(1);
		f.setDenialReason("");
		f.setDeniedBy(-1);
		f.setDeptHeadDate("");
		f.setDeptHeadID(Integer.parseInt(ctx.formParam("dhID")));
		f.setDeptHeadStatus(1);
		f.setDirectSupDate("");
		f.setDirectSupID(Integer.parseInt(ctx.formParam("dsID")));
		f.setDirectSupStatus(1);
		f.setEventDate(ctx.formParam("eventDate"));
		f.setEventType(Integer.parseInt(ctx.formParam("eventType")));
		f.setFormID(Integer.parseInt(ctx.formParam("formID")));
		f.setFormStatus(1);
		f.setGradingFormat(Integer.parseInt(ctx.formParam("grading")));
		f.setGradingApproved(false);
		f.setJustification("");
		f.setLast_updated("");
		f.setLast_updator(0);
		f.setRequestedAmount(Integer.parseInt(ctx.formParam("requestAmount")));
		f.setRequestedBy(Integer.parseInt(ctx.formParam("requestor")));
		f.setRequestedDate(ctx.formParam("requestDate"));
		String result = fs.submitForm(f);
		double newBal = u.getAvailableBal() - f.getRequestedAmount();
		u.setAvailableBal(newBal);
		us.updateUser(u);
		ctx.result(result);

	};

	public static void updateStatus(Context ctx) {

		log.trace("Update Form Status");
		log.debug(ctx.method() + " called for " + ctx.fullUrl());
		User u = (User) ctx.sessionAttribute("User");
		if (u == null || u.getRoleID() == 0) {
			ctx.status(403);
			return;
		}
		int formID = Integer.parseInt(ctx.formParam("formID"));
		log.debug(formID);
		int status = Integer.parseInt(ctx.formParam("status"));
		log.debug(status);
		String reason = ctx.formParam("reason");
		log.debug(status);
		log.debug(u.getUserID());
		String result = fs.updateStatus(formID, status, u.getUserID(), reason);
		ctx.json(result);
	}

	public static void updateAmount(Context ctx) {
		log.trace("Update Requested Amount");
		log.debug(ctx.method() + " called for " + ctx.fullUrl());
		User u = (User) ctx.sessionAttribute("User");
		if (u == null || u.getRoleID() == 0) {
			ctx.status(403);
			return;
		}
		int formID = Integer.parseInt(ctx.formParam("formID"));
		log.debug(formID);
		int amount = Integer.parseInt(ctx.formParam("status"));
		log.debug(amount);
		int userID = Integer.parseInt(ctx.formParam("userID"));
		log.debug(userID);
		String justification = ctx.formParam("justification");
		log.debug(justification);
		String result = fs.updateAmount(formID, amount, userID, justification);
		ctx.json(result);
	}

	public static void approveGrade(Context ctx) {
		log.trace("Update Grade");
		log.debug(ctx.method() + " called for " + ctx.fullUrl());
		User u = (User) ctx.sessionAttribute("User");
		if (u == null || u.getRoleID() == 0) {
			ctx.status(403);
			return;
		}
		int formID = Integer.parseInt(ctx.formParam("formID"));
		log.debug(formID);
		int userID = Integer.parseInt(ctx.formParam("userID"));
		log.debug(userID);
		String result = fs.approveGrade(formID, userID);
		ctx.json(result);
	}
	
	public static void getForm(Context ctx)
	{
		log.trace("Get Form");
		log.debug(ctx.method() + " called for " + ctx.fullUrl());
		User u = (User) ctx.sessionAttribute("User");
		if (u == null) {
			ctx.status(403);
			return;
		}
		Forms form = fs.getFormByID(Integer.parseInt(ctx.pathParam("fid")));
		ctx.json(form);
	}
}
