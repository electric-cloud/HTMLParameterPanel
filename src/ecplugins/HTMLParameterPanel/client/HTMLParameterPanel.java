
// HTMLParameterPanel.java --
//
// HTMLParameterPanel.java is part of the HTMLParamPanel plugin.
//
// Copyright (c) 2014 Electric Cloud, Inc.
// All rights reserved.
//

package ecplugins.HTMLParameterPanel.client;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.electriccloud.commander.client.ChainedCallback;
import com.electriccloud.commander.client.domain.ActualParameter;
import com.electriccloud.commander.client.domain.FormalParameter;
import com.electriccloud.commander.client.domain.Property;
import com.electriccloud.commander.client.requests.GetPropertyRequest;
import com.electriccloud.commander.client.responses.DefaultPropertyCallback;
import com.electriccloud.commander.client.responses.PropertyCallback;
import com.electriccloud.commander.gwt.client.Component;
import com.electriccloud.commander.gwt.client.ComponentBase;
import com.electriccloud.commander.gwt.client.ui.ParameterPanel;
import com.electriccloud.commander.gwt.client.ui.ParameterPanelProvider;

/**
 * Basic component that is meant to be cloned and then customized to perform a
 * real function.
 */
public class HTMLParameterPanel
    extends ComponentBase
    implements ParameterPanel, ParameterPanelProvider {

	private Map<String, FormalParameter> m_formalParams; 	// Formal parameters
	private Map <String, HTMLParameterWidget> m_widgets;	// parameter widget objects
	private String projectName;
	private String procedureName;
	
    //~ Methods ----------------------------------------------------------------
	HTMLPanel HTMLpanel;
	Component component=this;
    /**
     * This function is called by SDK infrastructure to initialize the UI parts of
     * this component.
     *
     * @return                 A widget that the infrastructure should place in
     *                         the UI; usually a panel.
     */
    @Override public Widget doInit()
    {
    	/*
    	 * Create the HTML panel that will get the code
    	 */
    	HTMLpanel = new HTMLPanel("<div id='root'><h1>Loading, please wait...</h1></div>");
    	// getLog().debug("HTML panel instanciated\n");
    	//HTMLpanel.setSize("400px", "400px");
    	HTMLpanel.addStyleName("HTML panel");
 
        return HTMLpanel;
    }

	 /**
     * Performs validation of user supplied data before submitting the form.
     * This function is called after the user hits submit.</p>
     * @return  true if checks succeed, false otherwise
     */
	@Override public boolean validate() {
		// TODO Auto-generated method stub
   	 	// Loop over each parameter
   	 	Iterator<String> keySetIterator = m_widgets.keySet().iterator();
   	 	getLog().debug("Entering Validate\n");
   	 	while(keySetIterator.hasNext()) {
   	 		String key = keySetIterator.next();
   	 		HTMLParameterWidget paramWidget=m_widgets.get(key);
   	 		getLog().debug(paramWidget.toString());
   	 		if (paramWidget.isRequired() && paramWidget.getValue().equals("")) {
   	 			alert("Parameter " + key + " is required but does not have a value");
   	 			return false;
   	 		}
   	 	}
         return true;
	}
	
	@Override public ParameterPanel getParameterPanel() {
		// TODO Auto-generated method stub
		return this;
	}


    /**
     * Gets the values of the parameters that should map 1-to-1 to the formal
     * parameters on the object being called. Transform user input into a map of
     * parameter names and values.
     *
     * <p>This function is called after the user hits submit and validation has
     * succeeded.</p>
     *
     * @return  The values of the parameters that should map 1-to-1 to the
     *          formal parameters on the object being called.
     */
    @Override public Map<String, String> getValues() {
        Map<String, String> values = new HashMap<String, String>();

   	 	// Loop over each parameter
   	 	Iterator<String> keySetIterator = m_widgets.keySet().iterator();
   	 	while(keySetIterator.hasNext()) {
   	 		String key = keySetIterator.next();
   	 		values.put(key, m_widgets.get(key).getValue());
   	 	}
         return values;
    }	
    
    /**
     * Push actual parameters into the panel implementation.
     *
     * This is used when editing an existing object to show existing
     * content.</p>
     */
	@Override public void setActualParameters(Collection<ActualParameter> actualParameters) {
		// TODO Auto-generated method stub
		
	}

    /**
     * Push form parameters into the panel implementation.
     * This is used when creating a new object and showing default values.
     *
     * @param  formalParameters  Formal parameters on the target object.
     */
    @Override public void setFormalParameters(Collection<FormalParameter> formalParameters) {
        m_formalParams  = new HashMap<String, FormalParameter>();
        m_widgets		= new HashMap<String, HTMLParameterWidget>();
        /*
         * Extract project and procedure from the URL
         * URL need to be decoded (Issue #5)
         */
        String URLstring=Window.Location.getPath();
        String pattern = "/commander/link/runProcedure/projects/([%.\\w]+)/procedures/([%.\\w]+)";
		projectName   = URL.decode(URLstring.replaceAll(pattern, "$1"));
        procedureName = URL.decode(URLstring.replaceAll(pattern, "$2"));

        /* 
         * Issues with %3D (=) which don't seem to be decoded
         */
        projectName = projectName.replaceAll("%3D", "=");
        procedureName = procedureName.replaceAll("%3D", "=");

        for (FormalParameter formalParameter : formalParameters) {
        	String pName=formalParameter.getName();
        	getLog().debug("Param: " + pName + ", Required: "+  formalParameter.isRequired() + "\n");
            m_widgets.put(pName, new HTMLParameterWidget(pName, formalParameter.getType(), formalParameter.getDefaultValue(),
            		formalParameter.getDescription(), formalParameter.isRequired(), projectName, procedureName));
        }
        
       	/*
    	 * Let's get the ec_parameterHTML property we have to render
    	 */
    	 getProperties();

    }

    /**
     * Function to get the ec_parameterHTML
     * and replace each parameter placeholder by a widget
     */
	 void getProperties() {
		/**
		 * Callback function to handle the property value
		 */
		PropertyCallback propertyRequestCallback = new DefaultPropertyCallback(this) {
	        @SuppressWarnings("deprecation")
			@Override public void handleResponse(Property property) {
	        	 // getLog().debug("Property retrieved" + property.getValue());
	        	 HTMLpanel.getElementById("root").setInnerHTML(property.getValue());
	        	
	        	 //getLog().debug("Processing the list of formal parameters for replacement\n");
	        	 // Loop over each parameter
	        	 Iterator<String> keySetIterator = m_widgets.keySet().iterator();
	        	 while(keySetIterator.hasNext()) {
	        	   String key = keySetIterator.next();
	        	   
	        	   HTMLParameterWidget paramElement=m_widgets.get(key);
	        	   paramElement.createWidget(component);
	        	   Element HTMLelement=HTMLpanel.getElementById(key);
	        	   if (HTMLelement != null) {
	        		   HTMLpanel.addAndReplaceElement(paramElement.getWidget(), HTMLpanel.getElementById(key));
	        	   } else {
	        		   alert("Parameter " + key + 
	        				 " does not have an entry in ec_parameterHTML\n");
	        	   }
	        	 }
	        	 // getLog().debug("All formal parameters processed\n");
	         }
		};

		GetPropertyRequest getProperty = getRequestFactory().createGetPropertyRequest();
		getProperty.setCallback(propertyRequestCallback);
		getProperty.setPropertyName("/projects/"+projectName+
									"/procedures/"+procedureName+"/ec_parameterHTML");

		doRequest(new ChainedCallback() {
			@Override public void onComplete() {
	           	  	// All done!             
			}
	    }, getProperty);    
	}

	 	
	public static native void alert(String msg) /*-{
	  $wnd.alert(msg);
	}-*/;
}


