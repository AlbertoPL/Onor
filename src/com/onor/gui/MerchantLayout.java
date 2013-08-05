package com.onor.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JLabel;

import org.vaadin.peter.imagescaler.ImageScaler;
import org.vaadin.risto.stepper.IntStepper;
import org.vaadin.vaadinvisualizations.LineChart;
import org.vaadin.vaadinvisualizations.PieChart;

import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.StreamResource;
import com.vaadin.terminal.StreamResource.StreamSource;
import com.vaadin.terminal.StreamVariable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Html5File;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

@SuppressWarnings("serial")
public class MerchantLayout extends CustomLayout {
	
	//loyalty card panel stuff
	private CssLayout logoArea;
	private Panel logoPanel;
	private ImageScaler logoScaler;

	private TextField businessNameField;
	private HorizontalLayout rewardScheme;
	private IntStepper dollarPicker;
	private Label dollarEquals;
	private IntStepper pointPicker;
	private Label points;
	private RichTextArea loyaltyCardEditorArea;
	
	private HorizontalLayout buttonBar;
	private Button publish;
	private Button save;
	private Button delete;
	//end loyalty card panel stuff
	
	//loyalty card saved stuff
	private Label businessNameLabel;
	private Label dollarLabel;
	private Label pointLabel;
	private Label loyaltyCardEditorLabel;
	//end loyalty card saved stuff
	
	//charts!
	private HorizontalLayout simpleChartLayout; //TODO: MAKE IT BETTER!
	private LineChart lineChart;
	private PieChart pieChart;
	
	//transaction stuff!
	private String current = "";
    private char lastOperationRequested = 'C';

    // User interface components
    private final Label display = new Label("0.00");
	
	private List<ThemeResource> templates;
	private TabSheet merchantTabs;
	
	private Panel loyaltyPanel;
	private Panel reportingPanel;
	private Panel transactionPanel;
	private Panel settingsPanel;
	
	private HorizontalLayout templateSelector;
	private CustomLayout loyaltyCardLayout;
	private CustomLayout reportingLayout;
	private VerticalLayout transactionLayout;
	
	public MerchantLayout(String layoutName) {
		super(layoutName);
		init();
	}
	
	private void init() {
	
		loyaltyCardLayout = new CustomLayout("loyaltyCardLayout");
		
		logoArea = new CssLayout();
		logoArea.setWidth("80px");
		logoArea.setHeight("80px");
		logoScaler = new ImageScaler();

        ImageDropBox dropBox = new ImageDropBox(logoArea);
        dropBox.setSizeUndefined();
        
        logoPanel = new Panel(dropBox);
        logoPanel.setSizeUndefined();
    	logoPanel.setCaption("Drag Logo Here");
        
        businessNameField = new TextField();
        businessNameField.setCaption("Business Name");
		
        rewardScheme = new HorizontalLayout();
        dollarPicker = new IntStepper();
        dollarPicker.setValue(10);
        dollarPicker.setStepAmount(5);
        dollarEquals = new Label("  $  =  ");
        pointPicker = new IntStepper();
        pointPicker.setValue(1);
        pointPicker.setStepAmount(1);
        points = new Label("  points");
        rewardScheme.addComponent(dollarPicker);
        rewardScheme.addComponent(dollarEquals);
        rewardScheme.addComponent(pointPicker);
        rewardScheme.addComponent(points);
        rewardScheme.setCaption("Reward Scheme: ");
        
        loyaltyCardEditorArea = new RichTextArea();
        loyaltyCardEditorArea.setCaption("Describe your Loyalty Card");

        
        buttonBar = new HorizontalLayout();
        publish = new Button("Publish");
        publish.addListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if (!save.isEnabled()) {
					System.out.println("Publish!");
					try {
						FileOutputStream fos = new FileOutputStream("logo.png");
						InputStream in = ((StreamResource) logoScaler.getImage()).getStreamSource().getStream();
						byte[] buffer = new byte[1024*10];
						int len;
						while ((len = in.read(buffer)) != -1) {
						    fos.write(buffer, 0, len);
						}
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					BufferedImage logo = null;
					try {
						logo = ImageIO.read(new File("logo.png"));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					BufferedImage export = new BufferedImage (320, 480,
                            BufferedImage.TYPE_INT_RGB); //size of screen on Android phone
					Graphics2D g = export.createGraphics();
					g.setColor(Color.BLACK);
					g.setBackground(new Color(102,153,0));
			        g.clearRect(0, 0, 320, 480);
					g.drawImage(createResizedCopy(logo,80,80,true), 10,10, null);
					g.drawString(businessNameLabel.getValue().toString(), 200, 20);
					JLabel label = new JLabel("<html>" + loyaltyCardEditorLabel.getValue().toString() + "</html>");
					label.setSize(200, 120);
					BufferedImage image = new BufferedImage(
				            label.getWidth(), label.getHeight(), 
				            BufferedImage.TYPE_INT_ARGB);
					{
				        // paint the html to an image
				        Graphics gr = image.getGraphics();
				        gr.setColor(Color.BLACK);
				        label.paint(gr);
				        gr.dispose();
				    }
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
				    try {
						ImageIO.write(image, "jpg", baos);
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				    g.drawImage(image, 20, 200, null);
					
				    Iterator writers = ImageIO.getImageWritersByFormatName("png");
			        ImageWriter writer = (ImageWriter) writers.next();
			        if (writer == null) {
			            throw new RuntimeException("PNG not supported?!");
			        }

			        ImageOutputStream out;
					try {
						out = ImageIO.createImageOutputStream(
						                            new File("TestPNG.png"));
						writer.setOutput(out);
				        writer.write(export);
				        out.close(); // close flushes buffer
					} catch (IOException e) {
						e.printStackTrace();
					}
			        
					//display on the screen in a new window
					
					Window w = new Window();
					w.setCaption("Your new loyalty card!");
					w.setWidth(320, Sizeable.UNITS_PIXELS);
					w.setHeight(480, Sizeable.UNITS_PIXELS);
					Embedded embedded = new Embedded(null, new FileResource(new File("C:\\Users\\Alberto\\Documents\\Eclipse EE Indigo\\TestPNG.png"),MerchantLayout.this.getApplication()));
					embedded.setWidth(320, Sizeable.UNITS_PIXELS);
					embedded.setHeight(480, Sizeable.UNITS_PIXELS);
					w.addComponent(embedded);
					MerchantLayout.this.getApplication().getMainWindow().addWindow(w);
				}
				else {
					//TODO: Warn the user to save his or her work!
				}
			}
        	
        });
        
        save = new Button("Save");
        save.addListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if (businessNameLabel == null) {
					businessNameLabel = new Label(businessNameField);
				}
				else {
					businessNameLabel.setValue(businessNameField.getValue());
				}
				if (dollarLabel == null) {
					dollarLabel = new Label(dollarPicker);
				}
				else {
					dollarLabel.setValue(dollarPicker.getValue());
				}
				if (pointLabel == null) {
					pointLabel = new Label(pointPicker);
				}
				else {
					pointLabel.setValue(pointPicker.getValue());
				}
				if (loyaltyCardEditorLabel == null) {
					loyaltyCardEditorLabel = new Label(loyaltyCardEditorArea);
					loyaltyCardEditorLabel.setContentMode(Label.CONTENT_XHTML);
				}
				else {
					loyaltyCardEditorLabel.setValue(loyaltyCardEditorArea.getValue());
				}
				loyaltyCardLayout.removeAllComponents();
				rewardScheme.removeAllComponents();
				Label rewardSchemeLabel = new Label(dollarLabel.getValue().toString() + dollarEquals.getValue().toString() + pointPicker.getValue().toString() + points.getValue().toString());
				rewardScheme.addComponent(rewardSchemeLabel);
				
		        loyaltyCardLayout.addComponent(logoPanel, "logo");
		        loyaltyCardLayout.addComponent(businessNameLabel, "name");
		        loyaltyCardLayout.addComponent(rewardScheme, "scheme");
		        loyaltyCardLayout.addComponent(loyaltyCardEditorLabel, "description");		
		        loyaltyCardLayout.addComponent(buttonBar, "buttonBar");
		        save.setEnabled(false);
			}
        	
        });
        
        delete = new Button("Delete");
        buttonBar.addComponent(publish);
        buttonBar.addComponent(save);
        buttonBar.addComponent(delete);
        
        loyaltyPanel = new Panel();
        
        loyaltyCardLayout.addComponent(logoPanel, "logo");
        loyaltyCardLayout.addComponent(businessNameField, "name");
        loyaltyCardLayout.addComponent(rewardScheme, "scheme");
        loyaltyCardLayout.addComponent(loyaltyCardEditorArea, "description");		
        
        loyaltyPanel.addComponent(loyaltyCardLayout);
        loyaltyCardLayout.addComponent(buttonBar, "buttonBar");
        
        
        reportingLayout= new CustomLayout("reportingLayout");
        
        lineChart = new LineChart();
        lineChart.setOption("legend", "bottom");
        lineChart.setOption("title", "Loyal Customers/Sales");
       
        lineChart.addXAxisLabel("Year");
        lineChart.addLine("Customers");
        lineChart.addLine("Sales");
        lineChart.add("2004", new double[]{100,200});
        lineChart.add("2005", new double[]{75,100});
        lineChart.add("2006", new double[]{32,234});
        lineChart.add("2007", new double[]{25,2534});
        lineChart.add("2008", new double[]{2343,12});
        lineChart.setSizeUndefined();


        pieChart = new PieChart();
        
        pieChart.setSizeUndefined();
        
        /*pc.addListener(new PieChart.SelectionListener() {

                public void selectionChanged(List<String> selectedItems) {
                        if (selectedItems.get(0).equalsIgnoreCase("")){
                                
                        
                           getMainWindow().showNotification("Node : " + selectedNode + " deselected.");
                        } else {
                                getMainWindow().showNotification("Node : " + selectedItems.get(0) + " selected.");
                                selectedNode = selectedItems.get(0);
                                
                        }
                }

        });*/
        
        pieChart.add("New Customers", 7);
        pieChart.add("Loyal Customers", 3);
        pieChart.add("Other", 1);
        pieChart.add("Walk In/Out", 7);
        pieChart.setOption("width", 400);
        pieChart.setOption("height", 400);
        pieChart.setOption("is3D", true);
        
        reportingLayout.addComponent(lineChart, "lineChart");
        reportingLayout.addComponent(pieChart, "pieChart");
        reportingLayout.setHeight(400, UNITS_PIXELS);
        
        reportingPanel = new Panel();
        
        reportingPanel.addComponent(reportingLayout);
        
        transactionLayout = new VerticalLayout();
        
        TextField phoneNumberId = new TextField();
        
        final GridLayout layout = new GridLayout(3, 5);

        // Create a result label that over all 4 columns in the first row
        layout.addComponent(display, 0, 0, 2, 0);

        // The operations for the calculator in the order they appear on the
        // screen (left to right, top to bottom)
        String[] operations = new String[] { "1", "2", "3", "4", "5", "6",
                "7", "8", "9", ".", "0", "C"};

        for (String caption : operations) {

            // Create a button and use this application for event handling
            Button button = new Button(caption);
            button.addListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					// Get the button that was clicked
			        Button button = event.getButton();

			        // Get the requested operation from the button caption
			        char requestedOperation = button.getCaption().charAt(0);

			        // Calculate the new value
			        String newValue = calculate(requestedOperation);
			        
			        //fix formatting at the end
			        if (!newValue.contains(".")) {
			        	newValue += ".00";
			        }
			        if (newValue.startsWith(".")) {
			        	newValue = "0" + newValue;
			        }
			        while (newValue.contains(".") && newValue.substring(newValue.indexOf('.')).length() < 3) {
			        	newValue += "0";
			        }
			        // Update the result label with the new value
			        display.setValue(newValue);
				}
			});

            // Add the button to our main layout
            layout.addComponent(button);
        }
        
        Button sale = new Button("Sale");
        Button storeCard = new Button("Store Card");
        HorizontalLayout buttonbar = new HorizontalLayout();
        buttonbar.addComponent(sale);
        buttonbar.addComponent(storeCard);
        
        transactionLayout.addComponent(phoneNumberId);
        transactionLayout.addComponent(layout);
        transactionLayout.addComponent(buttonbar);
        
        transactionPanel = new Panel();
        transactionPanel.setContent(transactionLayout);
        
		settingsPanel = new Panel();
		
		templates = new ArrayList<ThemeResource>();
		templateSelector = new HorizontalLayout();
		for (ThemeResource template: templates) {
			//TODO: add Theme Resources and display them in a bar under the loyaltyCardEditorArea 
			//templateSelector.addComponent(new image viewer);
		}
		
		merchantTabs = new TabSheet();
		
		reportingPanel.setCaption("Dashboard");
		reportingPanel.setScrollable(true);
		transactionPanel.setCaption("Transaction Terminal");
		loyaltyPanel.setCaption("Create a Card");
		settingsPanel.setCaption("Profile and Settings");

		merchantTabs.addTab(reportingPanel);
		merchantTabs.addTab(transactionPanel);
		merchantTabs.addTab(loyaltyPanel);
		merchantTabs.addTab(settingsPanel);

		this.addComponent(merchantTabs, "content");
		//this.addComponent(buttonBar,"buttonBar");
	}
	
	private BufferedImage createResizedCopy(Image originalImage, 
            int scaledWidth, int scaledHeight, 
            boolean preserveAlpha) {
	    System.out.println("resizing...");
	    int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
	    BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
	    Graphics2D g = scaledBI.createGraphics();
	    if (preserveAlpha) {
	            g.setComposite(AlphaComposite.Src);
	    }
	    g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null); 
	    g.dispose();
	    return scaledBI;
	}

	
	private class ImageDropBox extends DragAndDropWrapper implements DropHandler {
		private static final long FILE_SIZE_LIMIT = 2 * 1024 * 1024; // 2MB
		
		public ImageDropBox(Component root) {
		    super(root);
		    setDropHandler(this);
		}
		
		public void drop(DragAndDropEvent dropEvent) {
		
		    // expecting this to be an html5 drag
		    WrapperTransferable tr = (WrapperTransferable) dropEvent
		            .getTransferable();
		    Html5File[] files = tr.getFiles();
		    if (files != null) {
		        for (final Html5File html5File : files) {
		            final String fileName = html5File.getFileName();
		
		            if (html5File.getFileSize() > FILE_SIZE_LIMIT) {
		                getWindow()
		                        .showNotification(
		                                "File rejected. Max 2Mb files are accepted by Onor",
		                                Notification.TYPE_WARNING_MESSAGE);
		            } else {
		
		                final ByteArrayOutputStream bas = new ByteArrayOutputStream();
		                StreamVariable streamVariable = new StreamVariable() {
		
		                    public OutputStream getOutputStream() {
		                        return bas;
		                    }
		
		                    public boolean listenProgress() {
		                        return false;
		                    }
		
		                    public void onProgress(StreamingProgressEvent event) {
		                    }
		
		                    public void streamingStarted(
		                            StreamingStartEvent event) {
		                    }
		
		                    public void streamingFinished(
		                            StreamingEndEvent event) {
		                        //progress.setVisible(false);
		                        showFile(fileName, html5File.getType(), bas);
		                    }
		
		                    public void streamingFailed(
		                            StreamingErrorEvent event) {
		                        //progress.setVisible(false);
		                    }
		
		                    public boolean isInterrupted() {
		                        return false;
		                    }
		                };
		                html5File.setStreamVariable(streamVariable);
		                logoPanel.setCaption("");
		                //progress.setVisible(true);
		            }
		        }
		
		    } else {
		        String text = tr.getText();
		        if (text != null) {
		            showText(text);
		        }
		    }
		}
		
		private void showText(String text) {
		    showComponent(new Label(text), "Wrapped text content");
		}
		
		private void showFile(String name, String type,
		        final ByteArrayOutputStream bas) {
		    // resource for serving the file contents
		    StreamSource streamSource = new StreamSource() {
		        public InputStream getStream() {
		            if (bas != null) {
		                byte[] byteArray = bas.toByteArray();
		                return new ByteArrayInputStream(byteArray);
		            }
		            return null;
		        }
		    };
		    StreamResource resource = new StreamResource(streamSource, name,
		            getApplication());
		
		    // show the file contents - images only for now
		    logoScaler.setImage(resource, 80, 80);
		    logoScaler.setSizeFull();
		    
		    //Embedded embedded = new Embedded();
		    //embedded.setSource(resource);
		    showComponent(logoScaler, name);
		}
		
		private void showComponent(Component c, String name) {
		    
			MerchantLayout.this.logoArea.addComponent(c);
		}
		
		public AcceptCriterion getAcceptCriterion() {
		    return AcceptAll.get();
		}
	}

	private String calculate(char requestedOperation) {
        if ('0' <= requestedOperation && requestedOperation <= '9') {
        	if (!current.contains(".") || current.contains(".") && current.substring(current.indexOf('.')).length() < 3) {
        		current += requestedOperation;
        	}
        }
        
        if (requestedOperation == 'C') {
            current = "";
        }
        else if (requestedOperation == '.' && !current.contains(".")) {
        	current += '.';
        }

        return current;
    }

}
