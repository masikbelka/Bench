(function() {
    'use strict';

    angular
        .module('benchApp')
        .factory('BenchHistorySearch', BenchHistorySearch);

    BenchHistorySearch.$inject = ['$resource'];

    function BenchHistorySearch($resource) {
        var resourceUrl =  'api/_search/bench-histories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
