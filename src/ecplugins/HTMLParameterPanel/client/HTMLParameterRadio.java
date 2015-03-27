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

/**
 * @author lrochette
 *
 */
public class HTMLParameterRadio extends HTMLParameterList implements CommanderErrorHandler {

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
		getChoices();
	}

	@Override public void fillWidgetList() {
		m_component.getLog().debug("Entering fillWidget-Radio for " + m_name + "\n");
		HorizontalPanel HP=(HorizontalPanel) m_widget;
		
	   	Iterator<String> keySetIterator = m_choiceList.keySet().iterator();
	    while(keySetIterator.hasNext()) {
	    	String key = keySetIterator.next();
	    	m_component.getLog().debug("Adding radio choice " + key + "\n");
    		final RadioButton RB=new RadioButton(m_name, key);
    		HP.add(RB);
       		if (m_defaultValue.equals(key)) {
       			RB.setValue(true);
    		}
    	}
 
	}

	@Override
	public void handleError(CommanderError error) {
		// TODO Auto-generated method stub
		
	}
}

