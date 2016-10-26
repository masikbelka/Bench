(function() {
    'use strict';

    angular
        .module('benchApp')
        .factory('OpportunityPositionSearch', OpportunityPositionSearch);

    OpportunityPositionSearch.$inject = ['$resource'];

    function OpportunityPositionSearch($resource) {
        var resourceUrl =  'api/_search/opportunity-positions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
