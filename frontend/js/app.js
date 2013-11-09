var scheduleApp = angular.module('scheduleApp', ['scheduleControllers', 'scheduleDirectives', 'facebook']);

scheduleApp.config(['$routeProvider', 'FacebookProvider',
    function($routeProvider, FacebookProvider) {
        $routeProvider.
            when('/calendar', {
                templateUrl: 'partials/calendar.html',
                controller: 'CalendarGenerator'
            }).
            when('/client-landing', {
                templateUrl: 'partials/client-landing.html'
            }).
            when('/staff-landing', {
                templateUrl: 'partials/staff-landing.html'
            }).
            when('/edit-profile', {
                templateUrl: 'partials/edit-profile.html'
            }).
            when('/login', {
                templateUrl: 'partials/login.html'
            }).
            when('/loading', {
                templateUrl: 'partials/loading.html'
            }).
             when('/admin', {
                templateUrl: 'partials/admin.html'
            }).
             when('/edit-user', {
                templateUrl: 'partials/edit-user.html'
            }).
            otherwise({
                redirectTo: '/loading'
            });
        FacebookProvider.init('197300770451342');
    }]);