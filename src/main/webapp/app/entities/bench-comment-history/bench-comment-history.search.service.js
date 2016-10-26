(function() {
    'use strict';

    angular
        .module('benchApp')
        .factory('BenchCommentHistorySearch', BenchCommentHistorySearch);

    BenchCommentHistorySearch.$inject = ['$resource'];

    function BenchCommentHistorySearch($resource) {
        var resourceUrl =  'api/_search/bench-comment-histories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
