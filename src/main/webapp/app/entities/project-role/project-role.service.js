(function() {
    'use strict';
    angular
        .module('benchApp')
        .factory('ProjectRole', ProjectRole);

    ProjectRole.$inject = ['$resource'];

    function ProjectRole ($resource) {
        var resourceUrl =  'api/project-roles/:id';

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
