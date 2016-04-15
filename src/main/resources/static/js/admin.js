// declare a new module called 'myApp', and make it require the `ng-admin` module as a dependency
var myApp = angular.module('myApp', ['ng-admin']);
// declare a function to run when the module bootstraps (during the 'config' phase)
myApp.config(['NgAdminConfigurationProvider', function (nga) {
    // create an admin application
    var admin = nga.application('Администратор');
    // more configuation here later
    
    var project = nga.entity('project')
    	.label("Проекты");
    
    project.listView().fields([
	                          nga.field('name')
	                          	.label('Название проекта')
	                          	.editable(true),
	                          nga.field('active', 'boolean')
	                          	.label('Активный'),	                          	
	                          nga.field('colorName')
	                          	.label('Цвет')
                            ]);
    admin.addEntity(project);
    
    // attach the admin application to the DOM and execute it
    nga.configure(admin);
}]);