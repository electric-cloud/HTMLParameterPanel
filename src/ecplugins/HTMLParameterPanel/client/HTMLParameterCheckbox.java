package ecplugins.HTMLParameterPanel.client;

import com.electriccloud.commander.gwt.client.Component;
import com.google.gwt.user.client.ui.CheckBox;

public class HTMLParameterCheckbox extends HTMLParameterWidget {

    //~ Methods ----------------------------------------------------------------
	public HTMLParameterCheckbox(String pName, String pValue, String pDesc,
			Boolean pReq) {
		super(pName, "checkbox", pValue, pDesc, pReq);
	}

	public String getValue() {
		CheckBox CB=(CheckBox) m_widget;
 		if (CB.getValue()) {
 			return "true";
 		} else {
 			return "false";
 		}
	}
	
	public void createWidget(Component component) {
		m_component=component;
		
 		// simple text entry
 		CheckBox CB=new CheckBox();
 		if (m_defaultValue.equals("true")) {
 			CB.setValue(true);
 		} else {
 			CB.setValue(false);
 		}
 		m_widget=CB.asWidget();
	}

}
