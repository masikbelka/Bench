(function() {
    'use strict';
    angular
        .module('benchApp')
        .factory('BenchCommentHistory', BenchCommentHistory);

    BenchCommentHistory.$inject = ['$resource', 'DateUtils'];

    function BenchCommentHistory ($resource, DateUtils) {
        var resourceUrl =  'api/bench-comment-histories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.changeTime = DateUtils.convertDateTimeFromServer(data.changeTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
