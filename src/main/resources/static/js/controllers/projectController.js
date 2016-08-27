/**
 * Created by George on 19.08.2016.
 */
angular.module('app').controller('projectController',
	['EmployeeService',	function (EmployeeService) {

		var self = this;
		AmCharts.useUTC = true;

		EmployeeService.getUserProjects()
			.then(function(data) {
					self.employees = data;
					AmCharts.makeChart("chartdiv", data);

				},

				function(reason) {
					self.error = reason;
				});
	}]);