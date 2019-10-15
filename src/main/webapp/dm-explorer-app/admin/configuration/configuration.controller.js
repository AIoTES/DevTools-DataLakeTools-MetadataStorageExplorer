(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .controller('SimConfigurationController', SimConfigurationController);

    SimConfigurationController.$inject = ['$filter','SimConfigurationService'];

    function SimConfigurationController (filter,SimConfigurationService) {
        var vm = this;

        vm.allConfiguration = null;
        vm.configuration = null;

        SimConfigurationService.get().then(function(configuration) {
            vm.configuration = configuration;
        });
        SimConfigurationService.getEnv().then(function (configuration) {
            vm.allConfiguration = configuration;
        });
    }
})();
