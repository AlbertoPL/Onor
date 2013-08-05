package com.onor;

import com.onor.gui.IndexLayout;
import com.vaadin.Application;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class OnorApplication extends Application {
	
	private Window mainWindow;
	
	private CustomLayout indexLayout;
	
	@Override
	public void init() {
		setTheme("onor");
		mainWindow = new Window("Save Money. Build Loyalty...");
		indexLayout = new IndexLayout("indexLayout");
		mainWindow.setContent(indexLayout);
		setMainWindow(mainWindow);
	}
	
	public void setLayout(Layout layout) {
		mainWindow.setContent(layout);
	}
	
	public void goHome() {
		mainWindow.setContent(indexLayout);
	}
}
