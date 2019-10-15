(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$state', 'Auth', 'Principal', 'ProfileService', 'LoginService','Database','Table','Schema','Model', '$rootScope'];

    function NavbarController ($state, Auth, Principal, ProfileService, LoginService,Database,Table,Schema,Model, $rootScope) {
        var vm = this;

        console.log('NavbarController');
        vm.isNavbarCollapsed = true;
        vm.databases = [];
        vm.isAuthenticated = Principal.isAuthenticated;

        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        vm.login = login;
        vm.logout = logout;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.$state = $state;

        function login() {
            collapseNavbar();
            LoginService.open();
        }

        function logout() {
            collapseNavbar();
            Auth.logout();
            $state.go('home');
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }

        loadAllDatabase();

                function loadAllDatabase () {
                        Database.query({
                            page: 0,
                            size: 20,
                            sort: sort()
                        }, onSuccess, onError);

                    function sort() {
                        var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                        if (vm.predicate !== 'id') {
                            result.push('id');
                        }
                        return result;
                    }
                    function onSuccess(data, headers) {
                       // vm.links = ParseLinks.parse(headers('link'));
                        vm.totalItems = headers('X-Total-Count');
                        vm.queryCount = vm.totalItems;
                        vm.databases = data;
                        vm.page = 0;//pagingParams.page;
                        console.log(vm.databases);
                        console.log('tenete');
                        console.log(data);
                    }
                    function onError(error) {
                        AlertService.error(error.data.message);
                    }
                }


loadAllTable();

        function loadAllTable () {

                Table.query({
                    page: 0,
                    size: 20,
                    sort: sort()
                }, onSuccess, onError);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.tables = data;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }


  loadAllSchema();

         function loadAllSchema () {

                 Schema.query({
                     page: 0,
                     size: 20,
                     sort: sort()
                 }, onSuccess, onError);
             function sort() {
                 var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                 if (vm.predicate !== 'id') {
                     result.push('id');
                 }
                 return result;
             }
             function onSuccess(data, headers) {
                // vm.links = ParseLinks.parse(headers('link'));
                 vm.totalItems = headers('X-Total-Count');
                 vm.queryCount = vm.totalItems;
                 vm.schemata = data;
             }
             function onError(error) {
                 AlertService.error(error.data.message);
             }
         }

         
         
         $rootScope.$on('dataLakeToolApp:modelUpdateEvent', function(event) {
        	 loadAllModels();  
         });
         
         
         loadAllModels();

         function loadAllModels () {

                 Model.query({
                     page: 0,
                     size: 20,
                     sort: sort()
                 }, onSuccess, onError);

             function sort() {
                 var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                 if (vm.predicate !== 'id') {
                     result.push('id');
                 }
                 return result;
             }
             function onSuccess(data, headers) {

                 vm.totalItems = headers('X-Total-Count');
                 vm.queryCount = vm.totalItems;
                 vm.models = data;
             }
             function onError(error) {
                 AlertService.error(error.data.message);
             }
         }

/////


    }
})();
