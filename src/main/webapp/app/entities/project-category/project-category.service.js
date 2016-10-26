(function() {
    'use strict';
    angular
        .module('benchApp')
        .factory('ProjectCategory', ProjectCategory);

    ProjectCategory.$inject = ['$resource'];

    function ProjectCategory ($resource) {
        var resourceUrl =  'api/project-categories/:id';

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
