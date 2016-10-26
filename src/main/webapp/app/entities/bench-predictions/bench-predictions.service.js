(function() {
    'use strict';
    angular
        .module('benchApp')
        .factory('BenchPredictions', BenchPredictions);

    BenchPredictions.$inject = ['$resource', 'DateUtils'];

    function BenchPredictions ($resource, DateUtils) {
        var resourceUrl =  'api/bench-predictions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createdTime = DateUtils.convertDateTimeFromServer(data.createdTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
