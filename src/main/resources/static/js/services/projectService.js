angular.module('services')
    .service('ProjectService', ['$http','$q', function ($http, $q) {

        this.getDataProjects = function () {
            var deferred = $q.defer();

            $http.get('/project', { params: { isActive: true }})
                .then(function (response) {
                    deferred.resolve(response.data);
                }, function (error) {
                    deferred.reject('Error retrieving list of documents');
                });

            return deferred.promise;
        };
    }

    ]);