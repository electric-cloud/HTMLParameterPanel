package ecplugins.HTMLParameterPanel.client;

import java.util.HashMap;
import java.util.Map;

/**
 * The class HTMLParameterList is used to abstract the list of values
 * required for RadioButton and Menus
 * *
 * @author L. Rochette
 *
 */
public class HTMLParameterList extends HTMLParameterWidget {
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

}
