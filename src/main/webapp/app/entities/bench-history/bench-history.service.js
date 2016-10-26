(function() {
    'use strict';
    angular
        .module('benchApp')
        .factory('BenchHistory', BenchHistory);

    BenchHistory.$inject = ['$resource', 'DateUtils'];

    function BenchHistory ($resource, DateUtils) {
        var resourceUrl =  'api/bench-histories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createdTime = DateUtils.convertDateTimeFromServer(data.createdTime);
                        data.validTo = DateUtils.convertDateTimeFromServer(data.validTo);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
