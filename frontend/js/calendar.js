var calendarModule = angular.module('calendarModule', []);


calendarModule.directive('calendar', function($http, $location) {
    return {
        restrict: 'E',
        template:   '<div class="week" ng-repeat="week in month.weeks" ng-init="dayInfo = true">'+
                        '<div ng-repeat="day in week" class="day {{day.name}} {{day.class}}" ng-click="open(appointmentInfo, day.times)">'+
                            '<strong>{{day.number}}</strong>'+
                        '</div>'+
                        '<div class="day-info" ng-hide="dayInfo"></div>'+
                    '</div>',
        scope: {
            day: '='
        },
        controller: function($scope, $modal, $http) {
            $scope.appointmentInfo = {};
            var config = {
                headers: {
                    'authToken': readCookie('myAccessToken')
                }
            };
            $http.get('http://home.joubin.me/salon-scheduler-api/appointmentTypes', config).success(function(appointmentTypes) {
                var types = [];
                console.log(appointmentTypes);
                for (type in appointmentTypes) {
                    types.push({
                        type: appointmentTypes[type].appointmentType,
                        duration: appointmentTypes[type].durationInMinutes,
                        id: appointmentTypes[type].id
                    });
                }
                $scope.appointmentInfo.types = types;
                console.log(types);
            });
            $http.get('http://home.joubin.me/salon-scheduler-api/users/stylists', config).success(function(stylistAccounts) {
                var stylists = [];
                for (stylist in stylistAccounts) {
                    stylists.push({
                        name: stylistAccounts[stylist].firstName +' '+ stylistAccounts[stylist].lastName,
                        id: stylistAccounts[stylist].id
                    });
                }
                $scope.appointmentInfo.stylists = stylists;
                console.log(stylists);
            });
            console.log($scope.appointmentInfo);

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
                        var dayTimes = []
                        var tempDate = new Date(date.getTime());
                        tempDate.setHours(7);
                        tempDate.setMinutes(30);
                        tempDate.setSeconds(0);
                        for (var l=0 ; l<24 ; l++) {
                            tempDate.setTime(tempDate.getTime()+(1800000));
                            dayTimes.push({
                                epoch: tempDate.getTime(),
                                time: tempDate.toLocaleTimeString()
                            });
                        }
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
                    weekdays.push({number: dayNumber, name: dayName, class:dayClass, times: dayTimes});

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

            var tempday = {};
            tempday.year = 2013;
            tempday.month = 11;
            tempday.day = 1;
            tempday.name = "Tuesday";
            tempday.stylists = ["staff 1", "staff 2", "staff 3"];
            $scope.day = tempday;

            $scope.open = function(tempDay, times) {
                var modalInstance = $modal.open({
                    template:   '<div class="modal">'+
                                    '<div class="modal-dialog">'+
                                        '<div class="modal-content">'+
                                            '<div class="modal-header">'+
                                                '<button type="button" class="close" aria-hidden="true" ng-click="cancel()">&times;</button>'+
                                                '<h4 class="modal-title">Make A New Appointment for {{day.month}}/{{day.date}}/{{day.year}}</h4>'+
                                            '</div>'+
                                            '<div class="modal-body">'+
                                                '<form class="new-appointment-form">' +
                                                    '<div class="clearfix">' +
                                                        '<label class="control-label appointment-type-label">Stylist</label>' +
                                                        '<div class="appointment-stylist-selection">' +
                                                            '<select ng-model="newAppointment.stylist" class="selectpicker-popover" id="appointment-stylist" ng-options="i.name for i in day.stylists">' +
                                                            '</select>' +
                                                        '</div>' +
                                                    '</div>' +
                                                    '<div class="clearfix">' +
                                                        '<label class="control-label appointment-type-label">Type</label>' +
                                                        '<div class="appointment-type-selection">' +
                                                            '<select ng-model="newAppointment.type" class="selectpicker-popover" id="appointment-type" ng-options="i.type for i in day.types">' +
                                                            '</select>' +
                                                        '</div>' +
                                                    '</div>' +
                                                     '<div class="clearfix">' +
                                                        '<label class="control-label appointment-type-label">Time</label>' +
                                                        '<div class="appointment-type-selection">' +
                                                            '<select ng-model="newAppointment.time" class="selectpicker-popover" id="appointment-type" ng-options="i.time for i in day.times">' +
                                                            '</select>' +
                                                        '</div>' +
                                                    '</div>' +
                                                '</form>'+
                                            '</div>'+
                                            '<div class="modal-footer">'+
                                                '<button class="btn btn-primary" ng-click="ok()">OK</button>'+
                                                '<button class="btn btn-danger" ng-click="cancel()">Cancel</button>'+
                                            '</div>'+
                                        '</div><!-- /.modal-content -->'+
                                    '</div><!-- /.modal-dialog -->'+
                                '</div><!-- /.modal -->',
                    controller: CalendarModalInstance,
                    resolve: {
                        day: function() {
                            return tempDay;
                        },
                        times: function() {
                            return times
                        }
                    }
                });
                modalInstance.result.then(function(appointment) {
                    console.log(appointment);

                    var newAppointment = {};
                    newAppointment.stylistID = appointment.stylist.id;
                    newAppointment.appointmentType = appointment.type.type;
                    newAppointment.appointmentTypeID = appointment.type.id;
                    newAppointment.startTime = appointment.time.epoch;
                    newAppointment.endTime = appointment.time.epoch + (appointment.type.duration*60000);

                    var config = {
                        headers: {
                            'authToken': readCookie('myAccessToken')
                        }
                    }
                    $http.post('http://home.joubin.me/salon-scheduler-api/appointments', newAppointment, config).success(function(data) {
                        console.log(data);
                        $location.path('client-landing');
                    }).error(function(data){
                        alert("Error: it seems that "+ data);
                    });

                });
            };
            $scope.makeAppointment = function() {
                //TODO do things here to make the appointment
            };
        },
        link: function(scope, elm, attr) {

        }
    }
});
// scheduleControllers.controller('ModalInstanceController', [], function($scope, $modalInstance, day) {
//  $scope.day = day;

//  $scope.ok = function() {
//      $modalInstance.close($scope.day);
//  };
//  $scope.cancel = function() {
//      $modalInstance.dismiss('cancel');
//  };
// })
calendarModule.controller('CalendarGenerator', function CalendarGenerator($scope, $modal) {
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
function CalendarModalInstance($scope, $modalInstance, day, times) {
    $scope.day = day;
    $scope.day.times = times;
    $scope.newAppointment = {
        stylist: day.stylists[0],
        type: day.types[0],
        time: times[0]
    };

    $scope.ok = function() {
        $modalInstance.close($scope.newAppointment);
    };
    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };
}