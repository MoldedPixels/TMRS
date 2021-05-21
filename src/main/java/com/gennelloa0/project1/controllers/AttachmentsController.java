package com.gennelloa0.project1.controllers;

import java.util.Arrays;

import com.gennelloa0.project1.beans.User;
import com.gennelloa0.project1.factory.BeanFactory;
import com.gennelloa0.project1.services.FormsService;
import com.gennelloa0.project1.services.FormsServiceImpl;
import com.gennelloa0.project1.services.HistoryService;
import com.gennelloa0.project1.services.HistoryServiceImpl;
import com.gennelloa0.project1.utils.S3Util;

import io.javalin.http.Context;

public class AttachmentsController {
	private static String[] allowed = { "jpeg", "pdf", "png", "txt", "doc", "msg" };
	private static HistoryService hs = (HistoryService) BeanFactory.getFactory().get(HistoryService.class,
			HistoryServiceImpl.class);
	private static FormsService fs = (FormsService) BeanFactory.getFactory().get(FormsService.class,
			FormsServiceImpl.class);

	public static void upload(Context ctx) {
		String name = "";
		if (Arrays.asList(allowed).contains(ctx.pathParam("filetype"))) {
			User u = (User) ctx.sessionAttribute("User");
			if (u == null || u.getRoleID() == 5) {
				ctx.status(403);
				ctx.result("Who do you think you are?");
				return;
			}
			name = new StringBuilder(ctx.pathParam("formID")).append(u.getFirstName() + "_" + u.getLastName() + "." + ctx.pathParam("filetype")).toString();
			byte[] bytes = ctx.bodyAsBytes();

			// hs.addHistory(0, Integer.parseInt(ctx.pathParam("formID")), "A ." +
			// ctx.pathParam("filetype") + " Attachment has been uploaded for Form #" +
			// ctx.pathParam("formID") + " has been submitted", "");
			if (ctx.pathParam("grade").equals("grade")) {
				fs.approveGrade(Integer.parseInt(ctx.pathParam("formID")), u.getUserID());
			}

			try {
				S3Util.getInstance().uploadToBucket(name, bytes);
			} catch (Exception e) {
				ctx.status(500);
				ctx.result("Oh hey look it failed. How sad.");
			}
			ctx.result("Oh hey look it worked");
			ctx.status(200);
			

		} else {
			ctx.status(500);
			ctx.result("That file doesn't look right.");
		}
	}

	public static void download(Context ctx) {
		String name = new StringBuilder(ctx.pathParam("formID")).append("." + ctx.pathParam("filetype")).toString();
		try {
			// InputStream s = S3Util.getInstance().getObject(name);
			// ctx.result(s);

			String url = S3Util.getInstance().generateURL(name);
			ctx.result(url);
		} catch (Exception e) {
			ctx.status(500);
			ctx.result("Looks like your file pulled a Houdini.");
		}
	}
}
