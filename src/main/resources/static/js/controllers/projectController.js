/**
 * Created by George on 19.08.2016.
 */
angular.module('app').controller('projectController',
	['EmployeeService', 'ProjectService','$scope',	function (EmployeeService, ProjectService) {

		var self = this;
		self.projects =  [{ id: null, name: 'All' }] ;
		self.selectedProject =  null;
		self.onProjectSelected = onProjectSelected;
		AmCharts.useUTC = true;

		EmployeeService.getConfig()

			.then(function(data) {
				self.chartconfig = data;

			},
				function(reason) {
					self.error = reason;
				});

		EmployeeService.getUserProjects()


			.then(plotData,
					function (reason) {
						self.error = reason;

					});

		ProjectService.getDataProjects()

			.then(function(data) {

					for (var i=0; i<data.length; i++){
						self.projects.push(data[i]);
					}
				},
				function(reason) {
					self.error = reason;
				});

		function onProjectSelected() {

			EmployeeService.getUserProjects(self.selectedProject.id)
				.then(plotData);

		}

		function plotData(employees) {
			self.employees = employees;
			chartEmployeesData =  {"dataProvider": self.employees };

			var dst = {};
			angular.extend(dst, chartEmployeesData, self.chartconfig);
			AmCharts.makeChart("chartdiv", dst);
		}


	}]);


