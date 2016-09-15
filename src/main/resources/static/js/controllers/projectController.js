/**
 * Projects Controller
 * @namespace Controller
 */
angular
	.module('app')
	.controller('projectController', projectController);

projectController.$inject = ['EmployeeService', 'ProjectService', '$filter'];
/**
 * @namespace projectController
 */
function projectController(EmployeeService, ProjectService, $filter) {
	var self = this;

	self.projects = [];
	self.selectedProject = null;
	AmCharts.useUTC = true;

	self.onProjectSelected = onProjectSelected;
	self.plotData = plotData;
	self.fillUserProject = fillUserProject;
	self.getColor = getColor;
	self.getConfig = getConfig;

	/**
	 * @namespace projectController
	 * @desc initialization
	 * @memberOf projectController
	 */
	activate();

	///////////////////////////////////////////////////////

	/**
	 * @namespace activate
	 * @desc initialization functions
	 * @memberOf projectController
	 */
	function activate() {
		var allProjects = ($filter('translate'))("ALL_PROJECTS");

		self.projects.push({ id: null, name: allProjects });
		self.selectedProject = self.projects[0];

		/**
		 *
		 */
		EmployeeService.getUserProjects()
			.then(plotData,
				function (reason) {
					self.error = reason;

				});
		/**
		 *
		 */
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

	/**
	 *@name onProjectSelected
	 * @desc ng-change for drop down list
	 * @param {string} self.selectedProject.id
	 * @memberOf activate.projectController
	 */
	function onProjectSelected() {
		EmployeeService.getUserProjects(self.selectedProject.id)
			.then(plotData);
	}

	/**@name plotData
	 * @desc generate chart w
	 * @param {Object} chartEmployeesData
	 * @param {array} employees
	 * @memberOf activate.projectController
	 */
	function plotData(employees) {
		employees.forEach(mergeProjectsIntersections);
		
		if (self.selectedProject.id == null) {	// if all projects selected: show vacations also
			injectVacancies(employees);
		}
		
		fillUserProject(employees);
		self.employees = employees;
		chartEmployeesData =  {"dataProvider": self.employees };
		getConfig();
		var dst = {};
		angular.extend(dst, chartEmployeesData, chartconfig);
		AmCharts.makeChart("chartdiv", dst);
		
		var valueAxis = new AmCharts.ValueAxis();
		valueAxis.autoGridCount = false;
		valueAxis.gridCount = 50;
		// since we increased the number of grid lines dramatically, let's make the label display only on each 10th of them
		valueAxis.labelFrequency = 50;
	}

	/**
	 * Push color for each one of employees.projects
	 * @param {array} employees
	 */
	function fillUserProject(employees) {
		for (var i = 0; i < employees.length; i++) {
			for (var j = 0; j < employees[i].projects.length; j++) {
				employees[i].projects[j]['color'] = getColor(employees[i].projects[j].workload);
			}
		}
	}
	
	/**
	 * Merge projects intersections by dates
	 * @param {Employee} employee
	 */
	function mergeProjectsIntersections(employee, index) {
		var temp = [];
		//employee.projects.forEach(function(p) { temp.push(p)} );
		temp = temp.concat(employee.projects);
		employee.projects = []
		
		while (temp.length > 0) {
			var prj = temp.pop();
			var intersectionFound = false;
			
			for (var j = 0; j < temp.length && !intersectionFound; j++) {
				if (isIntersected(prj, temp[j])) {
					temp = temp.concat(getIntersectionProducts(prj, temp[j]));
					temp.splice(j, 1);	// remove intersected
					intersectionFound = true;
				}
			}
			
			if (!intersectionFound)
				employee.projects.push(prj);
		}
	}
	
	/**
	 * Get 3 project from intersected projects: 1 overlaped part and 2 the rest parts.
	 * @return 3 projects (1 overlaped) in case of intersection
	 * @throw error in case of no overlaping
	 */
	function getIntersectionProducts(project1, project2) {
		if (project1.startDate.getTime() > project2.startDate.getTime())
			return getIntersectionProducts(project2, project1);
		
		// not it is supposed that project1 starts earlier
		if (project2.startDate.getTime() > project1.finishDate.getTime())
			throw "It must be overlaping";
		
		var result = [];
		
		if (project2.finishDate.getTime() > project1.finishDate.getTime()) {	// one overlapping point
			var intersection = project2.startDate;
			
			var newProject = createOverlapedProject(intersection, project1.finishDate, project1, project2);
			
			project2.startDate = project1.finishDate;
			project1.finishDate = intersection;
			
			result = [project1, project2, newProject];
		}
		else {	// project2 inside project1
			var newProject = createOverlapedProject(project2.startDate, project2.finishDate, project1, project2); 
			
			var temp = project1.finishDate;
			project1.finishDate = project2.startDate;
			
			// project2 transform to projec1
			project2.startDate = project2.finishDate;
			project2.finishDate = temp;
			project2.projectName = project1.projectName;
			project2.workload = project1.workload;
			
			result = [project1, project2, newProject];
		}
		return result;
	}
	
	/**
	 * Create new merged project
	 */
	function createOverlapedProject(startDate, finishDate, project1, project2) {
		var res = {};
		res.startDate = startDate;
		res.finishDate = finishDate;
		res.projectName = buildProjectName(project1.projectName, project1.workload) + " / " 
						+ buildProjectName(project2.projectName, project2.workload);
		res.workload = project1.workload + project2.workload;
		
		return res;
	}
	
	/**
	 * Building project name for bubble on the chart
	 * @param {String} existingName Name of the project
	 * @param {int} workload Workload of the project
	 */
	function buildProjectName(existingName, workload) {
		if (existingName.indexOf('%') > -1)
			return existingName;
		else
			return existingName + "(" + workload + "%)";
	}
	
	/**
	 * Determine of the projects intersected
	 * @return true if intersected
	 */
	function isIntersected(project1, project2) {
		return project1.startDate.getTime() > project2.startDate.getTime() 
			&& project1.startDate.getTime() < project2.finishDate.getTime() ||
			   project2.startDate.getTime() > project1.startDate.getTime() 
			&& project2.startDate.getTime() < project1.finishDate.getTime();
	}
	
	/**
	 * @desc HACK: addind vacancies to the project array with workload=0 and projectName='VACANCY'
	 * @param {array} employees
	 */
	function injectVacancies(employees) {
		for (var i = 0; i < employees.length; i++) {
			for (var j = 0; j < employees[i].vacations.length; j++) {
				var vacation = employees[i].vacations[j];
				vacation['workload'] = 0;
				vacation['projectName'] = ($filter('translate')("VACATION_AKK"));
				employees[i].projects.push(vacation);
			}
		}
	}

	/**
	 * @name getColor
	 * @desc generate color for workload range
	 * @param {number} workload
	 * @returns {string} res - The color of strip
	 * @memberOf activate.projectController
	 */
	function getColor(workload) {
		var res = null;
		if (workload <= 0 )
			res = '#00FF17';	// green - vacation (hack)
		else if (workload < 20 )
			res = '#81B1DB';
		else if (workload < 40)
			res = '#A9D0F5';
		else if (workload < 60)
			res = '#2E9AFE';
		else if (workload < 80)
			res = '#045FB4';
		else if (workload <= 100 )
			res = '#0B2161';
		else if (workload < 125)
			res = '#9E2B4D';
		else if (workload < 150)
			res = '#C42D59';
		else if (workload >= 150)
			res ='$FF0000';		// red - highest overload
		return res
	}

	/**
	 * @name getConfig
	 * @desc generation config for chart
	 * @memberOf activate.projectController
	 */
	function getConfig() {
		var balloon = "<b>[[name]]</b> " + ($filter('translate'))("IN_PROJECT") + " <b>[[projectName]]</b>: <p>[[open]] - [[value]]</p>  " + ($filter('translate'))("WORKLOAD") + ": <b>[[workload]]%</b>";
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
				"balloonText": balloon
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