(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$state', 'Auth', 'Principal', 'ProfileService', 'LoginService','Database','Table','Schema','Model', '$rootScope'];

    function NavbarController ($state, Auth, Principal, ProfileService, LoginService,Database,Table,Schema,Model,$rootScope) {
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
        
        
        $rootScope.$on('dataLakeToolApp:databaseUpdateEvent', function(event) {
    		loadAllDatabase();  
    		loadAllSchema();  
        });
        
        var k=0;
        
        loadAllDatabase(k);

                function loadAllDatabase (kValue) {
                	k=kValue;
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
                        data.forEach(filterTables);

                        function filterTables(value, index, array) {
                          loadAllTable(value.id, k);
                        }
                    }
                    function onError(error) {
                        AlertService.error(error.data.message);
                    }
                }
                
        
        
        $rootScope.$on('dataLakeToolApp:tableUpdateEvent', function(event, dbID) {
        	loadAllDatabase(0);  
         });

//        loadAllTable();

        function loadAllTable (databaseID, kValue) {
        	k=kValue;
                Table.query({
                    page: 0,
                    size: 20,
                    sort: sort(),
                    db: databaseID
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
                
//                console.log("Full Data On Success: " + k + ": " + JSON.stringify(data));
                
                if(k==0) {
                	vm.tables = data;
                }
                
                var i=0;
                data.forEach(function(value) {
//                	console.log("Data On Success: " + JSON.stringify(value));
                	data[i].db=databaseID;
                	vm.tables[k] = value;
                	i++;
                	k++;
                });
                
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
            
        }


        
        $rootScope.$on('dataLakeToolApp:schemaUpdateEvent', function(event) {
        	loadAllSchema();  
        });
        
        
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
