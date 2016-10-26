(function() {
    'use strict';
    angular
        .module('benchApp')
        .factory('PrimarySkill', PrimarySkill);

    PrimarySkill.$inject = ['$resource'];

    function PrimarySkill ($resource) {
        var resourceUrl =  'api/primary-skills/:id';

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
