angular.module('services', []) 
	.service('DocumentService', ['$http', '$q', function($http, $q) {
			this.getAllDocuments = function() {
				var deferred = $q.defer();
				
				$http.get('/document/')
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
		
			this.getDocumentsByMonths = function() {
				var deferred  =$q.defer();
				
				$http.get('/month')
					.then(function(response) {
						if (response.status == 200) {
							deferred.resolve(response.data);
						}
						else {
							deferred.reject('Error retrieving list of documents by monthes');
						}
					});
				return deferred.promise;
			};	
	}])