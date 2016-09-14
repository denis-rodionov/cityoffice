angular
	.module('services')
	.service('EmployeeService', ['$http','$q', function ($http, $q) {

		this.getUserProjects = function (projectId) {
			var deferred = $q.defer();

			$http.get('/employee', { params: { projectId: projectId , onlyProjects: false} })
				.then(function (response) {
					deferred.resolve(response.data);
				}, function (error) {
					deferred.reject('Error retrieving list of documents');
				});
			return deferred.promise;
		};



		this.getEmployees = function() {
			var deferred = $q.defer();

			$http.get('/employee')
				.then(function (response) {
					deferred.resolve(response.data);
				}, function (error) {
					deferred.reject('Error retrieving list of documents');
				});

			return deferred.promise;
		};

		this.getEmployee = function(id) {
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



	}]);

