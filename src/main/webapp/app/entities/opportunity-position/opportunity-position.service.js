(function() {
    'use strict';
    angular
        .module('benchApp')
        .factory('OpportunityPosition', OpportunityPosition);

    OpportunityPosition.$inject = ['$resource', 'DateUtils'];

    function OpportunityPosition ($resource, DateUtils) {
        var resourceUrl =  'api/opportunity-positions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createdTime = DateUtils.convertLocalDateFromServer(data.createdTime);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.createdTime = DateUtils.convertLocalDateToServer(copy.createdTime);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.createdTime = DateUtils.convertLocalDateToServer(copy.createdTime);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
