(function() {
    'use strict';
    angular
        .module('benchApp')
        .factory('JobFunction', JobFunction);

    JobFunction.$inject = ['$resource'];

    function JobFunction ($resource) {
        var resourceUrl =  'api/job-functions/:id';

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
