(function() {
    'use strict';
    angular
        .module('benchApp')
        .factory('ProductionStatus', ProductionStatus);

    ProductionStatus.$inject = ['$resource'];

    function ProductionStatus ($resource) {
        var resourceUrl =  'api/production-statuses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
