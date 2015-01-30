package ecplugins.HTMLParameterPanel.client;

import com.electriccloud.commander.client.responses.CommanderError;
import com.electriccloud.commander.client.responses.CommanderErrorHandler;
import com.electriccloud.commander.gwt.client.Component;
import com.google.gwt.user.client.ui.Widget;

/**
 * Generic class to represent a parameter to display in a HTMLPanel
 *  
 *	@author lrochette
 *
 */
public class HTMLParameterWidget implements CommanderErrorHandler
{
	protected Component 	m_component; 	// The plugin component requesting this  widget
	protected String 		m_name;
	protected String 		m_type;
	protected String 		m_description;
	protected String 		m_defaultValue;
	protected Widget 		m_widget;
	protected Boolean       m_isRequired;

    //~ Methods ----------------------------------------------------------------
	/**
	 * Constructor
	 * 
	 * @param parameterName				name of this parameter
	 * @param parameterType				type of this parameter (entry, select, 
	 * 									radio, credential, project, checkbox)
	 * @param parameterDefaultValue		default value of this parameter
	 * @param parameterDescription		the description of this parameter to 
	 * 									display as a tooltip
	 * @param isRequired				is this parameter required (non empty value)
	 */
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
	
	/*
	 *  Create the GWT widget part of a parameter
	 */
	public void createWidget(Component component)  {		
	   alert("Parameters of type " + m_type + " are not yet supported.\n" + 
	   		 "Please open an issue on GitHub to have this fixed.\n");
	   m_widget=null;
	}

	public static native void alert(String msg) /*-{
	  $wnd.alert(msg);
	}-*/;
	
 	
	@Override
	public void handleError(CommanderError error) {
		// TODO Auto-generated method stub
		return;
	}

	
	public String getValue() {
		alert("getValue: We should not be here for " + m_name + ": " + m_type + "\n");
 	   	return null;
	}

}
