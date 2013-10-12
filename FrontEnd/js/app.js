var scheduleApp = angular.module('scheduleApp', ['scheduleControllers', 'scheduleDirectives']);

scheduleApp.config(['$routeProvider',
    function($routeProvider) {
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
            otherwise({
                redirectTo: '/login'
            });
    }]);