(function() {
    'use strict';

    angular
        .module('benchApp')
        .factory('ProjectHistorySearch', ProjectHistorySearch);

    ProjectHistorySearch.$inject = ['$resource'];

    function ProjectHistorySearch($resource) {
        var resourceUrl =  'api/_search/project-histories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
