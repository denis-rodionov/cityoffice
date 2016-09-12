// declare a new module called 'myApp', and make it require the `ng-admin` module as a dependency
var myApp = angular.module('myApp', ['ng-admin', 'pascalprecht.translate']);

//------------ i18n ----------------------
myApp.config(['$translateProvider', function($translateProvider) {
	$translateProvider.useStaticFilesLoader({
        prefix: '/i18n/',
        suffix: '.json?'
    });
	
    $translateProvider.preferredLanguage('ru');
    $translateProvider.useSanitizeValueStrategy('sanitize');
}]);

// ------------- Request converting ---------------------------
myApp.config(['RestangularProvider', function(RestangularProvider) {
    RestangularProvider.addFullRequestInterceptor(function(element, operation, what, url, headers, params, httpConfig) {
    	
    	if(what == 'notification_schema' && (operation == 'post' || operation == 'put' ) ) {
    		
	    	var res = [];
	    	var source = element.notifications;
	    	
	    	for (var i = 0; i < source.length; i++) {
	    		res.push({"id" : source[i]});
	    	}
	    	
	    	element.notifications = res;
        }
    	
    	// extracting filtering params from a map to a query params
    	if (operation == 'getList') {
            if (params._filters) {
                for (var filter in params._filters) {
                    params[filter] = params._filters[filter];
                }
                delete params._filters;
                
                return { params: params };
            }
        }        
    	
        return { element: element };
    });
}]);

// -------------- Response converting -------------------------
myApp.config(['RestangularProvider', function(RestangularProvider) {

    RestangularProvider.addElementTransformer('notification_schema', function(element) {
        //console.log(element.notifications);
        
    	var source = element.notifications;
    	var result = [];
    	if (source) {    		
    		for (var i = 0; i < source.length; i++)
    			result.push( source[i].id );
    	}
    	element['notifications'] = result;
        return element;
    });
}]);

// declare a function to run when the module bootstraps (during the 'config' phase)
myApp.config(['NgAdminConfigurationProvider', function (nga) {	
	
    // create an admin application
    var admin = nga.application('Admin Tool', true).debug(true);
    admin.header(getHeader());
    // more configuation here later
    
    
    // --------- PROJECTS -----------------------------------
    var project = nga.entity('project').label('Project');
    
    project.listView().fields([
	                          nga.field('name')
	                          	.label("Name")
	                          	.editable(true)
	                          	.isDetailLink(true),
	                          nga.field('isActive', 'boolean')
	                          	.label('Active'),	                          	
	                          nga.field('colorName')
	                          	.label('Color')
                            ])    	
    .filters([
         nga.field('name')
         	.label('Name'),
     	 nga.field('isActive', 'boolean')
         	.label('Is Active')
     ])
    .sortField('isActive')
    .sortDir("DESC");
    
    
    project.creationView().fields([
        nga.field('name')
        	.label('Project Name')
        	.validation({ required: true, minlength: 1, maxlength: 200}),
        nga.field('isActive', 'boolean')
        	.label('Active')
        	.validation({ required: true })        	
        	.defaultValue(true),
        nga.field('colorName', 'choice')
        	.choices([
        	    { value: 'primary', label: 'primary'},
        	    { value: 'success', label: 'success'},
        	    { value: 'info', label: 'info'},
        	    { value: 'warning', label: 'warning'},
        	    { value: 'danger', label: 'danger'}
        	])
        	.label('Color Scheme')
        	.defaultValue('default')
        	.validation({ required: true })
	])	;
    
    project.editionView().fields(project.creationView().fields())
    	.title('{{"EDIT_PROJECT" | translate}} "{{entry.values.name}}":');
    
    project.showView().fields(project.creationView().fields())
		.title('"{{PROJECT | translate}}" "{{entry.values.name}}":');
    
    project.deletionView().title('Delete project "{{entry.values.name}}":');
    
    admin.addEntity(project);
    
    // ------------- NOTIFICATIONS  -------------------------------
    
    var notification = nga.entity('notification')
    	.label('Notifications');
    
    notification.listView().fields([
        nga.field('name').label('Name').isDetailLink(true),
        nga.field('daysBefore', 'number').label("Notification period").editable(true)
    ]);
    
    notification.editionView().fields(notification.listView().fields())
    	.title('Edit notification "{{entry.values.name}}":');
    
    notification.creationView().fields(notification.listView().fields())
		.title('Creaing notification');
    
    notification.deletionView().title('Delete notification "{{entry.values.name}}":');
    
    admin.addEntity(notification);
    
    // ------------- NOTIFICATION SCHEMAS -------------------------
    
    var notification_schema = nga.entity('notification_schema')
    	.label('Notification Schemas');
    
    notification_schema.listView().fields([
        nga.field('name')
        	.label('Schema Name')
        	.editable(true)
        	.isDetailLink(true),
        nga.field('notifications', 'reference_many')
            .targetEntity(notification)
            .targetField(nga.field('name'))
            .label('Notifications')
    ]);
    
    notification_schema.editionView().fields(notification_schema.listView().fields())
    	.title('Edit notification schema "{{entry.values.name}}":');
    
    notification_schema.creationView().fields(notification_schema.listView().fields())
	.title('Create notification schema "{{entry.values.name}}":');
    
    notification_schema.deletionView().title('Delete notification schema "{{entry.values.name}}":');
    
    admin.addEntity(notification_schema);
    
// ------------- USERS -------------------------
    
    var user = nga.entity('user')
		.label("Users");
    
    user.listView().fields([
        nga.field('username')
           .label('Full name')
           .isDetailLink(true)
           .validation({ required : true }),
        nga.field('email', 'email')
           .label('E-mail')
           .validation({ required : true }),
        nga.field('projectIds', 'reference_many')
           .targetEntity(project)
           .targetField(nga.field('name'))
           .label('Projects'),
        nga.field('role')
       	   .label('Role'),
       	nga.field('managerId', 'reference')
       	   .targetEntity(user)
       	   .targetField(nga.field('username'))
       	   .label('Manager'),
       	nga.field('hours', 'number')
       	   .label('Hours a week') 
       	   
    ])
    .filters([
         nga.field('role', 'choice')
             .label('Role')
             .defaultValue('ADMIN')
             .choices([
			        	    { value: 'USER', label: 'USER'},
			        	    { value: 'ADMIN', label: 'ADMIN'}
			    ]),
         nga.field('username')
         	.label('Name'),
     	 nga.field('email')
         	.label('Email'),
         nga.field('project', 'reference')
         	.label('Project')
         	.targetEntity(project)
         	.targetField(nga.field('name'))
     ]);
    
    user.creationView().fields([
       nga.field('username')
           .label('Full name')
           .validation({ required : true }),
       nga.field('email', 'email')
           .label('E-mail')
           .validation({ required : true }),
       nga.field('projectIds', 'reference_many')
       	   .targetEntity(project)
       	   .targetField(nga.field('name'))   	
       	   .label('Projects'),
       nga.field('role', 'choice')
       	   .label('Role')
       	   .choices([
        	    { value: 'USER', label: 'USER '},
        	    { value: 'ADMIN', label: 'ADMIN'}
		    ])
		   .defaultValue('USER')
		   .validation({ required : true }),
       nga.field('password', 'password')
       	   .label('Password')
       	   .validation({ required : true }),
       nga.field('managerId', 'reference')
       	   .label('Manager')
       	   .targetEntity(user)
       	   .targetField(nga.field('username')),
       nga.field('hours', 'number')
       	   .defaultValue(40)
    	   .label('Hours a week')
    	   
    ]);
    
    user.showView().fields([
        nga.field('username')
            .label('Full name')
            .validation({ required : true }),
        nga.field('email', 'email')
            .label('E-mail')
            .validation({ reuqired : true })
     ]);
    
    user.editionView().fields(user.creationView().fields())
		.title('Edit User "{{entry.values.username}}":');
    
    user.deletionView().title('Delete user "{{entry.values.username}}":');
    
    admin.addEntity(user);    
    
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
          	.targetField(nga.field('name'))
          	.label('Notification Schema'),
          nga.field('assigneeId', 'reference')
            .targetEntity(user)
        	.targetField(nga.field('username'))
        	.label('Responsible')
         ])
         .filters([
             nga.field('status', 'choice')
                 .label('Status')
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
             	.targetField(nga.field('name')),
             nga.field('assignee', 'reference')
             	.label('Assignee')
             	.targetEntity(user)
             	.targetField(nga.field('username'))
         ])
         .sortField('status')
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
           	.targetField(nga.field('name'))           	
           	.label('Notification Schema'),
        nga.field('assigneeId', 'reference')
           	.label('Responsible')
           	.targetEntity(user)
           	.targetField(nga.field('username')),
        nga.field('description', 'text')
        	.label('Description')
    ]);
    
    document.editionView().fields(document.creationView().fields())
		.title('Edit Document "{{entry.values.name}}":');
    
    document.deletionView().title('Delete document "{{entry.values.name}}":');
    
    admin.addEntity(document);
    
    // ------------- USER PROJECTS -------------------------
    var employee_projects = nga.entity('user_project')
		.label('Employees Projects');

    employee_projects.listView().fields([
		nga.field('userId', 'reference')
			.label('Employee')
			.targetEntity(user)
			.targetField(nga.field('username'))
			.validation({ required : true }),
		nga.field('projectId', 'reference')
			.targetEntity(project)
			.targetField(nga.field('name'))
			.label('Project')
			.validation({ required : true }),		
        nga.field('load', 'float')
        	.label("Workload % (0..1)")
        	.format('0%')
        	.validation({ required : true })
        	.defaultValue(1),
    	nga.field('startDate', 'date')
			.label('Start Date')
			.format('dd.MM.yyyy')
			.validation({ required : true }),
		nga.field('finishDate', 'date')
			.label('Finish Date')
			.format('dd.MM.yyyy')
			.validation({ required : true })
	])
	.sortField('startDate')
    .sortDir('DESC')
	.listActions(['edit'])
	.filters([
         nga.field('project', 'reference')
         	.label('Project')
         	.targetEntity(project)
         	.targetField(nga.field('name')),
         nga.field('user', 'reference')
         	.label('Employee')
         	.targetEntity(user)
         	.targetField(nga.field('username'))
     ]);
     
	
	employee_projects.editionView().fields(employee_projects.listView().fields())
		.title('Edit employee project "{{entry.values.name}}":');
	
	employee_projects.creationView().fields(employee_projects.listView().fields())
		.title('Create employee project"{{entry.values.name}}":');
	
	employee_projects.deletionView().title('Delete employee project "{{entry.values.name}}":');
	
	admin.addEntity(employee_projects);
	
	// ------------- USER VACATIONS -------------------------
    var employee_vacations = nga.entity('user_vacation')
		.label('Employees Vacations');

    employee_vacations.listView().fields([
		nga.field('userId', 'reference')
			.label('Employee')
			.targetEntity(user)
			.targetField(nga.field('username'))
			.validation({ required : true }),
    	nga.field('startDate', 'date')
			.label('Start Date')
			.format('dd.MM.yyyy')
			.validation({ required : true }),
		nga.field('finishDate', 'date')
			.label('Finish Date')
			.format('dd.MM.yyyy')
			.validation({ required : true })
	])
	.sortField('startDate')
    .sortDir('DESC')
	.listActions(['edit'])
	.filters([
         nga.field('user', 'reference')
         	.label('Employee')
         	.targetEntity(user)
         	.targetField(nga.field('username'))
     ]);
     
	
    employee_vacations.editionView().fields(employee_vacations.listView().fields())
		.title('Edit employee vacation "{{entry.values.name}}":');
	
    employee_vacations.creationView().fields(employee_vacations.listView().fields())
		.title('Create employee vacation"{{entry.values.name}}":');
	
    employee_vacations.deletionView().title('Delete employee vacation "{{entry.values.name}}":');
	
	admin.addEntity(employee_vacations);
	
	// ------  GENERAL CONFIGURATION -------------------------
    
    
    // attach the admin application to the DOM and execute it
    nga.configure(admin);
    
    function getHeader() {
    	var res =   	'\
    		<div class="container"> \
    		<ul class="nav nav-pills" role="tablist"> \
    			<li><a href="index.html">{{"DOCUMENTS" | translate}}</a></li> \
    			<li><a href="index.html#/projects">{{"PROJECTS" | translate}}</a></li> \
				<li><a href="index.html#/employees">{{"EMPLOYEES" | translate}}</a></li> \
    			<li class="active"><a>{{"SETTINGS" | translate}}</a></li> \
    			<li><a>{{"LOG_OUT" | translate}}</a></li> \
    		</ul> \
    	</div> \
		';
    	return res;
    }
}]);
