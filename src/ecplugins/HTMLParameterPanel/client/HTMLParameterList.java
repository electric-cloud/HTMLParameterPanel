package ecplugins.HTMLParameterPanel.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.electriccloud.commander.client.ChainedCallback;
import com.electriccloud.commander.client.domain.Property;
import com.electriccloud.commander.client.domain.PropertySheet;
import com.electriccloud.commander.client.requests.GetPropertiesRequest;
import com.electriccloud.commander.client.responses.CommanderError;
import com.electriccloud.commander.client.responses.CommanderErrorHandler;
import com.electriccloud.commander.client.responses.PropertySheetCallback;


/**
 * The class HTMLParameterList is used to abstract the list of values
 * required for RadioButton and Menus
 * *
 * @author L. Rochette
 *
 */
public class HTMLParameterList extends HTMLParameterWidget implements CommanderErrorHandler {
	protected static String 		s_projectName;
	protected static String 		s_procedureName;
	protected Map<String, String>   m_choiceList;	// list of text/value 
	
	public HTMLParameterList(String parameterName, String parameterType, 
			String parameterDefaultValue, String parameterDescription, Boolean isRequired, 
			String projName, String procName) {
		super(parameterName, parameterType, parameterDefaultValue, parameterDescription, isRequired);
		s_projectName=projName;
		s_procedureName=procName;	
		m_choiceList=new HashMap<String, String>();
	}

	/*
	 * The first thing to do it do get the
	 * ec_customEditorData/parameters/PARAM/type property
	 * to decide how the list is set
	 *   list       => grab 'optionCount' then option1 to optionX 
	 *   simpleList => grab 'list' and split on '|'
	 */
	void getChoices() {
		m_component.getLog().debug("Entering getChoices for " + m_name + "\n");
		
        GetPropertiesRequest getPropertiesRequest = m_component.getRequestManager()
                .getRequestFactory().createGetPropertiesRequest();
        getPropertiesRequest.setProjectName(s_projectName);
        getPropertiesRequest.setProcedureName(s_procedureName);
        getPropertiesRequest.setPath("ec_customEditorData/parameters/" + m_name + "/options");
        getPropertiesRequest.setRecurse(true);
        
        getPropertiesRequest.setCallback(new PropertySheetCallback() {
        	
	        @Override
	        public void handleError(CommanderError error) {
	            // Ignore non-existent sheets.
	            /* if (!error.getCode().equals(
	                    ErrorCode.NoSuchProperty.toString())) {
	                m_errorPanel.addErrorMessage(error.getMessage());
	            } */
	        	m_component.getLog().debug(" ERROR ( " + m_name + ") :" + error.getMessage() + "\n");
	        	return;
	        };
        
            @Override
            public void handleResponse(PropertySheet response) {
            	String type=response.getValue("type");
            	
            	if (type.equals("simpleList")) {
            		/*
            		 * Get the list of options through the list property
            		 * choice1|choice2|choice3
            		 */
    	        	String choiceList=response.getValue("list");
    	        	String[] choices = choiceList.split("\\|");
    	        	for (String value: choices) {
    	        		m_choiceList.put(value, value);
    	        	}
            	} else if (type.equals("list")) {
            		/*
            		 * Choices are in option1, option2, ...
            		 * with text value pair in each PS
            		 */
	                Iterator<Entry<String, Property>> sheets =
	                        response.getProperties().entrySet().iterator();
	                while (sheets.hasNext()) {
	                    Entry<String, Property> sheet = sheets.next();
	                    PropertySheet options = sheet.getValue().getPropertySheet();
	                    String name  = options.getValue("text");
		                String value = options.getValue("value");
		                m_component.getLog().debug("Prop "+ m_name + ": " + name + " => " + value + "\n");
		                m_choiceList.put(name, value);        
	                } 
                
            	} else {
            		alert ("Menu of type " + type + " are not yet supported. Open an issue on GitHub!");
            	}
            	fillWidgetList();
            }
        });
        
		m_component.getRequestManager().doRequest(new ChainedCallback() {
			@Override public void onComplete() {
	           	  	// All done!             
			}
	    }, getPropertiesRequest);    

	}

	public void fillWidgetList() {
		m_component.getLog().debug("Entering fillWidget-List for " + m_name + "\n");
		// TODO Auto-generated method stub		
	}

	
	public String getValue(String choice) {
		return m_choiceList.get(choice);
	}
}
