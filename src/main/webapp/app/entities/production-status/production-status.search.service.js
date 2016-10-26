(function() {
    'use strict';

    angular
        .module('benchApp')
        .factory('ProductionStatusSearch', ProductionStatusSearch);

    ProductionStatusSearch.$inject = ['$resource'];

    function ProductionStatusSearch($resource) {
        var resourceUrl =  'api/_search/production-statuses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
