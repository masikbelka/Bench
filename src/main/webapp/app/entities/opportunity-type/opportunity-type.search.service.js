(function() {
    'use strict';

    angular
        .module('benchApp')
        .factory('OpportunityTypeSearch', OpportunityTypeSearch);

    OpportunityTypeSearch.$inject = ['$resource'];

    function OpportunityTypeSearch($resource) {
        var resourceUrl =  'api/_search/opportunity-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
