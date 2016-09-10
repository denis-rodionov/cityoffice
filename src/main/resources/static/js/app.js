angular.module('app', [ 'ngRoute', 'services', 'pascalprecht.translate'])
	  .config(function($routeProvider, $httpProvider) {
		  
		//$locationProvider.html5Mode(true);
	
	    $routeProvider.when('/', {
	      templateUrl : 'documents.html',
	      controller : 'documentController',
	      controllerAs: 'controller'
	    }).when('/projects', {
	      templateUrl : 'projects.html',
	      controller : 'projectController',
	      controllerAs : 'controller'
	    }).when('/employees', {
	      templateUrl : 'employees.html',
	      controller : 'employeeController',
	      controllerAs : 'controller'
	    }).when('/employees/:employeeId', {
		      templateUrl : 'employees.html',
		      controller : 'employeeController',
		      controllerAs : 'controller'
	    }).when('/login', {
	      templateUrl : 'login.html',
	      controller : 'navigation',
	      controllerAs: 'controller'
	    }).when('/admin', {
	      redirectTo : 'admin.html'
	    }).otherwise('/');
	
	    //$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
	
	  })  
	 .config(['$translateProvider', function($translateProvider) {
	    //$translateProvider.addInterpolation('$translateMessageFormatInterpolation');
	
	    $translateProvider.useStaticFilesLoader({
	        prefix: '/i18n/',
	        suffix: '.json?' + (new Date().getTime()) // no-cache for i18n
	    });
	    $translateProvider.preferredLanguage('ru');
	    $translateProvider.usePostCompiling(true);
	    //$translateProvider.useSanitizeValueStrategy('sanitize');
	}])
  	
