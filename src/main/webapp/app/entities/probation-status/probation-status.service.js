(function() {
    'use strict';
    angular
        .module('benchApp')
        .factory('ProbationStatus', ProbationStatus);

    ProbationStatus.$inject = ['$resource', 'DateUtils'];

    function ProbationStatus ($resource, DateUtils) {
        var resourceUrl =  'api/probation-statuses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.endDate = DateUtils.convertDateTimeFromServer(data.endDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
