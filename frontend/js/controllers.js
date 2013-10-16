var scheduleControllers = angular.module('scheduleControllers', []);


//   /$$$$$$   /$$$$$$  /$$       /$$$$$$$$ /$$   /$$ /$$$$$$$   /$$$$$$  /$$$$$$$ 
//  /$$__  $$ /$$__  $$| $$      | $$_____/| $$$ | $$| $$__  $$ /$$__  $$| $$__  $$
// | $$  \__/| $$  \ $$| $$      | $$      | $$$$| $$| $$  \ $$| $$  \ $$| $$  \ $$
// | $$      | $$$$$$$$| $$      | $$$$$   | $$ $$ $$| $$  | $$| $$$$$$$$| $$$$$$$/
// | $$      | $$__  $$| $$      | $$__/   | $$  $$$$| $$  | $$| $$__  $$| $$__  $$
// | $$    $$| $$  | $$| $$      | $$      | $$\  $$$| $$  | $$| $$  | $$| $$  \ $$
// |  $$$$$$/| $$  | $$| $$$$$$$$| $$$$$$$$| $$ \  $$| $$$$$$$/| $$  | $$| $$  | $$
//  \______/ |__/  |__/|________/|________/|__/  \__/|_______/ |__/  |__/|__/  |__/
scheduleControllers.controller('CalendarGenerator', function CalendarGenerator($scope) {
    $scope.calendar = function(year, monthIndex, day) {
        if (year === undefined &&
            month === undefined &&
            day === undefined) {
            var d = new Date();
            year = d.getFullYear();
            monthIndex = d.getMonth();
            day = d.getDate();
        }
        var monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
        var dayNames = ["sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"];

        var date = new Date(year, monthIndex, 1);
        var current = new Date(year, monthIndex, day);
        var month = {};
        var weeks = [];

        month.name = monthNames[monthIndex];
        month.year = date.getFullYear();
        month.number = date.getMonth();

        // loop for weeks
        for (var j=0 ; date.getMonth() === monthIndex ; j++) {
            //loop for days
            var weekdays = [];
            for (var k=0 ; date.getMonth() === monthIndex ; k++){
                // console.log(scope.date);

                if (date.getDay() === 0 && k !== 0) {
                    break;
                }
                // loop until you see the day go to 0 then jump out OR the month changes
                var dayNumber = date.getDate();
                var dayName   = dayNames[date.getDay()];
                var dayClass  = "";
                if (date.getTime() < current.getTime()) {
                    dayClass = "inactive";
                }
                if (date.getDate()  === current.getDate() &&
                    date.getMonth() === current.getMonth() &&
                    date.getYear()  === current.getYear()) {
                    dayClass = "today";
                }
                weekdays.push({number: dayNumber, name: dayName, class:dayClass});

                // increment the date
                date.setDate(date.getDate()+1);
            }
            weeks.push(weekdays);
        }
        // month.weeks = weeks;
        // console.log(month);
        month.weeks = weeks;
        $scope.month = month;
    };
    var d = new Date();
    $scope.calendar(d.getFullYear(), d.getMonth(), d.getDate());
});


//  /$$$$$$$$ /$$$$$$   /$$$$$$  /$$$$$$$$ /$$$$$$$   /$$$$$$   /$$$$$$  /$$   /$$
// | $$_____//$$__  $$ /$$__  $$| $$_____/| $$__  $$ /$$__  $$ /$$__  $$| $$  /$$/
// | $$     | $$  \ $$| $$  \__/| $$      | $$  \ $$| $$  \ $$| $$  \ $$| $$ /$$/ 
// | $$$$$  | $$$$$$$$| $$      | $$$$$   | $$$$$$$ | $$  | $$| $$  | $$| $$$$$/  
// | $$__/  | $$__  $$| $$      | $$__/   | $$__  $$| $$  | $$| $$  | $$| $$  $$  
// | $$     | $$  | $$| $$    $$| $$      | $$  \ $$| $$  | $$| $$  | $$| $$\  $$ 
// | $$     | $$  | $$|  $$$$$$/| $$$$$$$$| $$$$$$$/|  $$$$$$/|  $$$$$$/| $$ \  $$
// |__/     |__/  |__/ \______/ |________/|_______/  \______/  \______/ |__/  \__/
scheduleControllers.controller('AuthController', ['$scope', '$rootScope', '$location', 'Facebook', function($scope, $rootScope, $location, Facebook) {
   
    $scope.user = {};
    // Defining user logged status
    $scope.loggedIn = false;
    
    // Here, usually you should watch for when Facebook is ready and loaded
    $scope.$watch(function() {
        return Facebook.isReady(); // This is for convenience, to notify if Facebook is loaded and ready to go.
    }, function(newVal) {
        $scope.facebookReady = true; // You might want to use this to disable/show/hide buttons and else
        if ($scope.facebookReady) {
            $scope.getLoginStatus();
        }
    });

    // From now and on you can use the Facebook service just as Facebook api says
    // Take into account that you will need $scope.$apply when being inside Facebook functions scope and not angular
    $scope.login = function() {
        Facebook.login(function(response) {
            // Do something with response. Don't forget here you are on Facebook scope so use $scope.$apply
            $scope.getLoginStatus();
            $location.path('client-landing');
        });
    };
    $scope.logout = function() {
        Facebook.logout(function() {
            $scope.$apply(function() {
                $scope.user   = {};
                $scope.loggedIn = false;
                $location.path('login');

            });
        });
    };

    $scope.getLoginStatus = function() {
        console.log('before');
        Facebook.getLoginStatus(function(response) {
            console.log(response);
            if(response.status == 'connected') {
                $scope.loggedIn = true;
                $scope.me();
                console.log(response);
            } else {
                // $scope.login();
                //bring them to the login page
                console.log('send to login');
                $scope.$apply(function() {
                    $location.path('login');
                });
            }
        });
    };

    $scope.me = function() {
        Facebook.api('/me', function(response) {
            $scope.$apply(function() {
                // Here you could re-check for user status (just in case)
                $scope.user = response;
                $rootScope.user = $scope.user;
                console.log(response);
            });
        });
    };
}]);


//   /$$$$$$  /$$       /$$$$$$ /$$$$$$$$ /$$   /$$ /$$$$$$$$          
//  /$$__  $$| $$      |_  $$_/| $$_____/| $$$ | $$|__  $$__/          
// | $$  \__/| $$        | $$  | $$      | $$$$| $$   | $$             
// | $$      | $$        | $$  | $$$$$   | $$ $$ $$   | $$             
// | $$      | $$        | $$  | $$__/   | $$  $$$$   | $$             
// | $$    $$| $$        | $$  | $$      | $$\  $$$   | $$             
// |  $$$$$$/| $$$$$$$$ /$$$$$$| $$$$$$$$| $$ \  $$   | $$             
//  \______/ |________/|______/|________/|__/  \__/   |__/             
//  /$$        /$$$$$$  /$$   /$$ /$$$$$$$  /$$$$$$ /$$   /$$  /$$$$$$ 
// | $$       /$$__  $$| $$$ | $$| $$__  $$|_  $$_/| $$$ | $$ /$$__  $$
// | $$      | $$  \ $$| $$$$| $$| $$  \ $$  | $$  | $$$$| $$| $$  \__/
// | $$      | $$$$$$$$| $$ $$ $$| $$  | $$  | $$  | $$ $$ $$| $$ /$$$$
// | $$      | $$__  $$| $$  $$$$| $$  | $$  | $$  | $$  $$$$| $$|_  $$
// | $$      | $$  | $$| $$\  $$$| $$  | $$  | $$  | $$\  $$$| $$  \ $$
// | $$$$$$$$| $$  | $$| $$ \  $$| $$$$$$$/ /$$$$$$| $$ \  $$|  $$$$$$/
// |________/|__/  |__/|__/  \__/|_______/ |______/|__/  \__/ \______/ 
scheduleControllers.controller('ClientLandingController', ['$scope', '$rootScope', function($scope, $rootScope) {
    
}]);
