(function() {
    'use strict';
    angular
        .module('benchApp')
        .factory('BillingConcept', BillingConcept);

    BillingConcept.$inject = ['$resource'];

    function BillingConcept ($resource) {
        var resourceUrl =  'api/billing-concepts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
