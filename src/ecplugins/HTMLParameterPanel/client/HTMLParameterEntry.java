package ecplugins.HTMLParameterPanel.client;

import com.electriccloud.commander.gwt.client.Component;
import com.google.gwt.user.client.ui.TextBox;

public class HTMLParameterEntry extends HTMLParameterWidget {

    //~ Methods ----------------------------------------------------------------
	public HTMLParameterEntry(String pName, String pValue, String pDesc,
			Boolean pReq) {
		super(pName, "select", pValue, pDesc, pReq);
	}

	public String getValue() {
     	TextBox TB=(TextBox) m_widget;
     	return TB.getValue();
	}
	
	public void createWidget(Component component) {
		m_component=component;
		
 		// simple text entry
	   TextBox TB = new TextBox();
	   TB.setValue(m_defaultValue);
	   m_widget=TB.asWidget();
	}
}
