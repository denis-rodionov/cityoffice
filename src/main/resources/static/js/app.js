angular.module('app', [ 'ngRoute', 'services'])
	  .config(function($routeProvider, $httpProvider) {
	
	    $routeProvider.when('/', {
	      templateUrl : 'home.html',
	      controller : 'documentController',
	      controllerAs: 'controller'
	    }).when('/login', {
	      templateUrl : 'login.html',
	      controller : 'navigation',
	      controllerAs: 'controller'
	    }).otherwise('/');
	
	    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
	
	  })
  	.controller('documentController', ['$scope', 'DocumentService',	function($scope, DocumentService) {
  		var self = this;
  		
  		DocumentService.getAllDocuments()
  			.then(function(data) {
  				self.documents = data;
  			}); 		
  		
  		
  		//$http.get('/document/').then(function(response) {
  		//	self.greeting = response.data;
  		//});
    }])
  	.controller('navigation',
  		function($rootScope, $http, $location) {

  		  var self = this;

  		  var authenticate = function(credentials, callback) {

  		    var headers = credentials ? {authorization : "Basic "
  		        + btoa(credentials.username + ":" + credentials.password)
  		    } : {};

  		    $http.get('user', {headers : headers}).then(function(response) {
  		      if (response.data.name) {
  		        $rootScope.authenticated = true;
  		      } else {
  		        $rootScope.authenticated = false;
  		      }
  		      callback && callback();
  		    }, function() {
  		      $rootScope.authenticated = false;
  		      callback && callback();
  		    });

  		  }

  		  authenticate();
  		  self.credentials = {};
  		  
  		  self.login = function() {
  		      authenticate(self.credentials, function() {
  		        if ($rootScope.authenticated) {
  		          $location.path("/");
  		          self.error = false;
  		        } else {
  		          $location.path("/login");
  		          self.error = true;
  		        }
  		      });
  		  };
  		  
	  	  self.logout = function() {
	  		  $http.get('/logout', {}).finally(function() {
	  		    $rootScope.authenticated = false;
	  		    $location.path("/");
	  		  });
	  		};
  		});
