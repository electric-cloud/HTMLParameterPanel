
package ecplugins.HTMLParameterPanel.client;

import com.electriccloud.commander.gwt.client.ComponentContext;

import com.electriccloud.commander.gwt.client.Component;
import com.electriccloud.commander.gwt.client.ComponentBaseFactory;

/**
 * This factory is responsible for providing instances of the HTMLParameterPanel
 * class.
 */
public class HTMLParameterPanelFactory
    extends ComponentBaseFactory
{
 
    //~ Methods ----------------------------------------------------------------

    @Override public Component createComponent(ComponentContext jso)
    {
        return new HTMLParameterPanel();
    }
}
