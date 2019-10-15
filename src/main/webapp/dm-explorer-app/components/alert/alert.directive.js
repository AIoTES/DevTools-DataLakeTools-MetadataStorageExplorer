(function() {
    'use strict';

    var simAlert = {
        template: '<div class="alerts" ng-cloak="" role="alert">' +
                        '<div ng-repeat="alert in $ctrl.alerts" ng-class="[alert.position, {\'toast\': alert.toast}]">' +
                            '<uib-alert ng-cloak="" type="{{alert.type}}" close="alert.close($ctrl.alerts)"><pre ng-bind-html="alert.msg"></pre></uib-alert>' +
                        '</div>' +
                  '</div>',
        controller: simAlertController
    };

    angular
        .module('dataLakeToolApp')
        .component('simAlert', simAlert);

    simAlertController.$inject = ['$scope', 'AlertService'];

    function simAlertController($scope, AlertService) {
        var vm = this;

        vm.alerts = AlertService.get();
        $scope.$on('$destroy', function () {
            vm.alerts = [];
        });
    }
})();
