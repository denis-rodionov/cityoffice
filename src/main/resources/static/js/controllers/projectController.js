/**
 * Created by George on 19.08.2016.
 */
angular.module('app').controller('projectController',
	['EmployeeService', '$scope',	function (EmployeeService, $scope) {

		var self = this;
		AmCharts.useUTC = true;

		EmployeeService.getConfig()

			.then(function(data) {
				self.chartconfig = data;

			},
				function(reason) {
					self.error = reason;
				});

		EmployeeService.getUserProjects()

			.then(function (data) {
				self.employees = data;
				chartemployeesdata =  {"dataProvider": self.employees };

				var dst = {};
				angular.extend(dst, chartemployeesdata, self.chartconfig);
				AmCharts.makeChart("chartdiv", dst);


				},
					function (reason) {
						self.error = reason;

					});

		EmployeeService.getIDProjects()

			.then (function (data) {

				self.projecsID = data;
					projects_iddata = data;

					$scope.filterOptions = {
						IDs: projects_iddata
					};
					$scope.filterItem = {
						IDs: $scope.filterOptions.IDs[0]
					}

			},
				function (reason) {
					self.error = reason;
				}
			);



	}]);


