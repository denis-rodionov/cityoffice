// declare a new module called 'myApp', and make it require the `ng-admin` module as a dependency
var myApp = angular.module('myApp', ['ng-admin']);
// declare a function to run when the module bootstraps (during the 'config' phase)
myApp.config(['NgAdminConfigurationProvider', function (nga) {
    // create an admin application
    var admin = nga.application('Admin Tool').debug(true);
    // more configuation here later
    
    var project = nga.entity('project')
    	.label("Projects");
    
    project.listView().fields([
	                          nga.field('name')
	                          	.label('Name')
	                          	.editable(true)
	                          	.isDetailLink(true),
	                          nga.field('active', 'boolean')
	                          	.label('Active'),	                          	
	                          nga.field('colorName')
	                          	.label('Color')
                            ]);
    project.creationView().fields([
        nga.field('name')
        	.label('Project Name')
        	.validation({ required: true, minlength: 1, maxlength: 20}),
        nga.field('active', 'boolean')
        	.label('Active')
        	.validation({ required: true })
        	.defaultValue(true),
        nga.field('colorName', 'choice')
        	.choices([
        	    { value: 'default', label: 'default '},
        	    { value: 'primary', label: 'primary'},
        	    { value: 'success', label: 'success'},
        	    { value: 'info', label: 'info'},
        	    { value: 'warning', label: 'warning'},
        	    { value: 'danger', label: 'danger'}
        	])
        	.label('Color Scheme')
        	.defaultValue('default')
        	.validation({ required: true })
	]);
    
    project.editionView().fields(project.creationView().fields())
    	.title('Edit Project "{{entry.values.name}}":');
    
    project.showView().fields(project.creationView().fields())
		.title('Project "{{entry.values.name}}":');
    
    admin.addEntity(project);
    
    // attach the admin application to the DOM and execute it
    nga.configure(admin);
}]);