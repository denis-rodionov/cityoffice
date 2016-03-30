angular.module('app', [])
  .controller('home', function($http) {
    var self = this;
    $http.get('/document/').then(function(response) {
    	self.greeting = response.data;
    });
});