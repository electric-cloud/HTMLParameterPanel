
// HTMLParameterPanel.java --
//
// HTMLParameterPanel.java is part of the HTMLParamPanel plugin.
//
// Copyright (c) 2014 Electric Cloud, Inc.
// All rights reserved.
//

package ecplugins.HTMLParameterPanel.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.electriccloud.commander.client.domain.Order;
import com.electriccloud.commander.client.ChainedCallback;
import com.electriccloud.commander.client.domain.ActualParameter;
import com.electriccloud.commander.client.domain.FormalParameter;
import com.electriccloud.commander.client.domain.ObjectType;
import com.electriccloud.commander.client.domain.Property;
import com.electriccloud.commander.client.requests.FindObjectsRequest;
import com.electriccloud.commander.client.requests.GetPropertyRequest;
import com.electriccloud.commander.client.responses.DefaultPropertyCallback;
import com.electriccloud.commander.client.responses.PropertyCallback;
import com.electriccloud.commander.gwt.client.ComponentBase;
import com.electriccloud.commander.gwt.client.ui.ParameterPanel;
import com.electriccloud.commander.gwt.client.ui.ParameterPanelProvider;
import com.electriccloud.commander.client.responses.CommanderError;
import com.electriccloud.commander.client.responses.FindObjectsResponse;
import com.electriccloud.commander.client.responses.FindObjectsResponseCallback;
import com.electriccloud.commander.client.requests.FindObjectsFilter.IsNullFilter;
import com.electriccloud.commander.client.domain.Project;
/**
 * Basic component that is meant to be cloned and then customized to perform a
 * real function.
 */
public class HTMLParameterPanel
    extends ComponentBase
    implements ParameterPanel, ParameterPanelProvider {

	private Map<String, FormalParameter> m_formalParams; // Formal parameters
	private Map<String, String> m_paramTypes;			// Type of the parameters (entry, checkbox, ..)
	private Map<String, String> m_paramDefaultValues;	// Default value of the parameters
	private Map<String, String> m_paramDescriptions;	// Descriptions of the parameters
	private Map<String, Boolean> m_paramRequireds;		// parameter is required
	private Map<String, Widget> m_paramWidgets;			// Widget associated to the parameters
	private String projectName;
	private String procedureName;
	
    //~ Methods ----------------------------------------------------------------
	HTMLPanel HTMLpanel;
	
    /**
     * This function is called by SDK infrastructure to initialize the UI parts of
     * this component.
     *
     * @return                 A widget that the infrastructure should place in
     *                         the UI; usually a panel.
     */
    @Override public Widget doInit()
    {
    	/*
    	 * Create the HTML panel that will get the code
    	 */
    	HTMLpanel = new HTMLPanel("<div id='root'><h1>Loading, please wait...</h1></div>");
    	//HTMLpanel.setSize("400px", "400px");
    	HTMLpanel.addStyleName("HTML panel");
 
        return HTMLpanel;
    }

	 /**
     * Performs validation of user supplied data before submitting the form.
     * This function is called after the user hits submit.</p>
     * @return  true if checks succeed, false otherwise
     */
	@Override public boolean validate() {
		// TODO Auto-generated method stub
   	 	// Loop over each parameter
   	 	Iterator<String> keySetIterator = m_paramTypes.keySet().iterator();
   	 	while(keySetIterator.hasNext()) {
   	 		String key = keySetIterator.next();
   	 		String value=getWidgetValue(key);
   	 		
   	 		if (m_paramRequireds.get(key) && value.equals("")) {
   	 			alert("Parameter " + key + " is required abut does not have a value");
   	 			return false;
   	 		}
   	 	}
         return true;

	}

	
	@Override public ParameterPanel getParameterPanel() {
		// TODO Auto-generated method stub
		return this;
	}


    /**
     * Gets the values of the parameters that should map 1-to-1 to the formal
     * parameters on the object being called. Transform user input into a map of
     * parameter names and values.
     *
     * <p>This function is called after the user hits submit and validation has
     * succeeded.</p>
     *
     * @return  The values of the parameters that should map 1-to-1 to the
     *          formal parameters on the object being called.
     */
    @Override public Map<String, String> getValues() {
        Map<String, String> values = new HashMap<String, String>();

   	 	// Loop over each parameter
   	 	Iterator<String> keySetIterator = m_paramTypes.keySet().iterator();
   	 	while(keySetIterator.hasNext()) {
   	 		String key = keySetIterator.next();
   	 		values.put(key, getWidgetValue(key));
   	 	}
         return values;
    }	
    

	@Override public void setActualParameters(Collection<ActualParameter> actualParameters) {
		// TODO Auto-generated method stub
		
	}

    /**
     * Push form parameters into the panel implementation.
     * This is used when creating a new object and showing default values.
     *
     * @param  formalParameters  Formal parameters on the target object.
     */
    @Override public void setFormalParameters(Collection<FormalParameter> formalParameters) {
        m_formalParams       = new HashMap<String, FormalParameter>();
        m_paramTypes         = new HashMap<String, String>();
        m_paramDefaultValues = new HashMap<String, String>();
        m_paramDescriptions  = new HashMap<String, String>();
        m_paramWidgets       = new HashMap<String, Widget>();
        m_paramRequireds	 = new HashMap<String, Boolean>();
        /*
         * Extract project and procedure from the URL
         */
        String URL=Window.Location.getPath();
        String pattern = "/commander/link/runProcedure/projects/(\\w+)/procedures/(\\w+)";
        projectName=URL.replaceAll(pattern, "$1"); 
        procedureName=URL.replaceAll(pattern, "$2");
        //getLog().debug("Project: " + projectName + "\n");
        //getLog().debug("Procedure: " + procedureName + "\n");
       
        // getLog().debug("Formal parameters started\n");
        for (FormalParameter formalParameter : formalParameters) {
        	String pName=formalParameter.getName();
            m_formalParams.put(pName, formalParameter);
            m_paramTypes.put(pName, formalParameter.getType());
            m_paramDefaultValues.put(pName, formalParameter.getDefaultValue() );
            m_paramDescriptions.put(pName, formalParameter.getDescription());
            m_paramRequireds.put(pName, formalParameter.isRequired());
           
            //getLog().debug("  " + pName + ": " + formalParameter.getType() + " - " 
            //		+ formalParameter.getDefaultValue() + "\n");
        }
        // getLog().debug("Formal parameters processed\n");
        
       	/*
    	 * Let's get the ec_parameterHTML property we have to render
    	 */
    	 getProperties();

    }

    /**
     * Function to get the ec_parameterHTML
     * and replace each parameter placeholder by a widget
     */
	private void getProperties() {
		/**
		 * Callback function to handle the property value
		 */
		PropertyCallback propertyRequestCallback = new DefaultPropertyCallback(this) {
	        @SuppressWarnings("deprecation")
			@Override public void handleResponse(Property property) {
	        	 // getLog().debug("Property retrieved" + property.getValue());
	        	 HTMLpanel.getElementById("root").setInnerHTML(property.getValue());
	        	
	        	 // getLog().debug("Processing the list of formal parameters for replacement\n");
	        	 
	        	 // Loop over each parameter
	        	 Iterator<String> keySetIterator = m_paramTypes.keySet().iterator();
	        	 while(keySetIterator.hasNext()) {
	        	   String key = keySetIterator.next();
	        	   Widget widget=createParamWidget(key);
	        	   m_paramWidgets.put(key, widget);
	        	   Element HTMLelement=HTMLpanel.getElementById(key);
	        	   if (HTMLelement != null) {
	        		   HTMLpanel.addAndReplaceElement(widget, HTMLpanel.getElementById(key));
	        	   } else {
	        		   
	        		   alert("Parameter " + key + 
	        				   			" does not have an entry in ec_parameterHTML\n");
	        	   }
	        	 }
	         }
		};

		GetPropertyRequest getProperty = getRequestFactory().createGetPropertyRequest();
		getProperty.setCallback(propertyRequestCallback);
		getProperty.setPropertyName("/projects/"+projectName+
									"/procedures/"+procedureName+"/ec_parameterHTML");

		doRequest(new ChainedCallback() {
			@Override public void onComplete() {
	           	  	// All done!             
			}
	    }, getProperty);    
	}
	
	
    /**
     * Function to get the choices of a menu
     */
	private void getMenuChoices(final String paramName, final ListBox LB) {
		/**
		 * Callback function to handle the property value
		 */
		PropertyCallback propertyRequestCallback = new DefaultPropertyCallback(this) {
			@Override public void handleResponse(Property property) {
	        	String choiceList=property.getValue();
	        	String[] choices = choiceList.split("\\|");
	        	Integer index=0;
	        	Integer match=-1;
	        	String defaultValue=m_paramDefaultValues.get(paramName);
	        	// getLog().debug("Default Value " + defaultValue + "\n");
	        	for (String choice: choices) {
	        		if (defaultValue.equals(choice)) {
	        			match=index;
	        			// getLog().debug("Default Selected " + index + "\n");
	        		}
	        		// getLog().debug("Adding menu choice " + choice + "\n");
	        		LB.addItem(choice);
	        		index ++;
	        	}
	        	if (match >=0) {
	        		LB.setSelectedIndex(match);
	        	}
	        }
		};

		GetPropertyRequest getProperty = getRequestFactory().createGetPropertyRequest();
		getProperty.setCallback(propertyRequestCallback);
		getProperty.setPropertyName("/projects/" + projectName +
									"/procedures/" + procedureName +
									"/ec_customEditorData/parameters/" + paramName +
									"/options/list");

		doRequest(new ChainedCallback() {
			@Override public void onComplete() {
	           	  	// All done!             
			}
	    }, getProperty);    
	}

	
    /**
     * Function to get the choices of a project
     */
	private void getProjectChoices(final String paramName, final ListBox LB) {
			
        FindObjectsResponseCallback projectRequestCallback = new FindObjectsResponseCallback() {
            @Override public void handleError(CommanderError error) {
                return; 	// There was an error, list will be empty
            }
            
			@Override public void handleResponse(FindObjectsResponse response) {
	        	List<Project> prjList=response.getProjects();
	        	Integer index=0;
	        	Integer match=-1;
	        	String defaultValue=m_paramDefaultValues.get(paramName);
	        	// getLog().debug("Default Value " + defaultValue + "\n");
	        	for (Project prj: prjList) {
	        		String prjName=prj.getName();
	        		if (defaultValue.equals(prjName)) {
	        			match=index;
	        			// getLog().debug("Default Selected " + index + "\n");
	        		}
	        		// getLog().debug("Adding menu choice " + prjName + "\n");
	        		LB.addItem(prjName);
	        		index ++;
	        	}
	        	if (match >=0) {
	        		LB.setSelectedIndex(match);
	        	}
	        }
		};
		
    	/**
		 * Callback function to handle the property value
		 */
		FindObjectsRequest  findProjects = this.getRequestFactory()
                .createFindObjectsRequest(ObjectType.project);
		// Set maximum number of results returned by the request
        findProjects.setMaxIds(100);
        // Filter results: no plugin
        IsNullFilter filter=new IsNullFilter("pluginName");
        findProjects.addFilter(filter);
        findProjects.addSort("projectName", Order.ascending);
 
		// Set the callback
        findProjects.setCallback(projectRequestCallback);
		
        doRequest(new ChainedCallback() {
			@Override public void onComplete() {
	           	  	// All done!             
			}
	    }, findProjects);    

	}
	
	
	/**
     * Function to get the choices of a radio button
     */
	private void getRadioChoices(final String paramName, final HorizontalPanel HP) {
		/**
		 * Callback function to handle the property value
		 */
		PropertyCallback propertyRequestCallback = new DefaultPropertyCallback(this) {
			@Override public void handleResponse(Property property) {
	        	String choiceList=property.getValue();
	        	String[] choices = choiceList.split("\\|");
	        	String defaultValue=m_paramDefaultValues.get(paramName);
	        	// getLog().debug("Default Value " + defaultValue + "\n");
	        	
	        	for (String choice: choices) {
	        		// getLog().debug("Adding menu choice " + choice + "\n");
	        		// creating a radio button for each choice in the group
	        		// named after the parameter
	        		final RadioButton RB=new RadioButton(paramName, choice);
	        		HP.add(RB);
	        		if (defaultValue.equals(choice)) {
	        			RB.setValue(true);
	        		}
	        	}
	        }
		};

		GetPropertyRequest getProperty = getRequestFactory().createGetPropertyRequest();
		getProperty.setCallback(propertyRequestCallback);
		getProperty.setPropertyName("/projects/" + projectName +
									"/procedures/" + procedureName +
									"/ec_customEditorData/parameters/" + paramName +
									"/options/list");

		doRequest(new ChainedCallback() {
			@Override public void onComplete() {
	           	  	// All done!             
			}
	    }, getProperty);    
	}
	/*
	 * Creation of a widget based on the type of parameter
	 */
	Widget createParamWidget(String pName) {	
		Widget widget;				// Widget to create
		String pType=m_paramTypes.get(pName);

 	   	// getLog().debug("  Creating widget for " + pName + ": " + pType + "\n");
		/*
		 * simply entry
		 */

		if (pType.equals("entry")) {
 		   TextBox TB= new TextBox();
 		   TB.setValue(m_paramDefaultValues.get(pName));
 		   widget=TB.asWidget();
		} else if (pType.equals("checkbox")) {
     		CheckBox CB=new CheckBox();
     		if (m_paramDefaultValues.get(pName).equals("true")) {
     			CB.setValue(true);
     		} else {
     			CB.setValue(false);
     		}
     		widget=CB.asWidget();
		} else if (pType.equals("select")) {
     		ListBox LB=new ListBox();
     		widget=LB.asWidget();
     		getMenuChoices(pName, LB);
		} else if (pType.equals("radio")) {
			HorizontalPanel hPanel=new HorizontalPanel();
			hPanel.setSpacing(10);
			widget=hPanel.asWidget();
			getRadioChoices(pName, hPanel);
		} else if (pType.equals("project")) {
     		ListBox LB=new ListBox();
     		widget=LB.asWidget();
     		getProjectChoices(pName, LB);
		} else {
 		   // getLog().debug("Unknown type for " + pName + ": " + pType + "\n");
 		   widget=null;
     	}
     	// getLog().debug("  Widget created " + widget.toString() + "\n");
     	widget.setTitle(m_paramDescriptions.get(pName));
     	return widget;
	}
	
	String getWidgetValue(String pName) {
		Widget widget=m_paramWidgets.get(pName);		// Widget to read
		String pType=m_paramTypes.get(pName);			// Type of the Widget
		
		if (pType.equals("entry")) {
     		TextBox TB=(TextBox) widget;
 		   return TB.getValue();
		}
		if (pType.equals("checkbox")) {
     		CheckBox CB=(CheckBox) widget;
     		if (CB.getValue()) {
     			return "true";
     		} else {
     			return "false";
     		}
		}
		if (pType.equals("select") || pType.equals("project")) {
     		ListBox LB=(ListBox) widget;
     		return LB.getValue(LB.getSelectedIndex());
     	}
		if (pType.equals("radio")) {
			HorizontalPanel HP=(HorizontalPanel) widget;
			Iterator<Widget> widgets=HP.iterator();
	       	 while(widgets.hasNext()) {
	      	   RadioButton RB = (RadioButton) widgets.next();
	      	   if (RB.getValue()) {
	      		   return RB.getText();
	      	   }
	      	 }
		}
 		   
		// getLog().debug("Unknown type for " + pName + ": " + pType + "\n");
 	   return null;
 	}
	
	public static native void alert(String msg) /*-{
	  $wnd.alert(msg);
	}-*/;
}


