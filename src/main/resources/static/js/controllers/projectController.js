/**
 * Created by George on 19.08.2016.
 */
AmCharts.useUTC = true;

angular.module('app').controller('projectController',
	['EmployeeService',	function (EmployeeService) {

		var self = this;

		EmployeeService.getData().
		then(function(data) {
				chart =  AmCharts.makeChart ("chartdiv", data );
			},
			function(reason) {
				self.error = reason;
			});
	}]);





