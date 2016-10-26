(function() {
    'use strict';

    angular
        .module('benchApp')
        .factory('ProjectRoleSearch', ProjectRoleSearch);

    ProjectRoleSearch.$inject = ['$resource'];

    function ProjectRoleSearch($resource) {
        var resourceUrl =  'api/_search/project-roles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
