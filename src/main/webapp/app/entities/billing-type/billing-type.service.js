(function() {
    'use strict';
    angular
        .module('benchApp')
        .factory('BillingType', BillingType);

    BillingType.$inject = ['$resource'];

    function BillingType ($resource) {
        var resourceUrl =  'api/billing-types/:id';

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
