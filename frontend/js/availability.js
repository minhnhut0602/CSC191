var availabilityModule = angular.module('availabilityModule', []);

availabilityModule.controller('TimepickerDemoCtrl', function TimepickerDemoCtrl($scope, $http, $location) {
    $scope.mytime = new Date();
    while ($scope.mytime.getDay() != 0) {//sunday
        $scope.mytime.setDate($scope.mytime.getDate()-1);
    }
    $scope.mytime.setMinutes(0);
    $scope.mytime.setSeconds(0);
    $scope.mytime.setMilliseconds(0);

    $scope.repeat = 1;
    $scope.availability = [];

    //sunday
    $scope.availability.push({});
    $scope.availability[0].startTime = $scope.mytime;
    $scope.availability[0].endTime = $scope.mytime;

    //monday
    $scope.mytime.setDate($scope.mytime.getDate()+1);
    $scope.availability.push({});
    $scope.availability[1].startTime = $scope.mytime;
    $scope.availability[1].endTime = $scope.mytime;

    //tuesday
    $scope.mytime.setDate($scope.mytime.getDate()+1);
    $scope.availability.push({});
    $scope.availability[2].startTime = $scope.mytime;
    $scope.availability[2].endTime = $scope.mytime;

    //wednesday
    $scope.mytime.setDate($scope.mytime.getDate()+1);
    $scope.availability.push({});
    $scope.availability[3].startTime = $scope.mytime;
    $scope.availability[3].endTime = $scope.mytime;

    //thursday
    $scope.mytime.setDate($scope.mytime.getDate()+1);
    $scope.availability.push({});
    $scope.availability[4].startTime = $scope.mytime;
    $scope.availability[4].endTime = $scope.mytime;

    //friday
    $scope.mytime.setDate($scope.mytime.getDate()+1);
    $scope.availability.push({});
    $scope.availability[5].startTime = $scope.mytime;
    $scope.availability[5].endTime = $scope.mytime;

    //saturday
    $scope.mytime.setDate($scope.mytime.getDate()+1);
    $scope.availability.push({});
    $scope.availability[6].startTime = $scope.mytime;
    $scope.availability[6].endTime = $scope.mytime;

    $scope.hstep = 1;
    $scope.mstep = 15;

    $scope.ismeridian = true;


    $scope.submit = function(repeatAmt) {
        var availability = {};
        availability.availability = [];
        if ($scope.repeat < 1) {
            $scope.repeat = 1;
        }
        for (var i=0 ; i<$scope.repeat ; i++) {
            for (day in $scope.availability) {
                availability.availability.push({
                    startDate: $scope.availability[day].startTime.getTime()+(86400000*i),
                    endDate: $scope.availability[day].endTime.getTime()+(86400000*i)
                });
            }
        }
        var config = {
            headers: {
                'authToken': readCookie("myAccessToken"),
                'Content-Type': 'application/json'
            }
        };
        console.log(availability);
        $http.put('http://home.joubin.me/salon-scheduler-api/availability/me/', availability, config).success(function(data) {
            console.log("availability saved");
            $location.path('staff-landing');
        }).error(function(data) {
            alert("OH NO SOMETHING BROKE\n"+ data);
        });
    };
});