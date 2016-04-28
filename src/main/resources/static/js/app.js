angular.module('app', [ 'ngRoute', 'services'])
	  .config(function($routeProvider, $httpProvider) {
		  
		//$locationProvider.html5Mode(true);
	
	    $routeProvider.when('/', {
	      templateUrl : 'home.html',
	      controller : 'documentController',
	      controllerAs: 'controller'
	    }).when('/login', {
	      templateUrl : 'login.html',
	      controller : 'navigation',
	      controllerAs: 'controller'
	    }).when('/admin', {
	      redirectTo : 'admin.html'
	    }).otherwise('/');
	
	    //$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
	
	  })
  	.controller('documentController', ['$scope', 'DocumentService',	function($scope, DocumentService) {
  		var self = this;
  		
  		DocumentService.getDocumentsByMonths().
  			then(function(data) {
  				self.months = data;
  			},
  			function(reason) {
  				self.error = reason;
  			});
  		
  		self.finishDocument = function(document, month) {
  			DocumentService.finishDocument(document.id);
  			
  			// hide the document
  			var index = month.documents.indexOf(document);
  			month.documents.splice(index, 1);
  			
  			// hide month of no mo elements
  			if (month.documents.length == 0) {
  				var index1 = self.months.indexOf(month);
  				self.months.splice(index1, 1);
  			}
  		};
    }])
  	.controller('navigation',
  		function($rootScope, $http, $location) {
  		  console.log("navigation controller");
  		
  		  var self = this;

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
