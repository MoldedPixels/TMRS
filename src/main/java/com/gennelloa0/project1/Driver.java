package com.gennelloa0.project1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import com.gennelloa0.project1.controllers.UserController;
import com.gennelloa0.project1.controllers.HistoryController;
import com.gennelloa0.project1.controllers.AttachmentsController;
import com.gennelloa0.project1.controllers.FormController;
import com.gennelloa0.project1.utils.CassandraUtil;

import io.javalin.Javalin;

public class Driver {
	private static final Logger log = LogManager.getLogger(Driver.class);

	public static void main(String[] args) {
		// Trace the flow of an application
		log.trace("Begin the application");
		javalin();
		dbtest(); // created my keyspace
		log.trace("Checking for UserTable");
		userTable(); // created usertable
		log.trace("Checking for FormTable");
		formsTable(); //created formsTable
		log.trace("Checking for LogsTable");
		logsTable(); //created logsTable
		log.trace("Checking for AttachmentsTable");
		attachmentsTable(); //created attachmentsTable
		//log.trace("Adding to Roles");
		//addRoles();
		//log.trace("Adding to Status");
		//addStatus();
	}

	private static void addRoles() {
		String query = "Insert into roles (role_id, role_name) values (?,?); ";
		SimpleStatement s = new SimpleStatementBuilder(query)
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = CassandraUtil.getInstance().getSession().prepare(s).bind(3, "ADMIN");
		CassandraUtil.getInstance().getSession().execute(bound);
	}
	
	private static void addStatus() {
		String query = "Insert into status (status_id, status_name) values (?,?); ";
		SimpleStatement s = new SimpleStatementBuilder(query)
				.setConsistencyLevel(DefaultConsistencyLevel.LOCAL_QUORUM).build();
		BoundStatement bound = CassandraUtil.getInstance().getSession().prepare(s).bind(5, "Denied");
		CassandraUtil.getInstance().getSession().execute(bound);
	}

	public static void userTable() {
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append("User (")
				.append("user_id int, firstname text, lastname text,")
				.append("role int,")
				.append("direct_sup int, dept_head int, available_balance double, primary key(user_id));");
		CassandraUtil.getInstance().getSession().execute(sb.toString());
	}
	
	public static void formsTable() {
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append("Forms (")
				.append("form_id int, assigned_to int, form_status int, date_submitted text, event_date text,")
				.append("direct_sup int, dept_head int, requestor int, request_amount double,")
				.append("ds_status int, ds_status_date text, dh_status int, dh_status_date text,")
				.append("bco_status int, bco_status_date text, last_updated_date text,")
				.append("last_updated_by int, grading_format int, event_type int, justification text,")
				.append("request_date text, denial_reason text, denied_by int, grade_submitted boolean, primary key(form_id));");
		CassandraUtil.getInstance().getSession().execute(sb.toString());
	}
	
	public static void attachmentsTable() {
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append("Attachments (")
				.append("attachment_id int, uploaded_by int, form_id int, filetype text, primary key (filetype, form_id));");
		CassandraUtil.getInstance().getSession().execute(sb.toString());
	}
	
	public static void logsTable() {
		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append("Logs (")
				.append("user_id int, form_id int, timestamp text, message text, primary key (form_id, user_id, timestamp));");
		CassandraUtil.getInstance().getSession().execute(sb.toString());
	}

	public static void dbtest() {

		StringBuilder sb = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ").append("Project_1 with replication = {")
				.append("'class':'SimpleStrategy','replication_factor':1};");
		DriverConfigLoader loader = DriverConfigLoader.fromClasspath("application.conf");
		CqlSession.builder().withConfigLoader(loader).build().execute(sb.toString());

	}

	private static void javalin() {
		Javalin app = Javalin.create().start(8080);

		// Map controller functions to specific methods and urls

		// Forms Controller
		app.get("/forms", FormController::getForms);
		app.put("/forms/submit", FormController::submitForm);
		app.get("/forms/update/status", FormController::updateStatus);
		app.get("/forms/update/amount", FormController::updateAmount);
		app.get("/forms/view/:fid", FormController::getForm);

		// User Controller
		app.get("/users", UserController::getUsers);
		app.put("/users/register", UserController::register);
		app.post("/users/login", UserController::login);
		app.delete("/users/logout", UserController::logout);
		app.put("/users/update/:uid", UserController::update);

		// History Controller
		app.get("/history/user", HistoryController::getHistoryByUser);
		app.get("/history/form", HistoryController::getHistoryByForm);
		app.get("/history/date", HistoryController::getHistoryByDate);
		
		// Attachments Controller
		app.put("/attachments/:formID/upload/:grade/:filetype", AttachmentsController::upload);
		app.get("/attachments/:formID/download/:filetype", AttachmentsController::download);

		CassandraUtil.getInstance();
	}
}
