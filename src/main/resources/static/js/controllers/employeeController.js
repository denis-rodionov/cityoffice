/**
 * Projects Controller
 * @namespace Controller
 */
angular
	.module('app')
	.controller('employeeController', employeeController);
	
employeeController.$inject = ['$scope', 'EmployeeService', '$routeParams'];

function employeeController($scope, EmployeeService, $routeParams) { 
    	
    var vm = this; 
    
    vm.employees = [];
    vm.selectedEmployee = null;
    
    vm.onEmployeeSelected = onEmployeeSelected;
    
    activate();
    
    ///////////////////////////////////////////////////////
    
    function activate() {
    	EmployeeService.getEmployees().
			then(function(data) {
				vm.employees = data;
				
				if (typeof $routeParams.employeeId !== "undefined") 
					vm.selectedEmployee = getEmployeeById($routeParams.employeeId);
				else {				
					if (vm.employees.length >= 0)
						vm.selectedEmployee = vm.employees[0];
				}
			},
			function(reason) {
				vm.error = reason;
			}); 
    }
    
    function getEmployeeById(id) {
    	return vm.employees.find(function(e) { 
    		return e.id == id 
    	});
    }
    
    /**
     * @name on click by the employees list
     * @param {Employee} selected employee
     */
    function onEmployeeSelected(employee) {
    	vm.selectedEmployee = employee;
    }
}