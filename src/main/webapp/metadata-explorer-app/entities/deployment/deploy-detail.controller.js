(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .controller('DeployDetailController', DeployDetailController);

    DeployDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Deploy'];

    function DeployDetailController($scope, $rootScope, $stateParams, previousState, entity, Deploy) {
        var vm = this;

        vm.deploy = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('dataLakeToolApp:deployUpdate', function(event, result) {
            vm.deploy = result;
        });
        $scope.$on('$destroy', unsubscribe);

         document.getElementById("model_textview").innerHTML = JSON.stringify(vm.deploy.context, undefined, 2);

    }
})();
