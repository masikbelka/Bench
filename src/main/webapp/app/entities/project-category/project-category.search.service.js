(function() {
    'use strict';

    angular
        .module('benchApp')
        .factory('ProjectCategorySearch', ProjectCategorySearch);

    ProjectCategorySearch.$inject = ['$resource'];

    function ProjectCategorySearch($resource) {
        var resourceUrl =  'api/_search/project-categories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
