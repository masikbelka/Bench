(function() {
    'use strict';

    angular
        .module('benchApp')
        .factory('BillingTypeSearch', BillingTypeSearch);

    BillingTypeSearch.$inject = ['$resource'];

    function BillingTypeSearch($resource) {
        var resourceUrl =  'api/_search/billing-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
