(function() {
    'use strict';

    angular
        .module('dataLakeToolApp')
        .controller('TableController', TableController);

    TableController.$inject = ['$state', 'Table', 'TableSearch', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function TableController($state, Table, TableSearch, ParseLinks, AlertService, paginationConstants, pagingParams) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;
        vm.searchQuery = pagingParams.search;
        vm.currentSearch = pagingParams.search;

        loadAll();

        function loadAll () {
            if (pagingParams.search) {
                TableSearch.query({
                    query: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                Table.query({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort(),
                    db: $state.params.db
                }, onSuccess, onError);
            }
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                //vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                
                var tempArray = [];
                var i=0;
                data.forEach(function(value, index, array) {
                    if(value.db == $state.params.db) {
                  	  tempArray.push(value);
                  	  tempArray[i].db=$state.params.db;
                  	  i++;
                    }
                  });
                
                vm.tables = tempArray;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        function search(searchQuery) {
            if (!searchQuery){
                return vm.clear();
            }
            vm.links = null;
            vm.page = 1;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.transition();
        }

        function clear() {
            vm.links = null;
            vm.page = 1;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.currentSearch = null;
            vm.transition();
        }
    }
})();
