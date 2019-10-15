(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .controller('ModelDetailController', ModelDetailController);

    ModelDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Model'];

    function ModelDetailController($scope, $rootScope, $stateParams, previousState, entity, Model) {
        var vm = this;

        vm.model = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('dataLakeToolApp:modelUpdate', function(event, result) {
            vm.model = result;
        });
        $scope.$on('$destroy', unsubscribe);

         document.getElementById("model_textview").innerHTML = JSON.stringify(vm.model.params, undefined, 2);

    }
})();
