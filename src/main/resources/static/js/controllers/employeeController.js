angular.module('app').controller('employeeController', 
		['$scope', 'EmployeeService',	function($scope, EmployeeService) {
    	
    var self = this;   
   
   	EmployeeService.getEmployees().
  			then(function(data) {
  				self.employees = data;
  			},
  			function(reason) {
  				self.error = reason;
  			}); 
}])