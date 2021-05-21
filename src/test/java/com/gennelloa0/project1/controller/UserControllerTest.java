package com.gennelloa0.project1.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.gennelloa0.project1.controllers.UserController;
import com.gennelloa0.project1.services.UserService;

import io.javalin.http.Context;

//@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

	@Test
	public void testUpdate403WhenNotLogged() {
		UserService ps = mock(UserService.class);

		//Whitebox.setInternalState(UserController.class, "us", us);
		Context ctx = mock(Context.class);
		when(ctx.sessionAttribute("User")).thenReturn(null);
		when(ctx.pathParam("Name")).thenReturn("test");
		UserController.update(ctx);
		verify(ctx).status(403);
	}
}
