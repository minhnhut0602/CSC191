var availabilityModule = angular.module('availabilityModule', []);

availabilityModule.controller('TimepickerDemoCtrl', function TimepickerDemoCtrl($scope) {
    $scope.mytime = new Date();
    while ($scope.mytime.getDay() != 1) {//monday
        $scope.setDate($scope.getDate()-1);
    }
    $scope.mytime.setMinutes(0);
    $scope.mytime.setSeconds(0);
    $scope.mytime.setMilliseconds(0);

    $scope.monday    = $scope.mytime;

    $scope.tuesday   = $scope.monday;
    $scope.tuesday.setDate($scope.tuesday.getDate()+1);

    $scope.wednesday = $scope.tuesday;
    $scope.wednesday.setDate($scope.wednesday.getDate()+1);

    $scope.thursday  = $scope.wednesday;
    $scope.thursday.setDate($scope.thursday.getDate()+1);

    $scope.friday    = $scope.thursday;
    $scope.friday.setDate($scope.friday.getDate()+1);

    $scope.hstep = 1;
    $scope.mstep = 15;

    $scope.ismeridian = true;
    $scope.toggleMode = function() {
        $scope.ismeridian = ! $scope.ismeridian;
    };

    $scope.update = function() {
        var d = new Date();
        d.setHours( 14 );
        d.setMinutes( 0 );
        $scope.mytime = d;
    };

    $scope.changed = function () {
        console.log('Time changed to: ' + $scope.mytime);
    };

    $scope.clear = function() {
        $scope.mytime = null;
    };
});