function readCookie(name) {
    name += '=';
    for (var ca = document.cookie.split(/;\s*/), i = ca.length - 1; i >= 0; i--)
        if (!ca[i].indexOf(name))
            return ca[i].replace(name, '');
}
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
    var monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
    var dayNames = ["sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"];
    $scope.calendar = function(year, monthIndex, day) {
        if (year === undefined &&
            month === undefined &&
            day === undefined) {
            var d = new Date();
            year = d.getFullYear();
            monthIndex = d.getMonth();
            day = d.getDate();
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
    $scope.dayNow = d.getDate();
    $scope.monthNow = {"num": d.getMonth(), "name":monthNames[d.getMonth()]};
    $scope.yearNow = d.getFullYear();
    $scope.monthNames = monthNames;

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
                document.cookie="myAccessToken="+response.authResponse["accessToken"];
                document.cookie="myID="+response.authResponse["userID"];
                alert("asd");
                $scope.user = response;
                $rootScope.user = $scope.user;
                console.log(response);
                $rootScope.facebook = response;
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
                // this is all null scott
                // TODO figure out why
            });
        });
    };
}]);

//   /$$$$$$  /$$$$$$$$  /$$$$$$  /$$$$$$$$ /$$$$$$$$
//  /$$__  $$|__  $$__/ /$$__  $$| $$_____/| $$_____/
// | $$  \__/   | $$   | $$  \ $$| $$      | $$
// \  $$$$$$    | $$   | $$$$$$$$| $$$$$   | $$$$$
//  \____  $$   | $$   | $$__  $$| $$__/   | $$__/
//  /$$  \ $$   | $$   | $$  | $$| $$      | $$
// |  $$$$$$    | $$   | $$  | $$| $$      | $$
//  \______/    |__/   |__/  |__/|__/      |__/
//  /$$        /$$$$$$  /$$   /$$ /$$$$$$$  /$$$$$$ /$$   /$$  /$$$$$$
// | $$       /$$__  $$| $$$ | $$| $$__  $$|_  $$_/| $$$ | $$ /$$__  $$
// | $$      | $$  \ $$| $$$$| $$| $$  \ $$  | $$  | $$$$| $$| $$  \__/
// | $$      | $$$$$$$$| $$ $$ $$| $$  | $$  | $$  | $$ $$ $$| $$ /$$$$
// | $$      | $$__  $$| $$  $$$$| $$  | $$  | $$  | $$  $$$$| $$|_  $$
// | $$      | $$  | $$| $$\  $$$| $$  | $$  | $$  | $$\  $$$| $$  \ $$
// | $$$$$$$$| $$  | $$| $$ \  $$| $$$$$$$/ /$$$$$$| $$ \  $$|  $$$$$$/
// |________/|__/  |__/|__/  \__/|_______/ |______/|__/  \__/ \______/
scheduleControllers.controller('StaffLandingController', function StaffLandingController($scope, $http) {
  $http.get('json/appointments/staffAppointments.json').success(function(data) {
    $scope.appointments = data;
  });
});


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
scheduleControllers.controller('ClientLandingController', function ClientLandingController($scope, $http, $rootScope) {
    var config = {headers:  {
        'authType': 'admin',
        'authToken': readCookie("myAccessToken"),
        'debug': 'true',
        'id': readCookie("myID"),
        'Content-Type': 'application/json',
        'Cache-Control': 'no-cache'
        }
    };

  $http.get('json/appointments/clientAppointments.json', config).success(function(data) {
    $scope.appointments = data;
  });
});


//   /$$$$$$        /$$               /$$
//  /$$__  $$      | $$              |__/
// | $$  \ $$  /$$$$$$$ /$$$$$$/$$$$  /$$ /$$$$$$$
// | $$$$$$$$ /$$__  $$| $$_  $$_  $$| $$| $$__  $$
// | $$__  $$| $$  | $$| $$ \ $$ \ $$| $$| $$  \ $$
// | $$  | $$| $$  | $$| $$ | $$ | $$| $$| $$  | $$
// | $$  | $$|  $$$$$$$| $$ | $$ | $$| $$| $$  | $$
// |__/  |__/ \_______/|__/ |__/ |__/|__/|__/  |__/



//  /$$$$$$$
// | $$__  $$
// | $$  \ $$ /$$$$$$   /$$$$$$   /$$$$$$
// | $$$$$$$/|____  $$ /$$__  $$ /$$__  $$
// | $$____/  /$$$$$$$| $$  \ $$| $$$$$$$$
// | $$      /$$__  $$| $$  | $$| $$_____/
// | $$     |  $$$$$$$|  $$$$$$$|  $$$$$$$
// |__/      \_______/ \____  $$ \_______/
//                     /$$  \ $$
//                    |  $$$$$$/
//                     \______/

scheduleControllers.controller('adminController', function adminController($scope, $http, $rootScope) {
    var config = {headers:  {
        'authType': 'admin',
        'authToken': readCookie("myAccessToken"),
        'debug': 'true',
        'id': readCookie("myID"),
        'Content-Type': 'application/json',
        'Cache-Control': 'no-cache'
        }
    };
  $http.get('http://home.joubin.me/api/salon-scheduler-api/users', config).success(function(data) {
    $scope.users = data;
  });
});

//  /$$   /$$  /$$$$$$  /$$$$$$$$ /$$$$$$$
// | $$  | $$ /$$__  $$| $$_____/| $$__  $$
// | $$  | $$| $$  \__/| $$      | $$  \ $$
// | $$  | $$|  $$$$$$ | $$$$$   | $$$$$$$/
// | $$  | $$ \____  $$| $$__/   | $$__  $$
// | $$  | $$ /$$  \ $$| $$      | $$  \ $$
// |  $$$$$$/|  $$$$$$/| $$$$$$$$| $$  | $$
//  \______/  \______/ |________/|__/  |__/
//   /$$$$$$  /$$   /$$ /$$$$$$$$ /$$   /$$
//  /$$__  $$| $$  | $$|__  $$__/| $$  | $$
// | $$  \ $$| $$  | $$   | $$   | $$  | $$
// | $$$$$$$$| $$  | $$   | $$   | $$$$$$$$
// | $$__  $$| $$  | $$   | $$   | $$__  $$
// | $$  | $$| $$  | $$   | $$   | $$  | $$
// | $$  | $$|  $$$$$$/   | $$   | $$  | $$
// |__/  |__/ \______/    |__/   |__/  |__/
//   /$$$$$$   /$$$$$$  /$$   /$$ /$$$$$$$$ /$$$$$$$   /$$$$$$  /$$       /$$       /$$$$$$$$ /$$$$$$$
//  /$$__  $$ /$$__  $$| $$$ | $$|__  $$__/| $$__  $$ /$$__  $$| $$      | $$      | $$_____/| $$__  $$
// | $$  \__/| $$  \ $$| $$$$| $$   | $$   | $$  \ $$| $$  \ $$| $$      | $$      | $$      | $$  \ $$
// | $$      | $$  | $$| $$ $$ $$   | $$   | $$$$$$$/| $$  | $$| $$      | $$      | $$$$$   | $$$$$$$/
// | $$      | $$  | $$| $$  $$$$   | $$   | $$__  $$| $$  | $$| $$      | $$      | $$__/   | $$__  $$
// | $$    $$| $$  | $$| $$\  $$$   | $$   | $$  \ $$| $$  | $$| $$      | $$      | $$      | $$  \ $$
// |  $$$$$$/|  $$$$$$/| $$ \  $$   | $$   | $$  | $$|  $$$$$$/| $$$$$$$$| $$$$$$$$| $$$$$$$$| $$  | $$
//  \______/  \______/ |__/  \__/   |__/   |__/  |__/ \______/ |________/|________/|________/|__/  |__/


scheduleControllers.controller('userAuthController', function userAuthController($scope, $http) {
  $http.get('json/userAuthController.json').success(function(data) {
    $scope.currentUserAuth = data;
  });
});



//  /$$$$$$ /$$   /$$ /$$$$$$$  /$$$$$$$$ /$$   /$$
// |_  $$_/| $$$ | $$| $$__  $$| $$_____/| $$  / $$
//   | $$  | $$$$| $$| $$  \ $$| $$      |  $$/ $$/
//   | $$  | $$ $$ $$| $$  | $$| $$$$$    \  $$$$/
//   | $$  | $$  $$$$| $$  | $$| $$__/     >$$  $$
//   | $$  | $$\  $$$| $$  | $$| $$       /$$/\  $$
//  /$$$$$$| $$ \  $$| $$$$$$$/| $$$$$$$$| $$  \ $$
// |______/|__/  \__/|_______/ |________/|__/  |__/
//   /$$$$$$   /$$$$$$  /$$   /$$ /$$$$$$$$ /$$$$$$$   /$$$$$$  /$$       /$$       /$$$$$$$$ /$$$$$$$
//  /$$__  $$ /$$__  $$| $$$ | $$|__  $$__/| $$__  $$ /$$__  $$| $$      | $$      | $$_____/| $$__  $$
// | $$  \__/| $$  \ $$| $$$$| $$   | $$   | $$  \ $$| $$  \ $$| $$      | $$      | $$      | $$  \ $$
// | $$      | $$  | $$| $$ $$ $$   | $$   | $$$$$$$/| $$  | $$| $$      | $$      | $$$$$   | $$$$$$$/
// | $$      | $$  | $$| $$  $$$$   | $$   | $$__  $$| $$  | $$| $$      | $$      | $$__/   | $$__  $$
// | $$    $$| $$  | $$| $$\  $$$   | $$   | $$  \ $$| $$  | $$| $$      | $$      | $$      | $$  \ $$
// |  $$$$$$/|  $$$$$$/| $$ \  $$   | $$   | $$  | $$|  $$$$$$/| $$$$$$$$| $$$$$$$$| $$$$$$$$| $$  | $$
//  \______/  \______/ |__/  \__/   |__/   |__/  |__/ \______/ |________/|________/|________/|__/  |__/


scheduleControllers.controller('indexController', function indexController($scope, $http) {
    $scope.$on('$routeChangeStart', function(next, current) {
        mygnMenu._closeMenu();
    });
});


//   /$$$$$$  /$$$$$$$$ /$$$$$$  /$$$$$$$$ /$$$$$$$$
//  /$$__  $$|__  $$__//$$__  $$| $$_____/| $$_____/
// | $$  \__/   | $$  | $$  \ $$| $$      | $$
// |  $$$$$$    | $$  | $$$$$$$$| $$$$$   | $$$$$
//  \____  $$   | $$  | $$__  $$| $$__/   | $$__/
//  /$$  \ $$   | $$  | $$  | $$| $$      | $$
// |  $$$$$$/   | $$  | $$  | $$| $$      | $$
//  \______/    |__/  |__/  |__/|__/      |__/
//  /$$       /$$$$$$  /$$$$$$  /$$$$$$$$
// | $$      |_  $$_/ /$$__  $$|__  $$__/
// | $$        | $$  | $$  \__/   | $$
// | $$        | $$  |  $$$$$$    | $$
// | $$        | $$   \____  $$   | $$
// | $$        | $$   /$$  \ $$   | $$
// | $$$$$$$$ /$$$$$$|  $$$$$$/   | $$
// |________/|______/ \______/    |__/

scheduleControllers.controller('stafflist', function stafflist($scope, $http) {
});
