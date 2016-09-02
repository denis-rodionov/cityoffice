angular
	.module('app')
	.controller('projectController', projectController);

projectController.$inject = ['EmployeeService', 'ProjectService', '$filter'];

function projectController(EmployeeService, ProjectService, $filter) {
	var self = this;

	self.projects = [];
	self.selectedProject = null;

	self.onProjectSelected = onProjectSelected;
	self.plotData = plotData;
	self.fillUserProject = fillUserProject;
	self.getColor = getColor;
	self.getConfig = getConfig;

	activate();

	///////////////////////////////////////////////////////

	function activate() {
		var allProjects = ($filter('translate'))("ALL_PROJECTS");

		self.projects.push({ id: null, name: allProjects });
		self.selectedProject = self.projects[0];

		AmCharts.useUTC = true;

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
	}

	function onProjectSelected() {
		EmployeeService.getUserProjects(self.selectedProject.id)
			.then(plotData);
	}

	function plotData(employees) {
		fillUserProject(employees);
		self.employees = employees;
		chartEmployeesData =  {"dataProvider": self.employees };
		getConfig();
		var dst = {};
		angular.extend(dst, chartEmployeesData, chartconfig);
		AmCharts.makeChart("chartdiv", dst);
	}

	function fillUserProject(employees) {
		for (var i = 0; i < employees.length; i++) {
			for (var j = 0; j < employees[i].projects.length; j++) {
				employees[i].projects[j]['color'] = getColor(employees[i].projects[j].workload);
			}
		}
	}

	function getColor(workload) {
		var res = null;
		if (workload <= 0 )
			res = '#FE2E2E';
		else if (workload < 20 )
			res = '#CFE6FB';
		else if (workload < 40)
			res = '#A9D0F5' ;
		else if (workload < 60)
			res = '#2E9AFE';
		else if (workload < 80)
			res = '#045FB4';
		else if (workload < 100 )
			res = '#0B2161';
		else if (workload >= 100)
			res = '#0B0B3B';
		return res
	}

	function getConfig() {
		var ballon = "<b>[[name]]</b> " + ($filter('translate'))("IN_PROJECT") + " <b>[[projectName]]</b>: <p>[[open]] - [[value]]</p>  " + ($filter('translate'))("WORKLOAD") + ": [[workload]]%";
		chartconfig = {
			"language": "ru",
			"type": "gantt",
			"theme": "dark",
			"period": "DD",
			"marginRight": 70,
			"columnWidth": 0.5,
			"valueAxis": {
				"type": "date",
				"minPeriod": "DD"
			},
			"brightnessStep": 10,
			"graph": {
				"fillAlphas": 1,
				"balloonText": ballon
			},
			"rotate": true,
			"categoryField": "name",
			"segmentsField": "projects",
			"colorField": "color",
			"dataDateFormat": "YYYY-MM-DD",
			"startDateField": "startDate",
			"endDateField": "finishDate",
			"chartCursor": {
				"cursorColor": "#55bb76",
				"valueBalloonsEnabled": false,
				"cursorAlpha": 0,
				"valueLineAlpha": 0.5,
				"valueLineBalloonEnabled": true,
				"valueLineEnabled": true,
				"zoomable": false,
				"valueZoomable": true
			}
		}
	}

}


