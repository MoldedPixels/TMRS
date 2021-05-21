package com.gennelloa0.project1.data.cass;

import java.util.ArrayList;
import java.util.List;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import com.gennelloa0.project1.beans.User;
import com.gennelloa0.project1.data.UserDao;
import com.gennelloa0.project1.factory.Log;
import com.gennelloa0.project1.utils.CassandraUtil;

@Log
public class UserDaoCass implements UserDao{
	private CqlSession session = CassandraUtil.getInstance().getSession();
	
	@Override
	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();
		
		String query = "select * from user";
		ResultSet rs = session.execute(query);
		
		rs.forEach(data -> {
			User u = new User();
			u.setFirstName(data.getString("firstname"));
			u.setLastName(data.getString("lastname"));
			u.setRoleID(data.getInt("role"));
			u.setUserID(data.getInt("user_id"));
			u.setAvailableBal(data.getFloat("available_balance"));
			u.setDirectSupID(data.getInt("direct_sup"));
			u.setDeptHeadID(data.getInt("dept_head"));
			users.add(u);
		});
		
		return users;
	}

	@Override
	public User getUserByName(String fname, String lname) {
		User u = null;
		String query = "Select * from user where firstname = ? AND lastname = ?;";
		BoundStatement bound = session.prepare(query).bind(fname, lname);
		ResultSet rs = session.execute(bound);
		Row data = rs.one();
		if(data != null) {
			u = new User();
			u.setFirstName(data.getString("firstname"));
			u.setLastName(data.getString("lastname"));
			u.setRoleID(data.getInt("role"));
			u.setUserID(data.getInt("user_id"));
			u.setAvailableBal(data.getDouble("available_balance"));
			u.setDirectSupID(data.getInt("direct_sup"));
			u.setDeptHeadID(data.getInt("dept_head"));
		}
		return u;
	}

	@Override
	public void addUser(User u) throws Exception {
		String query = "Insert into user (firstname, lastname, role, user_id, available_balance, direct_sup, dept_head) values (?,?,?,?,?,?,?); ";
		SimpleStatement s = new SimpleStatementBuilder(query)
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = session.prepare(s).bind(u.getFirstName(), u.getLastName(), u.getRoleID(), u.getUserID(), u.getAvailableBal(), u.getDirectSupID(), u.getDeptHeadID());
		session.execute(bound);
	}

	@Override
	public void updateUser(User u) {
		String query = "update user set firstname = ?, lastname = ?, role = ?, available_balance = ?, direct_sup = ?, dept_head = ? where user_id = ?";
		SimpleStatement s = new SimpleStatementBuilder(query)
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = session.prepare(s).bind(u.getFirstName(), u.getLastName(), u.getRoleID(), u.getAvailableBal(), u.getDirectSupID(), u.getDeptHeadID(), u.getUserID());
		session.execute(bound);
	}

	@Override
	public User getUserByID(int userID) {
		User u = null;
		String query = "Select * from user where user_id = ?;";
		BoundStatement bound = session.prepare(query).bind(userID);
		ResultSet rs = session.execute(bound);
		Row data = rs.one();
		if(data != null) {
			u = new User();
			u.setFirstName(data.getString("firstname"));
			u.setLastName(data.getString("lastname"));
			u.setRoleID(data.getInt("role"));
			u.setUserID(data.getInt("user_id"));
			u.setAvailableBal(data.getDouble("available_balance"));
			u.setDirectSupID(data.getInt("direct_sup"));
			u.setDeptHeadID(data.getInt("dept_head"));
		}
		return u;
	}

}
