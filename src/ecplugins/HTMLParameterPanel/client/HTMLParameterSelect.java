/**
 * 
 */
package ecplugins.HTMLParameterPanel.client;

import com.electriccloud.commander.client.ChainedCallback;
import com.electriccloud.commander.client.domain.Property;
import com.electriccloud.commander.client.requests.GetPropertyRequest;
import com.electriccloud.commander.client.responses.CommanderError;
import com.electriccloud.commander.client.responses.CommanderErrorHandler;
import com.electriccloud.commander.client.responses.DefaultPropertyCallback;
import com.electriccloud.commander.client.responses.PropertyCallback;
import com.electriccloud.commander.gwt.client.Component;
import com.google.gwt.user.client.ui.ListBox;

/**
 * @author lrochette
 *
 */
public class HTMLParameterSelect extends HTMLParameterList implements CommanderErrorHandler{

    //~ Methods ----------------------------------------------------------------
	public HTMLParameterSelect(String parameterName, String parameterType, 
			String parameterDefaultValue, String parameterDescription, 
			Boolean isRequired, 
			String projName, String procName) {
		
		super(parameterName, parameterType, parameterDefaultValue, 
				parameterDescription, isRequired, 
				projName, procName);	
	}

	public String getValue() {
		ListBox LB=(ListBox) m_widget;
 		return LB.getValue(LB.getSelectedIndex());
	}
	
	public void createWidget(Component component)  {		
		m_component=component;

 		ListBox LB=new ListBox();
 		m_widget=LB.asWidget();
 		getMenuChoices(m_name, LB);
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

	@Override
	public void handleError(CommanderError error) {
		// TODO Auto-generated method stub
		
	}

}
