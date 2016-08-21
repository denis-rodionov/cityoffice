angular.module('services', [])

	.service('EmployeeService', ['$http','$q', function ($http, $q) {
		return {
			getData: function () {
				var deferred = $q.defer();

				$http.get('data.json')
					.then(function (response) {

						if (response.status == 200) {
							deferred.resolve(response.data);
						}
						else deferred.reject('Error retrieving list of documents');

					});

				return deferred.promise;
			}
		};

	}]);