function readCookie(name) {
    name += '=';
    for (var ca = document.cookie.split(/;\s*/), i = ca.length - 1; i >= 0; i--)
        if (!ca[i].indexOf(name))
            return ca[i].replace(name, '');
    }

function getFucked(inTheAss){
    for (var link in inTheAss){
        if (link.rel === "stylist") {
           return $http.get(link.href, config).success(function(data2) {
                console.log("reached the bottom of ass, also known as ahole"); // this should never happen
                return {first: data2.firstName, last: data2.lastName}; // = something
            }).error(function(data2){
                return null;
            });
        }
    }
    
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
scheduleControllers.controller('AuthController', ['$scope', '$rootScope', '$location', 'Facebook', '$http', function($scope, $rootScope, $location, Facebook, $http) {
    $scope.getInfo = function(){
        var config = {headers:  {
                'authToken': readCookie("myAccessToken"),
                'Content-Type': 'application/json',
                // 'debug': 'asd'
            }
        };
        $http.get('http://home.joubin.me/salon-scheduler-api/users/me', config).success(function(data) {
            $scope.tmpUserInfo = data;
        });
    }
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
        Facebook.getLoginStatus(function(response) {
            console.log(response);

            if(response.status == 'connected') {
                $scope.loggedIn = true;
                $scope.me();
                document.cookie="myAccessToken="+response.authResponse["accessToken"];
                document.cookie="myID="+response.authResponse["userID"];
                $scope.getInfo();
                $scope.$watch('tmpUserInfo', function(newValue, oldValue, scope) {
                    console.log($scope.tmpUserInfo);
                     if ($scope.tmpUserInfo.firstName == null) {
                        $location.path('edit-profile');
                    }
                 }, true);
                $scope.user = response;
                $rootScope.user = $scope.user;
                $rootScope.facebook = response;
               
                $location.path('client-landing');
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
                document.cookie = "facebookUsersName="+response.first_name;
                document.cookie = "facebookUsersLastName="+response.last_name;
                document.cookie = "facebookActualUserName="+response.username;
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
// scheduleControllers.controller('StaffLandingController', function StaffLandingController($scope, $http) {
//   $http.get('json/appointments/staffAppointments.json').success(function(data) {
//     $scope.appointments = data;
//   });
// });
scheduleControllers.controller('StaffLandingController', function StaffLandingController($scope, $http) {
    var config = {headers:  {
        'authToken': readCookie("myAccessToken"),
        'Content-Type': 'application/json',
        // 'debug': 'asd'
    }
};

$http.get('http://home.joubin.me/salon-scheduler-api/appointments', config).success(function(data) {
    // console.log(data);
    $scope.appointments = [];
    for (var something in data){
        console.log(data[something]);
        var tempAppointment = {};
        var date = new Date(data[something].startTime);
        tempAppointment.startTime = date;
        tempAppointment.appointmentStatus = data[something].appointmentStatus;
        console.log("fuck"+data[something].appointmentStatus);
        if (data[something].appointmentStatus === "APPROVED") {
            tempAppointment.myColor = "success";
        };
        if (data[something].appointmentStatus === "REJECTED" || data[something].appointmentStatus === "CANCELED") {
            tempAppointment.myColor = "danger";
        };
        if (data[something].appointmentStatus === "NEW") {
            tempAppointment.myColor = "warning";
        };
        if (data[something].appointmentStatus === "COMPLETED") {
            tempAppointment.myColor = "info";

        };

        $scope.appointments.push(tempAppointment);
        console.log($scope.appointments);
    }
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
            'authToken': readCookie("myAccessToken"),
            'Content-Type': 'application/json',
            'Cache-Control': 'no-cache',
            // 'debug': 'asd'
        }
    };

$http.get('http://home.joubin.me/salon-scheduler-api/appointments', config).success(function(data) {
    // console.log(data);
    $scope.appointments = [];
    for (var something in data){
        var tempAppointment = {};
        var date = new Date(data[something].startTime);
        tempAppointment.startTime = date;
        tempAppointment.appointmentStatus = data[something].appointmentStatus;
        if (data[something].appointmentStatus === "APPROVED") {
            tempAppointment.myColor = "success";
        };
        if (data[something].appointmentStatus === "REJECTED" || data[something].appointmentStatus === "CANCELED") {
            tempAppointment.myColor = "danger";
        };
        if (data[something].appointmentStatus === "NEW") {
            tempAppointment.myColor = "warning";
        };
        if (data[something].appointmentStatus === "COMPLETED") {
            tempAppointment.myColor = "info";

        };
        var shit = getFucked(data[something].links);
        tempAppointment.firstName = shit.first;
        tempAppointment.lastName = shit.last;
    };
    $scope.appointments.push(tempAppointment);
    console.log($scope.appointments);

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
        'authToken': readCookie("myAccessToken"),
        'Content-Type': 'application/json',
        'Cache-Control': 'no-cache',
    }
};
$http.get('http://home.joubin.me/salon-scheduler-api/users', config).success(function(data) {
    $scope.users = data;
}).error(function(data2){
    $score.user = "You have no access here";
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
    //TODO 
    /*
    We can use this to supply the staff list for the calendar. Maybe this should go in the calendar controller.
    But I thought it was already a big controller by itself. 
    */
});

//  /$$      /$$ /$$                             
// | $$$    /$$$|__/                             
// | $$$$  /$$$$ /$$  /$$$$$$$  /$$$$$$$         
// | $$ $$/$$ $$| $$ /$$_____/ /$$_____/         
// | $$  $$$| $$| $$|  $$$$$$ | $$               
// | $$\  $ | $$| $$ \____  $$| $$               
// | $$ \/  | $$| $$ /$$$$$$$/|  $$$$$$$         
// |__/     |__/|__/|_______/  \_______/         



//  /$$   /$$                                    
// | $$  | $$                                    
// | $$  | $$  /$$$$$$$  /$$$$$$   /$$$$$$       
// | $$  | $$ /$$_____/ /$$__  $$ /$$__  $$      
// | $$  | $$|  $$$$$$ | $$$$$$$$| $$  \__/      
// | $$  | $$ \____  $$| $$_____/| $$            
// |  $$$$$$/ /$$$$$$$/|  $$$$$$$| $$            
//  \______/ |_______/  \_______/|__/            



//  /$$            /$$$$$$                       
// |__/           /$$__  $$                      
//  /$$ /$$$$$$$ | $$  \__//$$$$$$               
// | $$| $$__  $$| $$$$   /$$__  $$              
// | $$| $$  \ $$| $$_/  | $$  \ $$              
// | $$| $$  | $$| $$    | $$  | $$              
// | $$| $$  | $$| $$    |  $$$$$$/              
// |__/|__/  |__/|__/     \______/               

//TODO 
/*
* make the controller add information such as a url to image into the cookie.
* save auth type into a cookie
* and other misc information that can be used to change the look of the UI.
* IE: if admin display some buttons that arent normally there for regular users 
*/



// EDIT edit-profile

scheduleControllers.controller('edit-profile', function userAuthController($scope, $http) {
  $http.put('http://home.joubin.me/salon-scheduler-api/users').success(function(data) {
    $scope.currentUserAuth = data;
});
});