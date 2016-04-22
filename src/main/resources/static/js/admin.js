// declare a new module called 'myApp', and make it require the `ng-admin` module as a dependency
var myApp = angular.module('myApp', ['ng-admin']);
// declare a function to run when the module bootstraps (during the 'config' phase)
myApp.config(['NgAdminConfigurationProvider', function (nga) {
    // create an admin application
    var admin = nga.application('Admin Tool', true).debug(true);
    admin.header(getHeader());
    // more configuation here later
    
    
    // --------- PROJECTS -----------------------------------
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
    
    // ------------- NOTIFICATION SCHEMAS -------------------------
    
    var notification_schema = nga.entity('notification_schema')
    	.label('Notification Schema');
    
    notification_schema.listView().fields([
        nga.field('name')
        	.label('Schema Name')
        	.editable(false)
        	.isDetailLink(true),
        nga.field('description')
        	.label('Description')
    ]);
    
    notification_schema.editionView().fields(notification_schema.listView().fields())
    	.title('Edit schema "{{entry.values.name}}":');
    
    admin.addEntity(notification_schema);
    
    // ------------- DOCUMENTS -------------------------
    
    var document = nga.entity('document')
		.label("Documents");
    
    document.listView().fields([
 	                          nga.field('name')
 	                          	.label('Name')
 	                          	.editable(true)
 	                          	.isDetailLink(true),
 	                          nga.field('deadline', 'date')
 	                          	.label('Deadline')
 	                          	.format('dd.MM.yyyy'),
 	                          nga.field('status')
 	                          	.label('Status'),
 	                          nga.field('projectId', 'reference')
 	                          	.targetEntity(project)
 	                          	.targetField(nga.field('name'))
 	                          	.label('Project Name'),
 	                          nga.field('notificationSchemaId', 'reference')
 	                          	.targetEntity(notification_schema)
 	                          	.targetField(nga.field('description'))
 	                          	.label('Notification Schema')
                             ])
                             .filters([
                                 nga.field('status', 'choice')
                                     .label('Status')
                                     .pinned(true)
                                     .defaultValue('NEW')
                                     .choices([
									        	    { value: 'NEW', label: 'NEW '},
									        	    { value: 'FINISHED', label: 'FINISHED'}
									    ]),
                                 nga.field('name')
                                 	.label('Name'),
                                 nga.field('project', 'reference')
                                 	.label('Project')
                                 	.targetEntity(project)
                                 	.targetField(nga.field('name'))
                             ])
                             .sortField(nga.field('status'))
                             .sortDir('DESC');
    
    document.creationView().fields([
		nga.field('name')
			.label('Name')
			.validation({ required : true, maxLength: 30}),
		nga.field('deadline', 'date')
			.label('Deadline')
			.format('dd.MM.yyyy')
			.validation({ required : true}),
		nga.field('status', 'choice')
			.label('Status')
			.choices([
		        	    { value: 'NEW', label: 'NEW '},
		        	    { value: 'FINISHED', label: 'FINISHED'}
		    ])
		    .defaultValue('NEW')
		    .validation({ required : true }),
		nga.field('projectId', 'reference')
			.targetEntity(project)
			.targetField(nga.field('name'))
			.label('Project')
			.validation({ required: true }),
		nga.field('notificationSchemaId', 'reference')
           	.targetEntity(notification_schema)
           	.targetField(nga.field('description'))
           	.label('Notification Schema')
    ]);
    
    document.editionView().fields(document.creationView().fields())
		.title('Edit Document "{{entry.values.name}}":');
    
    admin.addEntity(document);
    
// ------------- USERS -------------------------
    
    var user = nga.entity('user')
		.label("Users");
    
    user.listView().fields([
        nga.field('username')
           .label('Username')
           .validation({ required : true }),
        nga.field('email', 'email')
           .label('E-mail')
           .validation({ reuqired : true })
    ]);
    
    user.creationView().fields([
       nga.field('username')
           .label('Username')
           .validation({ required : true }),
       nga.field('email', 'email')
           .label('E-mail')
           .validation({ reuqired : true })
    ]);
    
    user.editionView().fields(user.creationView().fields())
		.title('Edit User "{{entry.values.name}}":');
    
    admin.addEntity(user);
    
    // attach the admin application to the DOM and execute it
    nga.configure(admin);
    
    function getHeader() {
    	var res =   	'\
    		<div class="container"> \
    		<ul class="nav nav-pills" role="tablist"> \
    			<li><a href="index.html">Documents</a></li> \
    			<li class="active"><a>Settings</a></li> \
    			<li><a>Log Out</a></li> \
    		</ul> \
    	</div> \
		';
    	return res;
    }
}]);