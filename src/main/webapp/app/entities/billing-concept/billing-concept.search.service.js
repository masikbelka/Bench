(function() {
    'use strict';

    angular
        .module('benchApp')
        .factory('BillingConceptSearch', BillingConceptSearch);

    BillingConceptSearch.$inject = ['$resource'];

    function BillingConceptSearch($resource) {
        var resourceUrl =  'api/_search/billing-concepts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
