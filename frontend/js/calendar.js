var calendarModule = angular.module('calendarModule', []);


calendarModule.directive('calendar', function($http, $location) {
    return {
        restrict: 'E',
        template:
                    '<div class="row">'+
                        '<ul class="pagination" >'+
                            '<li><a ng-click="calendar({true: yearNow+1, false: yearNow}[monthNow.num > (monthNow.num+0)%12],(monthNow.num+0)%12, dayNow)">{{monthNames[((monthNow.num+0)%12)]}} </a></li>'+
                            '<li><a ng-click="calendar({true: yearNow+1, false: yearNow}[monthNow.num > (monthNow.num+1)%12],(monthNow.num+1)%12,1)">{{monthNames[((monthNow.num+1)%12)]}} </a></li>'+
                            '<li><a ng-click="calendar({true: yearNow+1, false: yearNow}[monthNow.num > (monthNow.num+2)%12],(monthNow.num+2)%12,1)">{{monthNames[((monthNow.num+2)%12)]}} </a></li>'+
                            '<li><a ng-click="calendar({true: yearNow+1, false: yearNow}[monthNow.num > (monthNow.num+3)%12],(monthNow.num+3)%12,1)">{{monthNames[((monthNow.num+3)%12)]}} </a></li>'+
                            '<li><a ng-click="calendar({true: yearNow+1, false: yearNow}[monthNow.num > (monthNow.num+4)%12],(monthNow.num+4)%12,1)">{{monthNames[((monthNow.num+4)%12)]}} </a></li>'+
                            '<li><a ng-click="calendar({true: yearNow+1, false: yearNow}[monthNow.num > (monthNow.num+5)%12],(monthNow.num+5)%12,1)">{{monthNames[((monthNow.num+5)%12)]}} </a></li>'+
                            '<li><a ng-click="calendar({true: yearNow+1, false: yearNow}[monthNow.num > (monthNow.num+6)%12],(monthNow.num+6)%12,1)">{{monthNames[((monthNow.num+6)%12)]}} </a></li>'+
                            '<li><a ng-click="calendar({true: yearNow+1, false: yearNow}[monthNow.num > (monthNow.num+7)%12],(monthNow.num+7)%12,1)">{{monthNames[((monthNow.num+7)%12)]}} </a></li>'+
                            '<li><a ng-click="calendar({true: yearNow+1, false: yearNow}[monthNow.num > (monthNow.num+8)%12],(monthNow.num+8)%12,1)">{{monthNames[((monthNow.num+8)%12)]}} </a></li>'+
                            '<li><a ng-click="calendar({true: yearNow+1, false: yearNow}[monthNow.num > (monthNow.num+9)%12],(monthNow.num+9)%12,1)">{{monthNames[((monthNow.num+9)%12)]}} </a></li>'+
                            '<li><a ng-click="calendar({true: yearNow+1, false: yearNow}[monthNow.num > (monthNow.num+10)%12],(monthNow.num+10)%12,1)">{{monthNames[((monthNow.num+10)%12)]}} </a></li>'+
                            '<li><a ng-click="calendar({true: yearNow+1, false: yearNow}[monthNow.num > (monthNow.num+11)%12],(monthNow.num+11)%12,1)">{{monthNames[((monthNow.num+11)%12)]}} </a></li>'+
                        '</ul>'+
                    '</div>'+
                    '<div class="clearfix"></div>'+
                    '<div class="month">'+
                        '<div class="month-name"><h1>{{month.name}} {{month.year}}</h1></div>'+
                        '<div class="week-name">'+
                            '<div class="day-name sunday">Sunday</div>'+
                            '<div class="day-name monday">Monday</div>'+
                            '<div class="day-name tuesday">Tuesday</div>'+
                            '<div class="day-name wednesday">Wednesday</div>'+
                            '<div class="day-name thursday">Thursday</div>'+
                            '<div class="day-name friday">Friday</div>'+
                            '<div class="day-name saturday">Saturday</div>'+
                        '</div day>'+
                        '<div class="week" ng-repeat="week in month.weeks" ng-init="dayInfo = true">'+
                            '<div ng-repeat="day in week" class="day {{day.name}} {{day.class}}" ng-click="open(appointmentInfo, day.times)">'+
                                '<strong>{{day.number}}</strong>'+
                            '</div>'+
                            '<div class="day-info" ng-hide="dayInfo"></div>'+
                        '</div>'+
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
            $http.get('/salon-scheduler-api/appointmentTypes', config).success(function(appointmentTypes) {
                var types = [];
                console.log(appointmentTypes);
                for (type in appointmentTypes) {
                    types.push({
                        type: appointmentTypes[type].appointmentType,
                        duration: appointmentTypes[type].durationInMinutes,
                        id: appointmentTypes[type].id,
                        stylists: appointmentTypes[type].stylists
                    });
                }
                $scope.appointmentInfo.types = types;
                console.log(types);
            });
            $http.get('/salon-scheduler-api/users/stylists', config).success(function(stylistAccounts) {
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
                        tempDate.setHours(-1);
                        tempDate.setMinutes(45);
                        tempDate.setSeconds(0);
                        for (var l=0 ; l<96 ; l++) {
                            tempDate.setTime(tempDate.getTime()+(900000));
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
                                                '<h4 class="modal-title">Make A New Appointment for {{monthNum}}/{{dayNum}}/{{yearNum}}</h4>'+
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
                                                            '<select ng-model="newAppointment.type" class="selectpicker-popover" id="appointment-type" ng-options="i.type for i in newAppointment.stylist.types">' +
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
                    $http.post('/salon-scheduler-api/appointments', newAppointment, config).success(function(data) {
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
function CalendarModalInstance($scope, $http, $modalInstance, day, times) {
    $scope.day = day;
    $scope.day.times = [];
    for (stylist in $scope.day.stylists) { //iterate through stylists
        $scope.day.stylists[stylist].types = [];
        for (type in $scope.day.types) { //iterate through types
            for (id in $scope.day.types[type].stylists) { //iterate though stylists for this type
                if ($scope.day.types[type].stylists[id] === $scope.day.stylists[stylist].id) { //if stylist id matches add to the types
                    $scope.day.stylists[stylist].types.push($scope.day.types[type]);
                }
            }
        }
    }
    $scope.newAppointment = {
        stylist: {},
        type: {},
        time: {}
    };
    var currentDate = new Date(times[0].epoch);
    $scope.dayNum = currentDate.getDate();
    $scope.monthNum = currentDate.getMonth()+1;
    $scope.yearNum = currentDate.getFullYear();

    //TODO get availability for this day
    var selectedDay = new Date(times[0].epoch);
    var config = {
        headers: {
            authToken: readCookie('myAccessToken')
        }
    };
    $http.get('/salon-scheduler-api/availability?year='+selectedDay.getFullYear()+'&month='+selectedDay.getMonth()+'&day='+selectedDay.getDate(), config).success(function(data) {
        $scope.availability = data;
        console.log($scope.availability);
    }).error(function(data) {
        alert("Unable to get availability data. Please close the window and select the day again.");
    })
    $scope.$watch('newAppointment.stylist', function(newValue, oldValue, scope) {
        console.log(newValue);
        console.log($scope.availability);
        $scope.day.times = [];
        for (stylist in $scope.availability) {
            console.log("checking: "+$scope.availability[stylist].stylistID+" with "+newValue.id);
            if ($scope.availability[stylist].stylistID === newValue.id) {
                for (time in times) {
                    console.log($scope.availability[stylist].availability);
                    for (range in $scope.availability[stylist].availability) {
                        var startDate = new Date($scope.availability[stylist].availability[range].startDate);
                        var endDate = new Date($scope.availability[stylist].availability[range].endDate);

                        console.log("startDate: "+ startDate.toLocaleString()+ " endDate: "+ endDate.toLocaleString());

                        console.log("testing: "+times[time].epoch+" to "+$scope.availability[stylist].availability[range].startDate)
                        if (times[time].epoch >= $scope.availability[stylist].availability[range].startDate &&
                            times[time].epoch+($scope.newAppointment.type.duration*60000) <= $scope.availability[stylist].availability[range].endDate) {
                            console.log("adding: "+ times[time].time);
                            $scope.day.times.push(times[time]);
                        }
                    }
                }
            }
        }
    });
    $scope.$watch('newAppointment.type', function(newValue, oldValue, scope) {
        console.log(newValue);
        console.log($scope.availability);
        $scope.day.times = [];
        for (stylist in $scope.availability) {
            console.log("checking: "+$scope.availability[stylist].stylistID+" with "+newValue.id);
            if ($scope.availability[stylist].stylistID === $scope.newAppointment.stylist.id) {
                for (time in times) {
                    console.log($scope.availability[stylist].availability);
                    for (range in $scope.availability[stylist].availability) {
                        var startDate = new Date($scope.availability[stylist].availability[range].startDate);
                        var endDate = new Date($scope.availability[stylist].availability[range].endDate);

                        console.log("startDate: "+ startDate.toLocaleString()+ " endDate: "+ endDate.toLocaleString());

                        console.log("testing: "+times[time].epoch+" to "+$scope.availability[stylist].availability[range].startDate)
                        if (times[time].epoch >= $scope.availability[stylist].availability[range].startDate &&
                            times[time].epoch+(newValue.duration*60000) <= $scope.availability[stylist].availability[range].endDate) {
                            console.log("adding: "+ times[time].time);
                            $scope.day.times.push(times[time]);
                        }
                    }
                }
            }
        }
    });

    $scope.ok = function() {
        $modalInstance.close($scope.newAppointment);
    };
    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };
}




















calendarModule.directive('staffcalendar', function($http, $location) {
    return {
        restrict: 'E',
        template:
                    '<div class="row">'+
                        '<ul class="pagination" >'+
                            '<li><a ng-click="calendar({true: yearNow+1, false: yearNow}[monthNow.num > (monthNow.num+0)%12],(monthNow.num+0)%12, dayNow)">{{monthNames[((monthNow.num+0)%12)]}} </a></li>'+
                            '<li><a ng-click="calendar({true: yearNow+1, false: yearNow}[monthNow.num > (monthNow.num+1)%12],(monthNow.num+1)%12,1)">{{monthNames[((monthNow.num+1)%12)]}} </a></li>'+
                            '<li><a ng-click="calendar({true: yearNow+1, false: yearNow}[monthNow.num > (monthNow.num+2)%12],(monthNow.num+2)%12,1)">{{monthNames[((monthNow.num+2)%12)]}} </a></li>'+
                            '<li><a ng-click="calendar({true: yearNow+1, false: yearNow}[monthNow.num > (monthNow.num+3)%12],(monthNow.num+3)%12,1)">{{monthNames[((monthNow.num+3)%12)]}} </a></li>'+
                            '<li><a ng-click="calendar({true: yearNow+1, false: yearNow}[monthNow.num > (monthNow.num+4)%12],(monthNow.num+4)%12,1)">{{monthNames[((monthNow.num+4)%12)]}} </a></li>'+
                            '<li><a ng-click="calendar({true: yearNow+1, false: yearNow}[monthNow.num > (monthNow.num+5)%12],(monthNow.num+5)%12,1)">{{monthNames[((monthNow.num+5)%12)]}} </a></li>'+
                            '<li><a ng-click="calendar({true: yearNow+1, false: yearNow}[monthNow.num > (monthNow.num+6)%12],(monthNow.num+6)%12,1)">{{monthNames[((monthNow.num+6)%12)]}} </a></li>'+
                            '<li><a ng-click="calendar({true: yearNow+1, false: yearNow}[monthNow.num > (monthNow.num+7)%12],(monthNow.num+7)%12,1)">{{monthNames[((monthNow.num+7)%12)]}} </a></li>'+
                            '<li><a ng-click="calendar({true: yearNow+1, false: yearNow}[monthNow.num > (monthNow.num+8)%12],(monthNow.num+8)%12,1)">{{monthNames[((monthNow.num+8)%12)]}} </a></li>'+
                            '<li><a ng-click="calendar({true: yearNow+1, false: yearNow}[monthNow.num > (monthNow.num+9)%12],(monthNow.num+9)%12,1)">{{monthNames[((monthNow.num+9)%12)]}} </a></li>'+
                            '<li><a ng-click="calendar({true: yearNow+1, false: yearNow}[monthNow.num > (monthNow.num+10)%12],(monthNow.num+10)%12,1)">{{monthNames[((monthNow.num+10)%12)]}} </a></li>'+
                            '<li><a ng-click="calendar({true: yearNow+1, false: yearNow}[monthNow.num > (monthNow.num+11)%12],(monthNow.num+11)%12,1)">{{monthNames[((monthNow.num+11)%12)]}} </a></li>'+
                        '</ul>'+
                    '</div>'+
                    '<div class="clearfix"></div>'+
                    '<div class="month">'+
                        '<div class="month-name"><h1>{{month.name}} {{month.year}}</h1></div>'+
                        '<div class="week-name">'+
                            '<div class="day-name sunday">Sunday</div>'+
                            '<div class="day-name monday">Monday</div>'+
                            '<div class="day-name tuesday">Tuesday</div>'+
                            '<div class="day-name wednesday">Wednesday</div>'+
                            '<div class="day-name thursday">Thursday</div>'+
                            '<div class="day-name friday">Friday</div>'+
                            '<div class="day-name saturday">Saturday</div>'+
                        '</div day>'+
                        '<div class="week" ng-repeat="week in month.weeks" ng-init="dayInfo = true">'+
                            '<div ng-repeat="day in week" class="day {{day.name}} {{day.class}}" ng-click="open(appointmentInfo, day.times)">'+
                                '<strong>{{day.number}}</strong>'+
                            '</div>'+
                            '<div class="day-info" ng-hide="dayInfo"></div>'+
                        '</div>'+
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
            $http.get('/salon-scheduler-api/appointmentTypes', config).success(function(appointmentTypes) {
                var types = [];
                console.log(appointmentTypes);
                for (type in appointmentTypes) {
                    types.push({
                        type: appointmentTypes[type].appointmentType,
                        duration: appointmentTypes[type].durationInMinutes,
                        id: appointmentTypes[type].id,
                        stylists: appointmentTypes[type].stylists
                    });
                }
                $scope.appointmentInfo.types = types;
                console.log(types);
            });
            $http.get('/salon-scheduler-api/users/me/', config).success(function(stylistAccounts) {
                var stylists = [];
                stylists.push({
                    name: stylistAccounts.firstName +' '+ stylistAccounts.lastName,
                    id: stylistAccounts.id
                });
                $scope.appointmentInfo.stylists = stylists;
                console.log(stylists);
            });
            $http.get('/salon-scheduler-api/users/clients', config).success(function(clientAccounts) {
                var clients = [];
                for (stylist in clientAccounts) {
                    clients.push({
                        name: clientAccounts[stylist].firstName +' '+ clientAccounts[stylist].lastName,
                        id: clientAccounts[stylist].id
                    });
                }
                $scope.appointmentInfo.clients = clients;
                console.log(clients);
            });
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
                        tempDate.setHours(-1);
                        tempDate.setMinutes(45);
                        tempDate.setSeconds(0);
                        for (var l=0 ; l<96 ; l++) {
                            tempDate.setTime(tempDate.getTime()+(900000));
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
                                                '<h4 class="modal-title">Make A New Appointment for {{monthNum}}/{{dayNum}}/{{yearNum}}</h4>'+
                                            '</div>'+
                                            '<div class="modal-body">'+
                                                '<form class="new-appointment-form">' +
                                                    '<div class="clearfix">' +
                                                        '<label class="control-label appointment-type-label">Client</label>' +
                                                        '<div class="appointment-stylist-selection">' +
                                                            '<select ng-model="newAppointment.client" class="selectpicker-popover" id="appointment-stylist" ng-options="i.name for i in day.clients">' +
                                                            '</select>' +
                                                        '</div>' +
                                                    '</div>' +
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
                                                            '<select ng-model="newAppointment.type" class="selectpicker-popover" id="appointment-type" ng-options="i.type for i in newAppointment.stylist.types">' +
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
                    controller: StaffCalendarModalInstance,
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
                    newAppointment.clientID = appointment.client.id;
                    newAppointment.appointmentType = appointment.type.type;
                    newAppointment.appointmentTypeID = appointment.type.id;
                    newAppointment.startTime = appointment.time.epoch;
                    newAppointment.endTime = appointment.time.epoch + (appointment.type.duration*60000);
                    newAppointment.appointmentStatus = "APPROVED";

                    var config = {
                        headers: {
                            'authToken': readCookie('myAccessToken')
                        }
                    }
                    $http.post('/salon-scheduler-api/appointments', newAppointment, config).success(function(data) {
                        console.log(data);
                        $location.path('staff-landing');
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



function StaffCalendarModalInstance($scope, $http, $modalInstance, day, times) {
    $scope.day = day;
    $scope.day.times = [];
    console.log($scope.day);
    for (stylist in $scope.day.stylists) { //iterate through stylists
        $scope.day.stylists[stylist].types = [];
        for (type in $scope.day.types) { //iterate through types
            for (id in $scope.day.types[type].stylists) { //iterate though stylists for this type
                if ($scope.day.types[type].stylists[id] === $scope.day.stylists[stylist].id) { //if stylist id matches add to the types
                    $scope.day.stylists[stylist].types.push($scope.day.types[type]);
                }
            }
        }
    }
    $scope.newAppointment = {
        client: {},
        stylist: {},
        type: {},
        time: {}
    };
    var currentDate = new Date(times[0].epoch);
    $scope.dayNum = currentDate.getDate();
    $scope.monthNum = currentDate.getMonth()+1;
    $scope.yearNum = currentDate.getFullYear();

    //TODO get availability for this day
    var selectedDay = new Date(times[0].epoch);
    var config = {
        headers: {
            authToken: readCookie('myAccessToken')
        }
    };
    $http.get('/salon-scheduler-api/availability?year='+selectedDay.getFullYear()+'&month='+selectedDay.getMonth()+'&day='+selectedDay.getDate(), config).success(function(data) {
        $scope.availability = data;
        console.log($scope.availability);
    }).error(function(data) {
        alert("Unable to get availability data. Please close the window and select the day again.");
    })
    $scope.$watch('newAppointment.stylist', function(newValue, oldValue, scope) {
        console.log(newValue);
        console.log($scope.availability);
        $scope.day.times = [];
        for (stylist in $scope.availability) {
            console.log("checking: "+$scope.availability[stylist].stylistID+" with "+newValue.id);
            if ($scope.availability[stylist].stylistID === newValue.id) {
                for (time in times) {
                    console.log($scope.availability[stylist].availability);
                    for (range in $scope.availability[stylist].availability) {
                        var startDate = new Date($scope.availability[stylist].availability[range].startDate);
                        var endDate = new Date($scope.availability[stylist].availability[range].endDate);

                        console.log("startDate: "+ startDate.toLocaleString()+ " endDate: "+ endDate.toLocaleString());

                        console.log("testing: "+times[time].epoch+" to "+$scope.availability[stylist].availability[range].startDate)
                        if (times[time].epoch >= $scope.availability[stylist].availability[range].startDate &&
                            times[time].epoch+($scope.newAppointment.type.duration*60000) <= $scope.availability[stylist].availability[range].endDate) {
                            console.log("adding: "+ times[time].time);
                            $scope.day.times.push(times[time]);
                        }
                    }
                }
            }
        }
    });
    $scope.$watch('newAppointment.type', function(newValue, oldValue, scope) {
        console.log(newValue);
        console.log($scope.availability);
        $scope.day.times = [];
        for (stylist in $scope.availability) {
            console.log("checking: "+$scope.availability[stylist].stylistID+" with "+newValue.id);
            if ($scope.availability[stylist].stylistID === $scope.newAppointment.stylist.id) {
                for (time in times) {
                    console.log($scope.availability[stylist].availability);
                    for (range in $scope.availability[stylist].availability) {
                        var startDate = new Date($scope.availability[stylist].availability[range].startDate);
                        var endDate = new Date($scope.availability[stylist].availability[range].endDate);

                        console.log("startDate: "+ startDate.toLocaleString()+ " endDate: "+ endDate.toLocaleString());

                        console.log("testing: "+times[time].epoch+" to "+$scope.availability[stylist].availability[range].startDate)
                        if (times[time].epoch >= $scope.availability[stylist].availability[range].startDate &&
                            times[time].epoch+(newValue.duration*60000) <= $scope.availability[stylist].availability[range].endDate) {
                            console.log("adding: "+ times[time].time);
                            $scope.day.times.push(times[time]);
                        }
                    }
                }
            }
        }
    });

    $scope.ok = function() {
        $modalInstance.close($scope.newAppointment);
    };
    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };
}