angular.module('services')
	.service('EmployeeService', ['$http','$q', function ($http, $q) {

		this.getUserProjects = function () {
			var deferred = $q.defer();

			$http.get('employees_data.json')
				.then(function (response) {
					deferred.resolve(response.data);
				}, function (error) {
					deferred.reject('Error retrieving list of documents');
				});

			return deferred.promise;
		};

		this.getEmployees = function() {
			var deferred = $q.defer();

			$http.get('/employees/')
				.then(function(response) {
					if (response.status == 200) {
						deferred.resolve(response.data);
					}
					else {
						deferred.reject('Error retrieving list of documents');
					}
				});

			return deferred.promise;
		};

		this.getEmployee = function(id) {
			var deferred = $q.defer();

			$http.get('/employees/' + id)
				.then(function(response) {
					if (response.status == 200) {
						deferred.resolve(response.data);
					}
					else {
						deferred.reject('Error retrieving list of documents');
					}
				});

			return deferred.promise;
		};
	}]);