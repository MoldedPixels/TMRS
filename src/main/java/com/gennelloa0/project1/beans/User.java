package com.gennelloa0.project1.beans;

public class User{
	private int userID;
	private String firstname;
	private String lastname;
	private int roleID;
	private int directSupID;
	private int deptHeadID;
	private double availableBal;
	
	public User() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
		result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
		result = prime * result + userID;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (firstname == null) {
			if (other.firstname != null)
				return false;
		} else if (!firstname.equals(other.firstname))
			return false;
		if (lastname == null) {
			if (other.lastname != null)
				return false;
		} else if (!lastname.equals(other.lastname))
			return false;
		if (userID != other.userID)
			return false;
		if (directSupID != other.directSupID)
			return false;
		if (deptHeadID != other.deptHeadID)
			return false;
		if (availableBal != other.availableBal)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Player [name=" + firstname + lastname + ", role=" + roleID + ", userID=" + userID + "]";
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public int getRoleID() {
		return roleID;
	}
	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}
	public int getDirectSupID() {
		return directSupID;
	}
	public void setDirectSupID(int directSupID) {
		this.directSupID = directSupID;
	}
	public int getDeptHeadID() {
		return deptHeadID;
	}
	public void setDeptHeadID(int deptHeadID) {
		this.deptHeadID = deptHeadID;
	}
	public double getAvailableBal() {
		return availableBal;
	}
	public void setAvailableBal(double d) {
		this.availableBal = d;
	}
	public String getFirstName() {
		return firstname;
	}
	public void setFirstName(String fname) {
		this.firstname = fname;
	}
	public String getLastName() {
		return lastname;
	}
	public void setLastName(String lname) {
		this.lastname = lname;
	}
}
