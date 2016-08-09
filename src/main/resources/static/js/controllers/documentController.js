angular.module('app').controller('documentController', 
	['$scope', 'DocumentService',	function($scope, DocumentService) {
  		var self = this;
  		
  		DocumentService.getDocumentsByMonths().
  			then(function(data) {
  				self.months = data;
  			},
  			function(reason) {
  				self.error = reason;
  			});
  		
  		
  		self.finishDocument = function(document, month) {
  			DocumentService.finishDocument(document.id);
  			
  			// hide the document
  			var index = month.documents.indexOf(document);
  			month.documents.splice(index, 1);
  			
  			// hide month of no mo elements
  			if (month.documents.length == 0) {
  				var index1 = self.months.indexOf(month);
  				self.months.splice(index1, 1);
  			}
  		};
    }])