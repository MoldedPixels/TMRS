package com.gennelloa0.project1.services;

import java.util.List;

import com.gennelloa0.project1.beans.User;

public interface UserService {

	User getUser(String fname, String lname);
	
	boolean addUser(User u);
	
	void updateUser(User u);
	
	List<User> getUsers();

	User getUserByID(int userID);
}
