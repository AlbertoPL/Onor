package com.onor.gui;

import com.onor.OnorApplication;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Form;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class IndexLayout extends CustomLayout {
	
	private TextField merchantLoginName;
	private PasswordField merchantPassword;
	private Button merchantSignupButton;
	private Button merchantLoginButton;
	private TextField consumerLoginName;
	private PasswordField consumerPassword;
	private Button consumerSignupButton;
	private Button consumerLoginButton;
	
	private Form merchantForm;
	private Form consumerForm;
	
	private CustomLayout merchantLayout;
	private CustomLayout consumerLayout;
	private CustomLayout signupLayout;
	
	private int merchantPropertyIdCount;
	private int consumerPropertyIdCount;
	
	public IndexLayout(String layoutName) {
		super(layoutName); //TODO: remember to pass in name when using CustomLayout!
		init();
		merchantLayout = null;
		consumerLayout = null;
		signupLayout = null;
		merchantPropertyIdCount = 0;
		consumerPropertyIdCount = 0;
	}
	
	private void init() {
		merchantForm = new Form();
		merchantForm.setCaption("Merchant Login");
		
		merchantLoginName = new TextField("Merchant Name:");
		merchantPassword = new PasswordField("Password:");
		merchantSignupButton = new Button("Signup");
		merchantSignupButton.addListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		merchantLoginButton = new Button("Login");
		
		merchantLoginButton.addListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if (merchantLayout == null) {
					merchantLayout = new MerchantLayout("merchantLayout");
				}
				((OnorApplication)IndexLayout.this.getApplication()).setLayout(merchantLayout);
			}
			
		});
		
		merchantForm.addField(++merchantPropertyIdCount, merchantLoginName);
		merchantForm.addField(++merchantPropertyIdCount, merchantPassword);
		merchantForm.addField(++merchantPropertyIdCount, merchantLoginButton);

		consumerForm = new Form();
		consumerForm.setCaption("Consumer Login");
		
		consumerLoginName = new TextField("User Name:");
		consumerPassword = new PasswordField("Password:");
		consumerSignupButton = new Button("Signup");
		consumerSignupButton.addListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				if (signupLayout == null) {
					signupLayout = new SignupLayout("signupLayout");
				}
				((OnorApplication)IndexLayout.this.getApplication()).setLayout(signupLayout);
			}
		});
		
		consumerLoginButton = new Button("Login");
		consumerLoginButton.addListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		consumerForm.addField(++consumerPropertyIdCount, consumerLoginName);
		consumerForm.addField(++consumerPropertyIdCount, consumerPassword);
		consumerForm.addField(++consumerPropertyIdCount, consumerLoginButton);
		
		this.addComponent(merchantForm, "merchantLogin");
		this.addComponent(consumerForm, "consumerLogin");
	}
	
}
