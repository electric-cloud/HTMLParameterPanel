This plugin allows you to use HTML in your parameter panels to input data to your procedures and states.

To do this, simply install out/HTMLParameterPanel.jar or recompile the plugin in Eclipse following the instructions in the plugin developer guide. 

This plugin has been compiled with SDK 5.0 and requires ElectricCommander 5.x.

To use it, simply:

1. set the custom procedure property customerType to HTMLParameterPanel-1.0.0/HTML
2. create a custom procedure property named ec_parameterHTML that will contain some HTML and "id" pointers to your parameters like
```
	<span id="Param1"></span>
```
or
```
	<div id="Param2"></div>
```

See the ec_parameterHTML.html file provided as an example

Known issues:
 * Credential parameters are not yet supported
 * Select parmeters only support "short" list aka choice1|choice2|choice3
 * Tooltips (description) are not yet implemented
 * Javascript does not seem to work

Contact author: Laurent Rochette (lrochette@electric-cloud.com)

Legal Jumbo

This module is free for use. Modify it however you see fit to better your experience using ElectricCommander. Share your enhancements and fixes.

This module is not officially supported by Electric Cloud. It has undergone no formal testing and you may run into issues that have not been uncovered in the limited manual testing done so far.

Electric Cloud should not be held liable for any repercusions of using this software.