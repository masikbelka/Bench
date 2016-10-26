(function() {
    'use strict';
    angular
        .module('benchApp')
        .factory('LanguageLevel', LanguageLevel);

    LanguageLevel.$inject = ['$resource'];

    function LanguageLevel ($resource) {
        var resourceUrl =  'api/language-levels/:id';

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
