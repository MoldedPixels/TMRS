package com.gennelloa0.project1.data.cass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import com.gennelloa0.project1.beans.Forms;
import com.gennelloa0.project1.data.FormDao;
import com.gennelloa0.project1.factory.Log;
import com.gennelloa0.project1.utils.CassandraUtil;

@Log
public class FormDaoCass implements FormDao {
	private CqlSession session = CassandraUtil.getInstance().getSession();
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	@Override
	public void submitForm(Forms f) {
		Date date = new Date();
		String query = "Insert into forms " + "(assigned_to, " + "bco_status, " + "bco_status_date, "
				+ "denial_reason, " + "denied_by, " + "dept_head, " + "dh_status, " + "dh_status_date, "
				+ "direct_Sup, " + "ds_status, " + "ds_status_date, " + "event_date, " + "event_type, " + "form_id, "
				+ "form_status, " + "grade_submitted," + "grading_format, " + "justification, " + "last_updated_by, "
				+ "last_updated_date, " + "request_amount," + "requestor, " + "request_date) "
				+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); ";
		SimpleStatement s = new SimpleStatementBuilder(query).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM)
				.build();
		BoundStatement bound = session.prepare(s).bind(f.getAssignedToID(), f.getBcoStatus(), f.getBcoDate(),
				f.getDenialReason(), f.getDeniedBy(), f.getDeptHeadID(), f.getDeptHeadStatus(), f.getDeptHeadDate(),
				f.getDirectSupID(), f.getDirectSupStatus(), f.getDirectSupDate(), f.getEventDate(), f.getEventType(),
				f.getFormID(), f.getFormStatus(), f.isGradingApproved(), f.getGradingFormat(), f.getJustification(),
				f.getLast_updator(), f.getLast_updated(), f.getRequestedAmount(), f.getRequestedBy(),
				formatter.format(date));
		session.execute(bound);
	}

	@Override
	public Forms getFormsByID(int formID) {
		Forms f = new Forms();
		String query = "select * from forms where form_id = ?";
		SimpleStatement s = new SimpleStatementBuilder(query).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM)
				.build();
		BoundStatement bound = session.prepare(s).bind(formID);
		ResultSet rs = session.execute(bound);
		Row data = rs.one();
		if (data != null) {
			f.setAssignedToID(data.getInt("assigned_to"));
			f.setBcoDate(data.getString("bco_status_date"));
			f.setBcoStatus(data.getInt("bco_status"));
			f.setDenialReason(data.getString("denial_reason"));
			f.setDeniedBy(data.getInt("denied_by"));
			f.setDeptHeadDate(data.getString("dh_status_date"));
			f.setDeptHeadID(data.getInt("dept_head"));
			f.setDeptHeadStatus(data.getInt("dh_status"));
			f.setDirectSupDate(data.getString("ds_status_date"));
			f.setDirectSupID(data.getInt("direct_sup"));
			f.setDirectSupStatus(data.getInt("ds_status"));
			f.setEventDate(data.getString("event_date"));
			f.setEventType(data.getInt("event_type"));
			f.setFormID(data.getInt("form_id"));
			f.setFormStatus(data.getInt("form_id"));
			f.setGradingFormat(data.getInt("grading_format"));
			f.setJustification(data.getString("justification"));
			f.setLast_updated(data.getString("last_updated_date"));
			f.setLast_updator(data.getInt("last_updated_by"));
			f.setRequestedAmount(data.getDouble("request_amount"));
			f.setRequestedBy(data.getInt("requestor"));
			f.setRequestedDate(data.getString("request_date"));
		}
		return f;
	}

	@Override
	public void updateStatus(int formID, int status, int userID, String reason) {
		Forms f = this.getFormsByID(formID);
		String qpart = "";
		if (userID == f.getDeptHeadID()) {
			qpart = "dh_status = ?, dh_status_date = ?";
		} else if (userID == f.getDirectSupID()) {
			qpart = "ds_status = ?, ds_status_date = ?";

		} else if (userID == f.getAssignedToID()) {
			qpart = "bco_status = ?, bco_status_date = ?";
		} else if (status == 5) // denied
		{
			qpart += "denied_by = ?, denial_reason = ?, form_status = ?";
		} else {
			return;
		}
		Date date = new Date();
		String query = "update forms set last_updated_by = ?, last_updated_date = ?," + qpart + " where form_id = ?";
		SimpleStatement s = new SimpleStatementBuilder(query).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM)
				.build();
		BoundStatement bound;
		if (status == 5) {
			bound = session.prepare(s).bind(userID, formatter.format(date), status, formatter.format(date), userID,
					reason, status, formID);
		} else {
			bound = session.prepare(s).bind(userID, formatter.format(date), status, formatter.format(date), formID);
		}

		session.execute(bound);

		// update is done, check to see if form_status needs to be updated
		f = this.getFormsByID(formID);

		if (f.getBcoStatus() == f.getDeptHeadStatus() && f.getBcoStatus() == f.getDirectSupStatus()) {
			String query2 = "update forms set form_status = ? where form_id = ?";
			SimpleStatement s2 = new SimpleStatementBuilder(query2)
					.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
			BoundStatement bound2 = session.prepare(s2).bind(f.getBcoStatus(), formID);
			session.execute(bound2);
		}
	}

	@Override
	public void updateAmount(int formID, int newAmount, int userID, String justification) {
		Forms f = this.getFormsByID(formID);
		Date date = new Date();
		if (userID == f.getDeptHeadID() || userID == f.getDirectSupID() || userID == f.getAssignedToID()) {
			String query = "update forms set last_updated_by = ?, last_updated_date = ?, request_amount = ?, justification = ? where form_id = ?";
			SimpleStatement s = new SimpleStatementBuilder(query)
					.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
			BoundStatement bound = session.prepare(s).bind(userID, formatter.format(date), newAmount, justification,
					formID);
			session.execute(bound);
		} else {
			return;
		}

	}

	@Override
	public List<Forms> getForms() {
		List<Forms> forms = new ArrayList<Forms>();

		String query = "select * from forms";
		ResultSet rs = session.execute(query);

		rs.forEach(data -> {
			Forms f = new Forms();
			f.setAssignedToID(data.getInt("assigned_to"));
			f.setBcoDate(data.getString("bco_status_date"));
			f.setBcoStatus(data.getInt("bco_status"));
			f.setDenialReason(data.getString("denial_reason"));
			f.setDeniedBy(data.getInt("denied_by"));
			f.setDeptHeadDate(data.getString("dh_status_date"));
			f.setDeptHeadID(data.getInt("dept_head"));
			f.setDeptHeadStatus(data.getInt("dh_status"));
			f.setDirectSupDate(data.getString("ds_status_date"));
			f.setDirectSupID(data.getInt("direct_sup"));
			f.setDirectSupStatus(data.getInt("ds_status"));
			f.setEventDate(data.getString("event_date"));
			f.setEventType(data.getInt("event_type"));
			f.setFormID(data.getInt("form_id"));
			f.setFormStatus(data.getInt("form_id"));
			f.setGradingFormat(data.getInt("grading_format"));
			f.setJustification(data.getString("justification"));
			f.setLast_updated(data.getString("last_updated_date"));
			f.setLast_updator(data.getInt("last_updated_by"));
			f.setRequestedAmount(data.getInt("request_amount"));
			f.setRequestedBy(data.getInt("requestor"));
			f.setRequestedDate(data.getString("requested_date"));
			forms.add(f);
		});

		return forms;
	}

	@Override
	public boolean getGrade(int formID) {
		String query = "select grade_submitted from forms where form_id = ?";
		SimpleStatement s = new SimpleStatementBuilder(query).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM)
				.build();
		BoundStatement bound = session.prepare(s).bind(formID);
		ResultSet rs = session.execute(bound);
		Row data = rs.one();

		return data.getBoolean("grade_submitted");
	}

	@Override
	public void approveGrade(int formID) {
		String query2 = "update forms set grade_submitted = ? where form_id = ?";
		SimpleStatement s2 = new SimpleStatementBuilder(query2)
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound2 = session.prepare(s2).bind(true, formID);
		session.execute(bound2);

	}

	@Override
	public int assignBco(Forms f) {
		String query = "select user_id from user where user_id > 0 AND role = 2 ALLOW FILTERING"; // role == Benco
		SimpleStatement s = new SimpleStatementBuilder(query).setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM)
				.build();
		BoundStatement bound = session.prepare(s).bind();
		ResultSet rs = session.execute(bound);
		Row data = rs.one();
		return data.getInt("user_id");
	}

}
