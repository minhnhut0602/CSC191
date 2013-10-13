var scheduleControllers = angular.module('scheduleControllers', []);

scheduleControllers.controller('CalendarGenerator', function CalendarGenerator($scope) {
    $scope.calendar = function(year, monthIndex, day) {
        var monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
        var dayNames = ["sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"];
        if (year === undefined &&
            monthIndex === undefined &&
            day === undefined) {
            var setCurrentDate = new Date();
            year = setCurrentDate.getYear();
            monthIndex = setCurrentDate.getMonth();
            day = 0;
        }
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

scheduleControllers.controller('AuthController', ['$scope', 'Facebook', function($scope, Facebook) {
   
    $scope.user = {};
    // Defining user logged status
    $scope.loggedIn = false;
    
    // Here, usually you should watch for when Facebook is ready and loaded
    $scope.$watch(function() {
        return Facebook.isReady(); // This is for convenience, to notify if Facebook is loaded and ready to go.
    }, function(newVal) {
        $scope.facebookReady = true; // You might want to use this to disable/show/hide buttons and else
    });

    // From now and on you can use the Facebook service just as Facebook api says
    // Take into account that you will need $scope.$apply when being inside Facebook functions scope and not angular
    $scope.login = function() {
        Facebook.login(function(response) {
            // Do something with response. Don't forget here you are on Facebook scope so use $scope.$apply
            $scope.getLoginStatus();
        });
    };
    $scope.logout = function() {
        Facebook.logout(function() {
            $scope.$apply(function() {
                $scope.user   = {};
                $scope.loggedIn = false;  
            });
        });
    }

    $scope.getLoginStatus = function() {
        Facebook.getLoginStatus(function(response) {
            if(response.status == 'connected') {
                $scope.loggedIn = true;
                $scope.me();
            } else {
                $scope.login();
            }
        });
    };

    $scope.me = function() {
        Facebook.api('/me', function(response) {
            $scope.$apply(function() {
                // Here you could re-check for user status (just in case)
                $scope.user = response;
                console.log(response);
            });
        });
    };
}]);