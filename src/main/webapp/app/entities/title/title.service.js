(function() {
    'use strict';
    angular
        .module('benchApp')
        .factory('Title', Title);

    Title.$inject = ['$resource'];

    function Title ($resource) {
        var resourceUrl =  'api/titles/:id';

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
