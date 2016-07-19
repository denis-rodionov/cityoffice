angular.module('app', [ 'ngRoute', 'services'])
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
	    }).when('/login', {
	      templateUrl : 'login.html',
	      controller : 'navigation',
	      controllerAs: 'controller'
	    }).when('/admin', {
	      redirectTo : 'admin.html'
	    }).otherwise('/');
	
	    //$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
	
	  })    
  	.controller('navigation',function($rootScope, $http, $location) {  		
  		  console.log("navigation controller: " + $location.path());
  		  
  		  var self = this;
  		  
  		  $rootScope.menuClass = function(page) {
  		    var current = $location.path().substring(1);
  		    return page === current ? "active" : "";
  		  };

  		  var authenticate = function(credentials, callback) {
  			  
  			console.log("authenticate() { credentials : " + credentials + "}");

  		    var headers = credentials ? {authorization : "Basic "
  		        + btoa(credentials.username + ":" + credentials.password)
  		    } : {};

  		    console.log("sending request...");
  		    $http.get('getuser', {headers : headers}).then(function(response) {
  		      console.log("getuser succedd");
  		      if (response.data.name) {
  		        $rootScope.authenticated = true;
  		      } else {
  		        $rootScope.authenticated = false;
  		      }
  		      callback && callback();
  		    }, function() {
  		      console.log("getuser failed");
  		    	
  		      $rootScope.authenticated = false;
  		      callback && callback();
  		    });

  		  }

  		  authenticate();
  		  self.credentials = {};
  		  
  		  self.login = function() {
  			  console.log("login");
  		      authenticate(self.credentials, function() {
  		    	console.log("authentificate callback");
  		    	
  		    	
  		        if ($rootScope.authenticated) {
  		          console.log("redirection to documents");
  		          $location.path("/");
  		          self.error = false;
  		        } else {
  		          console.log("redirection to login page");
  		          $location.path("/login");
  		          self.error = true;
  		        }
  		      });
  		  };
  		  
	  	  self.logout = function() {
	  		  $http.post('/logout', {}).finally(function() {
	  		    $rootScope.authenticated = false;
	  		    $location.path("/");
	  		  });
	  		};
  		});
