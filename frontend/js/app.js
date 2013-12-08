var scheduleApp = angular.module('scheduleApp', ['scheduleControllers', 'calendarModule', 'availabilityModule' ,'scheduleDirectives', 'facebook', 'ui.bootstrap']);

scheduleApp.config(['$routeProvider', '$httpProvider','FacebookProvider',
    function($routeProvider, $httpProvider,FacebookProvider) {
        $httpProvider.defaults.useXDomain = true;
        delete $httpProvider.defaults.headers.common['X-Requested-With'];
        $routeProvider.
            when('/calendar', {
                templateUrl: 'partials/calendar.html',
            }).
            when('/client-landing', {
                templateUrl: 'partials/client-landing.html'
            }).
            when('/staff-landing', {
                templateUrl: 'partials/staff-landing.html'
            }).
            when('/edit-profile', {
                templateUrl: 'partials/edit-profile.html',
                controller: 'editprofile'
            }).
            when('/login', {
                templateUrl: 'partials/login.html',
                controller: 'loginController'
            }).
            when('/loading', {
                templateUrl: 'partials/loading.html',
                controller: 'loadingController'
            }).
            when('/view-stylists', {
                templateUrl: 'partials/view-stylists.html'
            }).
            when('/view-clients', {
                templateUrl: 'partials/view-clients.html'
            }).
            when('/edit-user', {
                templateUrl: 'partials/edit-user.html',
                controller: 'createUser'
            }).
            when('/admin', {
                templateUrl: 'partials/admin.html',
                controller: 'adminController'
            }).
            when('/edit-availability', {
                templateUrl: 'partials/edit-availability.html',
                controller: 'TimepickerDemoCtrl'
            }).
            when('/user-profile', {
                templateUrl: 'partials/user-profile.html',
                controller: 'userProfileController'
            }).
            when('/create-services', {
                templateUrl: 'partials/create-services.html',
                controller: 'createService'
            }).
            otherwise({
                redirectTo: '/loading',
            });
        FacebookProvider.init('197300770451342');
    }]);