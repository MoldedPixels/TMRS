package com.gennelloa0.project1.data;

import java.util.List;

import com.gennelloa0.project1.beans.User;

public interface UserDao {
	List<User> getUsers();
	User getUserByName(String fname, String lname);
	void addUser(User u) throws Exception;
	void updateUser(User u);
	User getUserByID(int userID);
}
