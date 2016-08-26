/**
 * Created by George on 19.08.2016.
 */
angular.module('app').controller('projectController',
	['EmployeeService',	function (EmployeeService) {

		var self = this;
		AmCharts.useUTC = true;

		EmployeeService.getUserProjects()
			.then(function(data) {

				chart =  AmCharts.makeChart ("chartdiv",data ); /* chart formation */
			},

			function(reason) {
				self.error = reason;
			});
	}]);