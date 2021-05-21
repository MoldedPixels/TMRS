package com.gennelloa0.project1.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gennelloa0.project1.beans.User;
import com.gennelloa0.project1.factory.BeanFactory;
import com.gennelloa0.project1.services.UserService;
import com.gennelloa0.project1.services.UserServiceImpl;

import io.javalin.http.Context;

public class UserController {
	private static UserService us = (UserService) BeanFactory.getFactory().get(UserService.class, UserServiceImpl.class);

	private static final Logger log = LogManager.getLogger(UserController.class);
	
	public static void getUsers(Context ctx) {
		User u = (User) ctx.sessionAttribute("User");
		if(u == null || u.getRoleID() != 3) {
			ctx.status(403);
			ctx.result("Well that didn't work.");
			return;
		}
		List<User> players = us.getUsers();
		ctx.json(players);
	}
	
	public static void update(Context ctx) {
		User u = (User) ctx.sessionAttribute("User");
		log.debug(u);
		int userID = (Integer.parseInt(ctx.pathParam("uid")));
		log.debug(userID);
		
		if(userID == 0) {
			ctx.status(400);
			ctx.result("Maybe you should try again later");
			return;
		}
		
		if(u == null || u.getRoleID() != 3) {
			ctx.status(403);
			ctx.result("Did you try asking for help?");
			return;
		}
		u = us.getUserByID(userID);
		u.setUserID(Integer.parseInt(ctx.formParam("userID")));
		u.setFirstName(ctx.formParam("fname"));
		u.setLastName(ctx.formParam("lname"));
		u.setRoleID(Integer.parseInt(ctx.formParam("roleID")));
		u.setDirectSupID(Integer.parseInt(ctx.formParam("directSupID")));
		u.setDeptHeadID(Integer.parseInt(ctx.formParam("deptHeadID")));
		u.setAvailableBal(Double.parseDouble(ctx.formParam("bal")));
		us.updateUser(u);
		ctx.json(u);
	}
	
	public static void register(Context ctx) {
		User u = new User();
		u.setUserID(Integer.parseInt(ctx.formParam("userID")));
		u.setFirstName(ctx.formParam("fname"));
		u.setLastName(ctx.formParam("lname"));
		u.setRoleID(Integer.parseInt(ctx.formParam("roleID")));
		u.setDirectSupID(Integer.parseInt(ctx.formParam("directSupID")));
		u.setDeptHeadID(Integer.parseInt(ctx.formParam("deptHeadID")));
		u.setAvailableBal(1000.00);
		
		boolean added = us.addUser(u);
		if (added) {
			ctx.json(u);
		} else {
			ctx.status(409);
			ctx.result("You can't do that 4head.");
		}
	}

	public static void login(Context ctx) {
		// We can get session information from the Context to use it elsewhere.
		if(ctx.sessionAttribute("User") != null) {
			ctx.status(204);
			ctx.result("Wait a minute, you already exist.");
			return;
		}
		int userID = (Integer.parseInt(ctx.formParam("uid")));
		User u = us.getUserByID(userID);
		if (u == null || u.getRoleID() == 0) {
			ctx.status(401);
			ctx.result("I thought we got rid of you already.");
		} else {
			ctx.sessionAttribute("User", u);
			ctx.json(u);
		}
	}
	
	public static void logout(Context ctx) {
		ctx.req.getSession().invalidate();
	}
}
