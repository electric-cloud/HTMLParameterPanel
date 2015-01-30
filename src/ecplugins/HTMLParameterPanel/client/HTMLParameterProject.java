package ecplugins.HTMLParameterPanel.client;

import java.util.List;

import com.electriccloud.commander.client.ChainedCallback;
import com.electriccloud.commander.client.domain.ObjectType;
import com.electriccloud.commander.client.domain.Order;
import com.electriccloud.commander.client.domain.Project;
import com.electriccloud.commander.client.requests.FindObjectsRequest;
import com.electriccloud.commander.client.requests.FindObjectsFilter.IsNullFilter;
import com.electriccloud.commander.client.responses.CommanderError;
import com.electriccloud.commander.client.responses.FindObjectsResponse;
import com.electriccloud.commander.client.responses.FindObjectsResponseCallback;
import com.electriccloud.commander.gwt.client.Component;
import com.google.gwt.user.client.ui.ListBox;

public class HTMLParameterProject extends HTMLParameterWidget {

    //~ Methods ----------------------------------------------------------------
	public HTMLParameterProject(String pName, String pValue, String pDesc,
			Boolean pReq) {
		super(pName, "project", pValue, pDesc, pReq);
	}
	
	public String getValue() {
		ListBox LB=(ListBox) m_widget;
 		return LB.getValue(LB.getSelectedIndex());
	}
	
	public void createWidget(Component component) {
		m_component=component;
 		ListBox LB=new ListBox();
 		m_widget=LB.asWidget();
 		getProjectChoices(m_name, LB);
	}
	
    /**
     * Function to get the choices of a project
     */
	private void getProjectChoices(final String paramName, final ListBox LB) {
		// m_component.getLog().debug("Entering getProjectChoices\n");
        FindObjectsResponseCallback projectRequestCallback = new FindObjectsResponseCallback() {
            @Override public void handleError(CommanderError error) {
                return; 	// There was an error, list will be empty
            }
            
			@Override public void handleResponse(FindObjectsResponse response) {
	        	List<Project> prjList=response.getProjects();
	        	Integer index=0;
	        	Integer match=-1;
	        	//m_component.getLog().debug("Default Value " + m_defaultValue + "\n");
	        	for (Project prj: prjList) {
	        		String prjName=prj.getName();
	        		if (m_defaultValue.equals(prjName)) {
	        			match=index;
	        			//m_component.getLog().debug("Default Selected " + index + "\n");
	        		}
	        		//m_component.getLog().debug("Adding menu choice " + prjName + "\n");
	        		LB.addItem(prjName);
	        		index ++;
	        	}
	        	if (match >=0) {
	        		LB.setSelectedIndex(match);
	        	}
	        }
		};
	   	/**
		 * Callback function to handle the property value
		 */
		FindObjectsRequest  findProjects = m_component.getRequestManager().getRequestFactory()
                .createFindObjectsRequest(ObjectType.project);
		// Set maximum number of results returned by the request
        findProjects.setMaxIds(100);
        // Filter results: no plugins
        IsNullFilter filter=new IsNullFilter("pluginName");
        findProjects.addFilter(filter);
        findProjects.addSort("projectName", Order.ascending);
 
		// Set the callback
        findProjects.setCallback(projectRequestCallback);
		
        m_component.getRequestManager().doRequest(new ChainedCallback() {
			@Override public void onComplete() {
	           	  	// All done!             
			}
	    }, findProjects);    

	}

}
