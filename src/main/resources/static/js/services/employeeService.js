angular
	.module('services')
	.service('EmployeeService', ['$http','$q', function ($http, $q) {
		self = this;
		
		self.getUserProjects = getUserProjects;
		self.getEmployees = getEmployees;
		self.getEmployee = getEmployee;
		
		///////////////////////////////////////////////////////

		function getUserProjects(projectId) {
			var deferred = $q.defer();

			$http.get('/employee', { params: { projectId: projectId , onlyProjects: false} })
				.then(function (response) {
					deferred.resolve(processUserProjects(response.data));
				}, function (error) {
					deferred.reject('Error retrieving list of documents');
				});
			return deferred.promise;
		};



		function getEmployees() {
			var deferred = $q.defer();

			$http.get('/employee')
				.then(function (response) {
					deferred.resolve(response.data);
				}, function (error) {
					deferred.reject('Error retrieving list of documents');
				});

			return deferred.promise;
		};

		function getEmployee(id) {
			var deferred = $q.defer();

			$http.get('/employees/' + id)
				.then(function(response) {
					if (response.status == 200) {
					}
					else {
						deferred.reject('Error retrieving list of documents');
					}
				});

			return deferred.promise;
		};
		
		/**
         * Process server response
         */
        function processUserProjects(employees) {
        	employees.forEach(function(employee) {
        		employee.projects.forEach(function(project) {
        			project.startDate = new Date(project.startDate);
        			project.finishDate = new Date(project.finishDate);
        		});
        		
        	});
        	return employees;
        }
}]);

