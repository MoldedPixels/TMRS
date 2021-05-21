package com.gennelloa0.project1.controllers;

import com.gennelloa0.project1.beans.User;
import com.gennelloa0.project1.factory.BeanFactory;
import com.gennelloa0.project1.services.HistoryService;
import com.gennelloa0.project1.services.HistoryServiceImpl;

import io.javalin.http.Context;

public class HistoryController {
	private static HistoryService hs = (HistoryService) BeanFactory.getFactory().get(HistoryService.class,
			HistoryServiceImpl.class);

	public static void getHistoryByUser(Context ctx) {
		User u = ctx.sessionAttribute("User");
		if (u == null || u.getRoleID() < 3) { // u.getUserID == getUser(form userid).getDirectSup()?
			ctx.status(403);
			ctx.result("Nononononono");
			return;
		}

		int userID = Integer.parseInt(ctx.formParam("user_id"));
		ctx.json(hs.getHistoryByUser(userID));
	}

	public static void getHistoryByForm(Context ctx) {
		User u = ctx.sessionAttribute("User");
		if (u == null || u.getRoleID() < 3) {
			ctx.status(403);
			ctx.result("I bet you thought that would work, huh.");
			return;
		}

		int formID = Integer.parseInt(ctx.formParam("formID"));
		ctx.json(hs.getHistoryByForm(formID));
	}
	
	public static void getHistoryByDate(Context ctx) {
		User u = ctx.sessionAttribute("User");
		if (u == null || u.getRoleID() < 3) {
			ctx.status(403);
			ctx.result("You're better off trying to convert the pope.");
			return;
		}

		String date = ctx.formParam("date");
		ctx.json(hs.getHistoryByDate(date));
	}
}
