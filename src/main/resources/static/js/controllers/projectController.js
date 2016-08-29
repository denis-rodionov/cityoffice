/**
 * Created by George on 19.08.2016.
 */
angular.module('app').controller('projectController',
	['EmployeeService',	function (EmployeeService) {

		var self = this;
		AmCharts.useUTC = true;

		EmployeeService.getConfig()

			.then(function(data) {

				self.employees = data;
				configdata = data;


			},
				function(reason) {
					self.error = reason;
				});

		EmployeeService.getUserProjects()

			.then(function (data) {

				self.employees = data;

				dst = {};
				angular.extend(dst, data, configdata);
				AmCharts.makeChart("chartdiv", dst);


				})




	}]);
