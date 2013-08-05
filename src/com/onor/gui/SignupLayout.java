package com.onor.gui;

import com.onor.OnorApplication;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Form;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class SignupLayout extends CustomLayout {

	private Form signupForm;
	private int signupPropertyCountId;
	
	private TextField loginNameField;
	private PasswordField passwordField;
	private PasswordField confirmPasswordField;
	private TextField emailField;
	private Button signupButton;
	
	public SignupLayout(String layoutName) {
		super(layoutName);
		init();
	}
	
	private void init() {
		signupPropertyCountId = 0;
		
		signupForm = new Form();

		loginNameField = new TextField();
		passwordField = new PasswordField();
		confirmPasswordField = new PasswordField();
		emailField = new TextField();
		signupButton = new Button();
		signupButton.addListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				((OnorApplication)SignupLayout.this.getApplication()).goHome();
			}
		});
		
		signupForm.addField(++signupPropertyCountId, loginNameField);
		signupForm.addField(++signupPropertyCountId, passwordField);
		signupForm.addField(++signupPropertyCountId, confirmPasswordField);
		signupForm.addField(++signupPropertyCountId, emailField);
		signupForm.addField(++signupPropertyCountId, signupButton);

		this.addComponent(signupForm);
	}
	
}
