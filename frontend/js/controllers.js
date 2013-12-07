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


function deleteAllCookies() {
    var cookies = document.cookie.split(";");

    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        var eqPos = cookie.indexOf("=");
        var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
        document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
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
// scheduleControllers.controller('CalendarGenerator', function CalendarGenerator($scope, $modal) {
//     var monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
//     var dayNames = ["sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"];
//     $scope.calendar = function(year, monthIndex, day) {
//         if (year === undefined &&
//             month === undefined &&
//             day === undefined) {
//             var d = new Date();
//         year = d.getFullYear();
//         monthIndex = d.getMonth();
//         day = d.getDate();
//     }

//     var date = new Date(year, monthIndex, 1);
//     var current = new Date(year, monthIndex, day);
//     var month = {};
//     var weeks = [];

//     month.name = monthNames[monthIndex];
//     month.year = date.getFullYear();
//     month.number = date.getMonth();

//         // loop for weeks
//         for (var j=0 ; date.getMonth() === monthIndex ; j++) {
//             //loop for days
//             var weekdays = [];
//             for (var k=0 ; date.getMonth() === monthIndex ; k++){
//                 // console.log(scope.date);

//                 if (date.getDay() === 0 && k !== 0) {
//                     break;
//                 }
//                 // loop until you see the day go to 0 then jump out OR the month changes
//                 var dayNumber = date.getDate();
//                 var dayName   = dayNames[date.getDay()];
//                 var dayClass  = "";
//                 if (date.getTime() < current.getTime()) {
//                     dayClass = "inactive";
//                 }
//                 if (date.getDate()  === current.getDate() &&
//                     date.getMonth() === current.getMonth() &&
//                     date.getYear()  === current.getYear()) {
//                     dayClass = "today";
//             }
//             weekdays.push({number: dayNumber, name: dayName, class:dayClass});

//                 // increment the date
//                 date.setDate(date.getDate()+1);
//             }
//             weeks.push(weekdays);
//         }
//         // month.weeks = weeks;
//         // console.log(month);
//         month.weeks = weeks;
//         $scope.month = month;
//     };
//     var d = new Date();
//     $scope.calendar(d.getFullYear(), d.getMonth(), d.getDate());
//     $scope.dayNow = d.getDate();
//     $scope.monthNow = {"num": d.getMonth(), "name":monthNames[d.getMonth()]};
//     $scope.yearNow = d.getFullYear();
//     $scope.monthNames = monthNames;

//     var tempday = {};
//     tempday.year = 2013;
//     tempday.month = 11;
//     tempday.day = 1;
//     tempday.name = "Tuesday";
//     tempday.stylists = ["staff 1", "staff 2", "staff 3"];
//     $scope.day = tempday;

//     $scope.open = function(tempDay) {
//         var modalInstance = $modal.open({
//             template:   '<div class="modal">'+
//                             '<div class="modal-dialog">'+
//                                 '<div class="modal-content">'+
//                                     '<div class="modal-header">'+
//                                         '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>'+
//                                         '<h4 class="modal-title">Modal title</h4>'+
//                                     '</div>'+
//                                     '<div class="modal-body">'+
//                                         '<ul>'+
//                                             '<li ng-repeat="stylist in day.stylists">'+
//                                                 '<a>{{stylist}}</a>'+
//                                             '</li>'+
//                                         '</ul>'+
//                                     '</div>'+
//                                     '<div class="modal-footer">'+
//                                         '<button class="btn btn-primary" ng-click="ok()">OK</button>'+
//                                         '<button class="btn btn-warning" ng-click="cancel()">Cancel</button>'+
//                                     '</div>'+
//                                 '</div><!-- /.modal-content -->'+
//                             '</div><!-- /.modal-dialog -->'+
//                         '</div><!-- /.modal -->',
//             controller: CalendarModalInstance,
//             resolve: {
//                 day: function() {
//                     return tempDay;
//                 }
//             }
//         });
//         modalInstance.result.then(function(appointment) {
//             $scope.newAppointment = appointment;
//             console.log($scope.newAppointment);
//         });
//     };
// });
// scheduleControllers.controller('CalendarModalInstance', ['$scope', '$modalInstance', 'day'], function($scope, $modalInstance, day){
//     $scope.day = day;

//     $scope.ok = function() {
//         $modalInstance.close($scope.day);
//     };
//     $scope.cancel = function() {
//         $modalInstance.dismiss('cancel');
//     };
// })


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
        $http.get('http://home.joubin.me/salon-scheduler-api/users/me/', config).success(function(data) {
            $scope.tmpUserInfo = data;
            $rootScope.user.name = data.firstName+" "+data.lastName;
        });
    }
    $scope.user = {};
    // Defining user logged status
    $scope.loggedIn = false;
    // document.cookie = "loggedIn=false"


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
        if (readCookie('userType') === "staff") {
            $location.path('staff-landing');
            $scope.loggedIn = true;
            document.cookie = "loggedIn=true"

            // $scope.me();
            location.reload();
            return;
        }
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
                document.cookie = "loggedIn=false"

                
            });
        });
    // deleteAllCookies();
    // $location.path('login');
    // location.reload();

    };

    $scope.getLoginStatus = function() {

        Facebook.getLoginStatus(function(response) {
            console.log(response);

            if(response.status == 'connected') {
                $scope.loggedIn = true;
                document.cookie = "loggedIn=true"

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
                if(readCookie('loggedIn')){
                    return;
                }
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
scheduleControllers.controller('acceptAppointmentsController', function acceptAppointmentsController($scope, $http, $location) {
    var config = {headers:  {
        'authToken': readCookie("myAccessToken"),
        'Content-Type': 'application/json',
        // 'debug': 'asd'
    }
};

    $scope.acceptAppointment = function(){  
        data = {};
        var comment = $scope.appointment.comment;
        var id = $scope.appointment.ID;
        console.log("accept: "+comment+" - "+ id);
        data = {"appointmentStatus": "APPROVED", 
        "comment": comment};
        $http.put('http://home.joubin.me/salon-scheduler-api/appointments/'+id, data, config).success(function(data){    
                console.log("winning");
                $location.path('staff-landing');
                location.reload();
        }).error(function(data) {
                alert("There is a conflict");
                console.log("failing");
        });

    }

    $scope.denyAppointment = function(){  
        data = {};
        var comment = $scope.appointment.comment;
        var id = $scope.appointment.ID;
        console.log("accept: "+comment+" - "+ id);
        data = {"appointmentStatus": "REJECTED", 
        "comment": comment};
        $http.put('http://home.joubin.me/salon-scheduler-api/appointments/'+id, data, config).success(function(data){    
                console.log("winning");
                $location.path('staff-landing');
                location.reload();
        }).error(function(data) {
                alert("There is a conflict");
                console.log("failing");
        });

    }



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
// scheduleControllers.controller('ClientLandingController', function ClientLandingController($scope, $http, $rootScope) {
//     var config = {headers:  {
//             'authToken': readCookie("myAccessToken"),
//             'Content-Type': 'application/json',
//             'Cache-Control': 'no-cache',
//             // 'debug': 'asd'
//         }
//     };

// $http.get('http://home.joubin.me/salon-scheduler-api/appointments', config).success(function(data) {
//     // console.log(data);
//     $scope.appointments = [];
//     for (var something in data){
//         var tempAppointment = {};
//         var date = new Date(data[something].startTime);
//         tempAppointment.startTime = date;
//         tempAppointment.appointmentStatus = data[something].appointmentStatus;
//         if (data[something].appointmentStatus === "APPROVED") {
//             tempAppointment.myColor = "success";
//         };
//         if (data[something].appointmentStatus === "REJECTED" || data[something].appointmentStatus === "CANCELED") {
//             tempAppointment.myColor = "danger";
//         };
//         if (data[something].appointmentStatus === "NEW") {
//             tempAppointment.myColor = "warning";
//         };
//         if (data[something].appointmentStatus === "COMPLETED") {
//             tempAppointment.myColor = "info";

//         };
//         var shit = getFucked(data[something].links);
//         tempAppointment.firstName = shit.first;
//         tempAppointment.lastName = shit.last;
//     };
//     $scope.appointments.push(tempAppointment);
//     console.log($scope.appointments);

// });
// });


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

scheduleControllers.controller('adminController', function adminController($location, $scope, $http) {
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
        var config = {headers:  {
        'authToken': readCookie("myAccessToken"),
        'Content-Type': 'application/json',
        'Cache-Control': 'no-cache',
    }
};
  $http.get('http://home.joubin.me/salon-scheduler-api/users/me/', config).success(function(data) {
    console.log("Getting auth level");
    $scope.currentUserAuth = data;
    console.log($scope.currentUserAuth.type);
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

scheduleControllers.controller('editprofile', function editprofile($location, $scope, $http) {
    console.log("Edit Controller");
    var config = {headers:  {
        'authToken': readCookie("myAccessToken"),
        'Content-Type': 'application/json',
        'Cache-Control': 'no-cache',
    }
};

    $scope.getUserInfo = function(){  
      data = {};
      var getFirstName = $scope.user.name.split(" ");
      var userPhone = $scope.user.phone;
      var userHairColor = $scope.user.hairColor;
      var userHairLength = $scope.user.hairLength;
      var userEmail = $scope.user.email;
      console.log($scope.user.email);
      console.log($scope.user.phone);
      console.log($scope.user.hairColor);
      console.log($scope.user.hairLength);
      console.log(getFirstName);
      var facebookUser = readCookie('facebookActualUserName');
      data = {"firstName": getFirstName[0],
      "lastName": getFirstName[1],
      "phone": userPhone,
      "hairColor": userHairColor,
      "hairLength": userHairLength,
      "active": true,
      "email": userEmail,
      'avatarURL': 'graph.facebook.com/'+facebookUser+'/picture'};
      console.log(data);
      $http.put('http://home.joubin.me/salon-scheduler-api/users/me/',data, config).success(function(data){
                    console.log("winning");
                    $location.path('client-landing');
            }).error(function(data) {
                    console.log("failing");
            });
    }
});



//   /$$                           /$$                                                      
//  | $$                          |__/                                                      
//  | $$        /$$$$$$   /$$$$$$  /$$ /$$$$$$$                                             
//  | $$       /$$__  $$ /$$__  $$| $$| $$__  $$                                            
//  | $$      | $$  \ $$| $$  \ $$| $$| $$  \ $$                                            
//  | $$      | $$  | $$| $$  | $$| $$| $$  | $$                                            
//  | $$$$$$$$|  $$$$$$/|  $$$$$$$| $$| $$  | $$                                            
//  |________/ \______/  \____  $$|__/|__/  |__/                                            
//                       /$$  \ $$                                                          
//                      |  $$$$$$/                                                          
//                       \______/                                                           
//    /$$$$$$                        /$$                         /$$ /$$                    
//   /$$__  $$                      | $$                        | $$| $$                    
//  | $$  \__/  /$$$$$$  /$$$$$$$  /$$$$$$    /$$$$$$   /$$$$$$ | $$| $$  /$$$$$$   /$$$$$$ 
//  | $$       /$$__  $$| $$__  $$|_  $$_/   /$$__  $$ /$$__  $$| $$| $$ /$$__  $$ /$$__  $$
//  | $$      | $$  \ $$| $$  \ $$  | $$    | $$  \__/| $$  \ $$| $$| $$| $$$$$$$$| $$  \__/
//  | $$    $$| $$  | $$| $$  | $$  | $$ /$$| $$      | $$  | $$| $$| $$| $$_____/| $$      
//  |  $$$$$$/|  $$$$$$/| $$  | $$  |  $$$$/| $$      |  $$$$$$/| $$| $$|  $$$$$$$| $$      
//   \______/  \______/ |__/  |__/   \___/  |__/       \______/ |__/|__/ \_______/|__/      
//                                                                                          
//                                                                                          
//                                                                                          

scheduleControllers.controller('loginController', function loginController($location, $scope, $http, $rootScope) {

    $scope.loginUser = function(){  
      data = {};
      var user = $scope.user.username;
      var pass = $scope.user.password;
      
      console.log(user);
      console.log(pass);
      if (user == null || pass == null) {
        console.log("fuck no");
        return;
      }
      
      $http.get('http://home.joubin.me/salon-scheduler-api/authorize?username='+user+'&password='+pass,data).success(function(data){    
                    document.cookie="myAccessToken="+data.authToken;
                    document.cookie="userType=staff";
                    $location.path("staff-landing");
                    console.log("winning");
                    console.log($scope);
                    $rootScope.loggedIn = true;
                    document.cookie = "loggedIn=true"

                    $location.path('staff-landing');
            }).error(function(data) {
                    document.cookie="myAccessToken="+"NULL";
                    document.cookie="userType=null";

                    console.log("failling");
            });
    }
});


//  /$$                                 /$$ /$$                    
// | $$                                | $$|__/                    
// | $$        /$$$$$$   /$$$$$$   /$$$$$$$ /$$ /$$$$$$$   /$$$$$$ 
// | $$       /$$__  $$ |____  $$ /$$__  $$| $$| $$__  $$ /$$__  $$
// | $$      | $$  \ $$  /$$$$$$$| $$  | $$| $$| $$  \ $$| $$  \ $$
// | $$      | $$  | $$ /$$__  $$| $$  | $$| $$| $$  | $$| $$  | $$
// | $$$$$$$$|  $$$$$$/|  $$$$$$$|  $$$$$$$| $$| $$  | $$|  $$$$$$$
// |________/ \______/  \_______/ \_______/|__/|__/  |__/ \____  $$
//                                                        /$$  \ $$
//                                                       |  $$$$$$/
//                                                        \______/ 


scheduleControllers.controller('loadingController', function loadingController($location, $scope) {
    console.log("loading page controller, let see where it goes");
    if (readCookie('userType') === "staff") {
        $location.path('staff-landing');
    }else{
        $location.path('client-landing');            
    }
});


scheduleControllers.controller('createUser', function createUser($location, $scope) {

});
