package ecplugins.HTMLParameterPanel.client;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.electriccloud.commander.client.ChainedCallback;
import com.electriccloud.commander.client.CommanderRequestFactory;
import com.electriccloud.commander.client.domain.BatchMode;
import com.electriccloud.commander.client.domain.ObjectType;
import com.electriccloud.commander.client.domain.Order;
import com.electriccloud.commander.client.domain.Project;
import com.electriccloud.commander.client.domain.Property;
import com.electriccloud.commander.client.domain.TimeMode;
import com.electriccloud.commander.client.CommanderRequestManager;
import com.electriccloud.commander.client.requests.CommanderRequest;
import com.electriccloud.commander.client.requests.FindObjectsRequest;
import com.electriccloud.commander.client.requests.GetPropertyRequest;
import com.electriccloud.commander.client.requests.FindObjectsFilter.IsNullFilter;
import com.electriccloud.commander.client.responses.CommanderError;
import com.electriccloud.commander.client.responses.CommanderErrorHandler;
import com.electriccloud.commander.client.responses.DefaultPropertyCallback;
import com.electriccloud.commander.client.responses.FindObjectsResponse;
import com.electriccloud.commander.client.responses.FindObjectsResponseCallback;
import com.electriccloud.commander.client.responses.PropertyCallback;
import com.electriccloud.commander.gwt.client.Component;
import com.electriccloud.commander.gwt.client.ComponentBase;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

 
public class HTMLParameterWidget implements CommanderErrorHandler
{
	protected Component 	m_component; 	//The plugin component requesting this  widget
	protected String 		m_name;
	protected String 		m_type;
	protected String 		m_description;
	protected String 		m_defaultValue;
	protected Widget 		m_widget;
	static String 		s_projectName;
	static String 		s_procedureName;
	protected Boolean     m_isRequired;
	
    //~ Methods ----------------------------------------------------------------
	public HTMLParameterWidget(String parameterName, String parameterType, 
			String parameterDefaultValue, String parameterDescription, Boolean isRequired) {
		m_name=parameterName;
		m_type=parameterType;
		m_defaultValue=parameterDefaultValue;
		m_description=parameterDescription;
		m_isRequired=isRequired;
	}
	/*
	 * Return a String representation of a parameter widget
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String str="";
		str += "Name    : " + m_name + "\n";
		str += "Type    : " + m_type + "\n";
		str += "Default : " + m_defaultValue + "\n";
		str += "Required: " + m_isRequired + "\n";
		str += "Descript: " + m_description + "\n";
		
		return str;
	}
	
	
	public Widget getWidget() {
		return this.m_widget;
	}
	
	public Boolean isRequired() {
		return this.m_isRequired;
	}

	public String getDefaultValue() {
		return this.m_defaultValue;
	}

	public void setProjectName(String projName) {
		s_projectName = projName;
	}

	public void setProcedureName(String procName) {
		s_projectName = procName;
	}

	public  HTMLParameterWidget(String parameterName, String parameterType, 
			String parameterDefaultValue, String parameterDescription, Boolean isRequired,
			String projName, String procName) {
		m_name=parameterName;
		m_type=parameterType;
		m_defaultValue=parameterDefaultValue;
		m_description=parameterDescription;
		m_isRequired=isRequired;
		s_projectName=projName;
		s_procedureName=procName;
	}
	
	/*
	 *  Create the GWT widget part of a parameter
	 */
	public void createWidget(Component component)  {		
		m_component=component;
		// component.getLog().debug("Creating Widget for " + m_name + "\n");
		
 		// simple text entry
		if (m_type.equals("checkbox")) {
     		CheckBox CB=new CheckBox();
     		if (m_defaultValue.equals("true")) {
     			CB.setValue(true);
     		} else {
     			CB.setValue(false);
     		}
     		m_widget=CB.asWidget();
		} else if (m_type.equals("select")) {
     		ListBox LB=new ListBox();
     		m_widget=LB.asWidget();
     		getMenuChoices(m_name, LB);
		} else if (m_type.equals("radio")) {
			HorizontalPanel hPanel=new HorizontalPanel();
			hPanel.setSpacing(10);
			m_widget=hPanel.asWidget();
			getRadioChoices(m_name, hPanel);
		} else if (m_type.equals("project")) {
     		ListBox LB=new ListBox();
     		m_widget=LB.asWidget();
     		getProjectChoices(m_name, LB);
		} else {
 		   alert("Type for " + m_name + ": " + m_type + "not yet supported\n");
 		   m_widget=null;
     	}
	}

	public static native void alert(String msg) /*-{
	  $wnd.alert(msg);
	}-*/;
	
    /**
     * Function to get the choices of a project
     */
	private void getProjectChoices(final String paramName, final ListBox LB) {
		// m_component.getLog().debug("Entering getProjectChoices\n");
        FindObjectsResponseCallback projectRequestCallback = new FindObjectsResponseCallback() {
            @Override public void handleError(CommanderError error) {
                return; 	// There was an error, list will be empty
            }
            
			@Override public void handleResponse(FindObjectsResponse response) {
	        	List<Project> prjList=response.getProjects();
	        	Integer index=0;
	        	Integer match=-1;
	        	//m_component.getLog().debug("Default Value " + m_defaultValue + "\n");
	        	for (Project prj: prjList) {
	        		String prjName=prj.getName();
	        		if (m_defaultValue.equals(prjName)) {
	        			match=index;
	        			//m_component.getLog().debug("Default Selected " + index + "\n");
	        		}
	        		//m_component.getLog().debug("Adding menu choice " + prjName + "\n");
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
		FindObjectsRequest  findProjects = m_component.getRequestManager().getRequestFactory()
                .createFindObjectsRequest(ObjectType.project);
		// Set maximum number of results returned by the request
        findProjects.setMaxIds(100);
        // Filter results: no plugins
        IsNullFilter filter=new IsNullFilter("pluginName");
        findProjects.addFilter(filter);
        findProjects.addSort("projectName", Order.ascending);
 
		// Set the callback
        findProjects.setCallback(projectRequestCallback);
		
        m_component.getRequestManager().doRequest(new ChainedCallback() {
			@Override public void onComplete() {
	           	  	// All done!             
			}
	    }, findProjects);    

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
	        	
	        	// getLog().debug("Default Value " + defaultValue + "\n");
	        	for (String choice: choices) {
	        		if (m_defaultValue.equals(choice)) {
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

		GetPropertyRequest getProperty = m_component.getRequestManager().getRequestFactory().createGetPropertyRequest();
		getProperty.setCallback(propertyRequestCallback);
		getProperty.setPropertyName("/projects/" + s_projectName +
									"/procedures/" + s_procedureName +
									"/ec_customEditorData/parameters/" + paramName +
									"/options/list");

		m_component.getRequestManager().doRequest(new ChainedCallback() {
			@Override public void onComplete() {
	           	  	// All done!             
			}
	    }, getProperty);    
	}

	
	/**
     * Function to get the choices of a radio button
     */
	private void getRadioChoices(final String paramName, final HorizontalPanel HP) {
		/**
		 * Callback function to handle the property value
		 */
		PropertyCallback propertyRequestCallback = new DefaultPropertyCallback(this) {
            @Override public void handleError(CommanderError error) {
                return; 	// There was an error, list will be empty
            }

			@Override public void handleResponse(Property property) {
	        	String choiceList=property.getValue();
	        	String[] choices = choiceList.split("\\|");
	        	// getLog().debug("Default Value " + m_defaultValue + "\n");
	        	
	        	for (String choice: choices) {
	        		// getLog().debug("Adding menu choice " + choice + "\n");
	        		// creating a radio button for each choice in the group
	        		// named after the parameter
	        		final RadioButton RB=new RadioButton(paramName, choice);
	        		HP.add(RB);
	        		if (m_defaultValue.equals(choice)) {
	        			RB.setValue(true);
	        		}
	        	}
	        }
		};

		GetPropertyRequest getProperty = m_component.getRequestManager().getRequestFactory().createGetPropertyRequest();
		getProperty.setCallback(propertyRequestCallback);
		getProperty.setPropertyName("/projects/" + s_projectName +
									"/procedures/" + s_procedureName +
									"/ec_customEditorData/parameters/" + paramName +
									"/options/list");

		m_component.getRequestManager().doRequest(new ChainedCallback() {
			@Override public void onComplete() {
	           	  	// All done!             
			}
	    }, getProperty);    
	}
	@Override
	public void handleError(CommanderError error) {
		// TODO Auto-generated method stub
		return;
	}

	public String getValue() {
		Widget widget=this.getWidget();		// Widget to read
		
		if (m_type.equals("checkbox")) {
     		CheckBox CB=(CheckBox) widget;
     		if (CB.getValue()) {
     			return "true";
     		} else {
     			return "false";
     		}
		}
		if (m_type.equals("select") || m_type.equals("project")) {
     		ListBox LB=(ListBox) widget;
     		return LB.getValue(LB.getSelectedIndex());
     	}
		if (m_type.equals("radio")) {
			HorizontalPanel HP=(HorizontalPanel) widget;
			Iterator<Widget> widgets=HP.iterator();
	       	 while(widgets.hasNext()) {
	      	   RadioButton RB = (RadioButton) widgets.next();
	      	   if (RB.getValue()) {
	      		   return RB.getText();
	      	   }
	      	 }
		}
 		   
		// getLog().debug("Unknown type for " + pName + ": " + m_type + "\n");
 	   return null;
	}



}
