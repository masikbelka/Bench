(function() {
    'use strict';

    angular
        .module('benchApp')
        .factory('ProbationStatusSearch', ProbationStatusSearch);

    ProbationStatusSearch.$inject = ['$resource'];

    function ProbationStatusSearch($resource) {
        var resourceUrl =  'api/_search/probation-statuses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
