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
 		return getValue(LB.getValue(LB.getSelectedIndex()));
	}
	
	public void createWidget(Component component)  {		
		m_component=component;

 		ListBox LB=new ListBox();
 		m_widget=LB.asWidget();
 		getChoices();
	}

	@Override public void fillWidgetList() {
		m_component.getLog().debug("Entering fillWidget-Select for " + m_name + "\n");
		Integer index=0;
    	Integer match=-1;
    	ListBox LB=(ListBox) m_widget;
	   	Iterator<String> keySetIterator = m_choiceList.keySet().iterator();
	    while(keySetIterator.hasNext()) {
	    	String key = keySetIterator.next();
    		if (m_defaultValue.equals(key)) {
    			match=index;
    			// getLog().debug("Default Selected " + index + "\n");
    		}
    		// getLog().debug("Adding menu choice " + choice + "\n");
    		LB.addItem(key);
    		index ++;
    	}
    	if (match >=0) {
    		LB.setSelectedIndex(match);
    	}

	}

	@Override
	public void handleError(CommanderError error) {
		// TODO Auto-generated method stub
		
	}

}
