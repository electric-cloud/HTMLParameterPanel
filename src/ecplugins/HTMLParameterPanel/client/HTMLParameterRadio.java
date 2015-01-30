package ecplugins.HTMLParameterPanel.client;

import java.util.Iterator;

import com.electriccloud.commander.client.ChainedCallback;
import com.electriccloud.commander.client.domain.Property;
import com.electriccloud.commander.client.requests.GetPropertyRequest;
import com.electriccloud.commander.client.responses.CommanderError;
import com.electriccloud.commander.client.responses.CommanderErrorHandler;
import com.electriccloud.commander.client.responses.DefaultPropertyCallback;
import com.electriccloud.commander.client.responses.PropertyCallback;
import com.electriccloud.commander.gwt.client.Component;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

public class HTMLParameterRadio extends HTMLParameterWidget implements CommanderErrorHandler {

    //~ Methods ----------------------------------------------------------------
	public HTMLParameterRadio(String parameterName, String parameterType, 
			String parameterDefaultValue, String parameterDescription, 
			Boolean isRequired, 
			String projName, String procName) {
		
		super(parameterName, parameterType, parameterDefaultValue, 
				parameterDescription, isRequired,
				projName, procName);	
	}

	public String getValue() {
		
		HorizontalPanel HP=(HorizontalPanel) m_widget;
		Iterator<Widget> widgets=HP.iterator();
       	while(widgets.hasNext()) {
      	   RadioButton RB = (RadioButton) widgets.next();
      	   if (RB.getValue()) {
      		   return RB.getText();
      	   }
      	 }
 	   return null;
	}

	public void createWidget(Component component)  {		
		m_component=component;

		HorizontalPanel hPanel=new HorizontalPanel();
		hPanel.setSpacing(10);
		m_widget=hPanel.asWidget();
		getRadioChoices(m_name, hPanel);
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
		
	}
}

