(function() {
    'use strict';

    angular
        .module('benchApp')
        .factory('JobFunctionSearch', JobFunctionSearch);

    JobFunctionSearch.$inject = ['$resource'];

    function JobFunctionSearch($resource) {
        var resourceUrl =  'api/_search/job-functions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
