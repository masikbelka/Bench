(function() {
    'use strict';

    angular
        .module('benchApp')
        .factory('OpportunitySearch', OpportunitySearch);

    OpportunitySearch.$inject = ['$resource'];

    function OpportunitySearch($resource) {
        var resourceUrl =  'api/_search/opportunities/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
