function readCookie(name) {
    name += '=';
    for (var ca = document.cookie.split(/;\s*/), i = ca.length - 1; i >= 0; i--)
        if (!ca[i].indexOf(name))
            return ca[i].replace(name, '');
    }
var scheduleDirectives = angular.module('scheduleDirectives', []);

scheduleDirectives.directive('calendarPopover', function() {
    return {
        restrict: 'A',
        link: function(scope, elm, attr) {
            if (!elm.hasClass('inactive')) {
                elm.popover({html: true, placement:'left auto', content: function() {
                    var year = $(this).attr('data-year');
                    var month = $(this).attr('data-month');
                    var day = $(this).attr('data-day');
                    var date = new Date(year, month, day);

                    return  '<form class="new-appointment-form">' +
                                '<div class="clearfix">' +
                                    '<label class="control-label appointment-type-label">Stylist</label>' +
                                    '<div class="appointment-stylist-selection">' +
                                        '<select class="selectpicker-popover" id="appointment-stylist" data-width="100%">' +
                                            '<option>Alexa Johnson</option>' +
                                            '<option>Amy Brown</option>' +
                                            '<option>Amanda Rinkey</option>' +
                                            '<option>Cris Rio</option>' +
                                            '<option>Shoni Summers</option>' +
                                        '</select>' +
                                    '</div>' +
                                '</div>' +
                                '<div class="clearfix">' +
                                    '<label class="control-label appointment-type-label">Type</label>' +
                                    '<div class="appointment-type-selection">' +
                                        '<select class="selectpicker-popover" id="appointment-type" data-width="100%">' +
                                            '<option>Color</option>' +
                                            '<option>Cut</option>' +
                                            '<option>Perm</option>' +
                                            '<option>Hilight</option>' +
                                            '<option>Shave</option>' +
                                        '</select>' +
                                    '</div>' +
                                '</div>' +
                                 '<div class="clearfix">' +
                                    '<label class="control-label appointment-type-label">Type</label>' +
                                    '<div class="appointment-type-selection">' +
                                        '<select class="selectpicker-popover" id="appointment-type" data-width="100%">' +
                                            '<option>12:00pm</option>' +
                                            '<option>1:00pm</option>' +
                                            '<option>2:00pm</option>' +
                                            '<option>3:00pm</option>' +
                                            '<option>4:00pm</option>' +
                                        '</select>' +
                                    '</div>' +
                                '</div>' +
                                '<div class="form-group">' +
                                    '<div class="submit-button">' +
                                        '<button type="submit" class="btn btn-primary">Done</button>' +
                                    '</div>' +
                                '</div>' +
                            '</form>';
                }});
            }

            elm.on('shown.bs.popover', function () {
                $('.selectpicker-popover').selectpicker();
            });
        }
    }
});

scheduleDirectives.directive('stylistselect', function($http, $log) {
    return {
        restrict: 'E',
        template: '<select ng-model="selectedItem" multiple ng-options="value.id as value.name for (key, value) in selects" data-style="btn-primary" bs-select></select>',
        replace: true,
        controller: function($scope, $http) {

        },
        link: function(scope, elm, attr) {
            // elm.selectpicker({container: 'body'});
            scope.selects = [];
            $http.get('json/stylists.json').success(function(data) {
                scope.selects = data;
            });
            console.log(scope.selects);

            // scope.$watch('allStaff', function(newValue, oldValue, scope) {
            //     scope.allStaff = newValue;
            //     elm.selectpicker('refresh');
            // }, true);
        }
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

scheduleDirectives.directive('appointmentgetter', function($http) {
    return {
        restrict: 'A',
        template:   '<div ng-repeat="appointment in appointments">'+
                        '<ng-form ng-controller="acceptAppointmentsController">'+
                        '<div class="alert alert-{{appointment.myColor}} " ng-click="calendar(appointment.startTime.getFullYear(),appointment.startTime.getMonth(),appointment.startTime.getDate())">You have an appointment with <stylistname stylisturl="appointment.stylist"/> on {{appointment.dayName}}, {{appointment.monthName}} {{appointment.dateNum}}{{appointment.dateNumSuffix}}, {{appointment.yearNum}} at {{appointment.startTime.toLocaleTimeString()}}'+
                        '<div ng-hide="appointment.comment == \'Pending\'"><stylistname stylisturl=appointment.stylist/> said: {{appointment.comment}}</div>'+
                        '<input ng-disabled="appointment.active" class="form-control" id="disabledInput" type="hidden" placeholder="{{appointment.ID}}" ng-model="appointment.ID"><br/>'+
                        '<button type="button" class="btn btn-danger" ng-click="cancelAppointment()">Cancel</button>'+
                        '</ng-form>'+
                        '</div>'+
                    '</div>',
        link: function(scope, elm, attr) {
            var config = { headers:  {
                    'authToken': readCookie("myAccessToken"),
                    'Content-Type': 'application/json',
                    'Cache-Control': 'no-cache',
                    // 'debug': 'asd'
                }
            };

            $http.get('http://home.joubin.me/salon-scheduler-api/appointments', config).success(function(data) {
                scope.appointments = [];

                for (var something in data){
                    var tempAppointment = {};
                    var date = new Date(data[something].startTime);
                    tempAppointment.startTime = date;
                    tempAppointment.ID = data[something].id;
                    tempAppointment.appointmentStatus = data[something].appointmentStatus;
                    tempAppointment.comment = data[something].comment;
                    tempAppointment.appStatus = "";

                    var monthNum = tempAppointment.startTime.getMonth();
                    var dayNum = tempAppointment.startTime.getDay();
                    var dateNum = tempAppointment.startTime.getDate();
                    var yearNum = tempAppointment.startTime.getFullYear();

                    tempAppointment.yearNum = yearNum;

                    var monthName = "";
                    switch(monthNum)
                    {
                        case 0:
                            monthName = "January"
                        break;
                        case 1:
                            monthName = "February"
                        break;
                        case 2:
                            monthName = "March"
                        break;
                        case 3:
                            monthName = "April"
                        break;
                        case 4:
                            monthName = "May"
                        break;
                        case 5:
                            monthName = "June"
                        break;
                        case 6:
                            monthName = "July"
                        break;
                        case 7:
                            monthName = "August"
                        break;
                        case 8:
                            monthName = "September"
                        break;
                        case 9:
                            monthName = "October"
                        break;
                        case 10:
                            monthName = "November"
                        break;
                        case 11:
                            monthName = "December"
                        break;
                    }
                    tempAppointment.monthName = monthName;

                    var dayName = "";
                    switch(dayNum)
                    {
                        case 0:
                            dayName = "Sunday";
                        break;
                        case 1:
                            dayName = "Monday";
                        break;
                        case 2:
                            dayName = "Tuesday";
                        break;
                        case 3:
                            dayName = "Wednesday";
                        break;
                        case 4:
                            dayName = "Thursday";
                        break;
                        case 5:
                            dayName = "Friday";
                        break;
                        case 6:
                            dayName = "Saturday";
                        break;
                    }
                    tempAppointment.dayName = dayName;

                    var dateNumSuffix = "";
                    if(dateNum == 1 || dateNum == 21 || dateNum == 31)
                    {
                        dateNumSuffix = "st";
                    } else if(dateNum == 2 || dateNum == 22)
                    {
                        dateNumSuffix = "nd";
                    } else if(dateNum == 3 || dateNum == 23)
                    {
                        dateNumSuffix = "rd";
                    } else dateNumSuffix = "th";
                    tempAppointment.dateNum = dateNum;
                    tempAppointment.dateNumSuffix = dateNumSuffix;


                    if (tempAppointment.comment != null) {
                        tempAppointment.comment = "\""+data[something].comment+"\"";
                    }else{
                        tempAppointment.comment = "Pending";
                    }

                    if (data[something].appointmentStatus === "APPROVED") {
                        tempAppointment.myColor = "success";
                        tempAppointment.appStatus = "Approved";
                    } else
                    if (data[something].appointmentStatus === "REJECTED") {
                        tempAppointment.myColor = "danger";
                        tempAppointment.appStatus = "Declined";
                    } else
                    if (data[something].appointmentStatus === "NEW") {
                        tempAppointment.myColor = "warning";
                        tempAppointment.appStatus = "Pending";
                    } else
                    if (data[something].appointmentStatus === "COMPLETED") {
                        tempAppointment.myColor = "info";
                        tempAppointment.appStatus = "Completed";
                    }

                    for (var link in data[something].links) {
                        if (data[something].links[link].rel === "stylist") {
                            tempAppointment.stylist = data[something].links[link].href;
                        }
                    }
                    if (data[something].appointmentStatus !== "CANCELED") {
                        scope.appointments.push(tempAppointment);
                    }
                }
                console.log(scope.appointments);
            });
        },
        controller: function($scope, $http) {

        }
    };
});


scheduleDirectives.directive('stylistname', function($http) {
    return {
        restrict: 'E',
        scope: {
            stylisturl: '='
        },
        replace: true,
        template: '<span>{{stylist.firstName}}</span>',
        link: function(scope, elm, attr) {
            var config = {headers:  {
                    'authToken': readCookie("myAccessToken"),
                    'Content-Type': 'application/json',
                    'Cache-Control': 'no-cache',
                    // 'debug': 'asd'
                }
            };
            $http.get(scope.stylisturl, config).success(function(data){
                scope.stylist = data;
            });
        },
        controller: function($scope) {
        }
    };
});


//   /$$$$$$   /$$$$$$  /$$       /$$$$$$$$ /$$   /$$ /$$$$$$$   /$$$$$$  /$$$$$$$
//  /$$__  $$ /$$__  $$| $$      | $$_____/| $$$ | $$| $$__  $$ /$$__  $$| $$__  $$
// | $$  \__/| $$  \ $$| $$      | $$      | $$$$| $$| $$  \ $$| $$  \ $$| $$  \ $$
// | $$      | $$$$$$$$| $$      | $$$$$   | $$ $$ $$| $$  | $$| $$$$$$$$| $$$$$$$/
// | $$      | $$__  $$| $$      | $$__/   | $$  $$$$| $$  | $$| $$__  $$| $$__  $$
// | $$    $$| $$  | $$| $$      | $$      | $$\  $$$| $$  | $$| $$  | $$| $$  \ $$
// |  $$$$$$/| $$  | $$| $$$$$$$$| $$$$$$$$| $$ \  $$| $$$$$$$/| $$  | $$| $$  | $$
//  \______/ |__/  |__/|________/|________/|__/  \__/|_______/ |__/  |__/|__/  |__/

// scheduleDirectives.directive('calendar', function() {
//     return {
//         restrict: 'E',
//         template: '',
//         scope: {
//             clickable: '='
//         },
//         link: function() {

//         },
//         controller: function($scope) {
//             var monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
//             var dayNames = ["sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"];
//             $scope.calendar = function(year, monthIndex, day) {
//                 if (year === undefined &&
//                     month === undefined &&
//                     day === undefined) {
//                     var d = new Date();
//                 year = d.getFullYear();
//                 monthIndex = d.getMonth();
//                 day = d.getDate();
//             }

//             var date = new Date(year, monthIndex, 1);
//             var current = new Date(year, monthIndex, day);
//             var month = {};
//             var weeks = [];

//             month.name = monthNames[monthIndex];
//             month.year = date.getFullYear();
//             month.number = date.getMonth();

//                 // loop for weeks
//                 for (var j=0 ; date.getMonth() === monthIndex ; j++) {
//                     //loop for days
//                     var weekdays = [];
//                     for (var k=0 ; date.getMonth() === monthIndex ; k++){
//                         // console.log(scope.date);

//                         if (date.getDay() === 0 && k !== 0) {
//                             break;
//                         }
//                         // loop until you see the day go to 0 then jump out OR the month changes
//                         var dayNumber = date.getDate();
//                         var dayName   = dayNames[date.getDay()];
//                         var dayClass  = "";
//                         if (date.getTime() < current.getTime()) {
//                             dayClass = "inactive";
//                         }
//                         if (date.getDate()  === current.getDate() &&
//                             date.getMonth() === current.getMonth() &&
//                             date.getYear()  === current.getYear()) {
//                             dayClass = "today";
//                     }
//                     weekdays.push({number: dayNumber, name: dayName, class:dayClass});

//                         // increment the date
//                         date.setDate(date.getDate()+1);
//                     }
//                     weeks.push(weekdays);
//                 }
//                 // month.weeks = weeks;
//                 // console.log(month);
//                 month.weeks = weeks;
//                 $scope.month = month;
//             };
//             var d = new Date();
//             $scope.calendar(d.getFullYear(), d.getMonth(), d.getDate());
//             $scope.dayNow = d.getDate();
//             $scope.monthNow = {"num": d.getMonth(), "name":monthNames[d.getMonth()]};
//             $scope.yearNow = d.getFullYear();
//             $scope.monthNames = monthNames;
//         }
//     };
// });

// scheduleDirectives.directive('modal', function() {
//     return {
//         restrict: 'E',
//         template:   '<div class="modal fade">'+
//                         '<div class="modal-dialog">'+
//                             '<div class="modal-content">'+
//                                 '<div class="modal-header">'+
//                                     '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>'+
//                                     '<h4 class="modal-title">Modal title</h4>'+
//                                 '</div>'+
//                                 '<div class="modal-body">'+
//                                     '<p>One fine body&hellip;</p>'+
//                                 '</div>'+
//                                 '<div class="modal-footer">'+
//                                     '<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>'+
//                                     '<button type="button" class="btn btn-primary">Save changes</button>'+
//                                 '</div>'+
//                             '</div><!-- /.modal-content -->'+
//                         '</div><!-- /.modal-dialog -->'+
//                     '</div><!-- /.modal -->',
//         scope: {
//             availability: '=',
//             day: '='
//         },
//         controller: function($scope) {

//         }
//     };
// });





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

scheduleDirectives.directive('appointmentgetterStaff', function($http) {
    return {
        restrict: 'A',
        template: '<div ng-repeat="appointment in appointments">'+
            '<div class="alert alert-{{appointment.myColor}} " ng-click="calendar(appointment.startTime.getFullYear(),appointment.startTime.getMonth(),appointment.startTime.getDate())" >Appointment with <client clienturl="appointment.client"/> at {{appointment.startTime.toLocaleTimeString()}}<br/>'+
                '<ng-form ng-controller="acceptAppointmentsController">'+
                '<input ng-disabled="appointment.active" class="form-control" id="disabledInput" type="hidden" placeholder="{{appointment.ID}}" ng-model="appointment.ID"><br/>'+
                '<input ng-disabled="appointment.active" class="form-control" id="disabledInput" type="text" placeholder="{{appointment.comment}}" ng-model="appointment.comment"><br/>'+
                '<button ng-disabled="appointment.active" type="button" class="btn btn-danger" ng-click="denyAppointment()">Deny</button>'+
                '<button ng-disabled="appointment.active" type="button" class="btn btn-success" ng-click="acceptAppointment()">Accept</button>'+
                '</ng-form>'+
            '</div>'+
        '</div>',
        link: function(scope, elm, attr) {
            var config = { headers:  {
                    'authToken': readCookie("myAccessToken"),
                    'Content-Type': 'application/json',
                    'Cache-Control': 'no-cache',
                    // 'debug': 'asd'
                }
            };
            $http.get('http://home.joubin.me/salon-scheduler-api/appointments', config).success(function(data) {
                scope.appointments = [];

                for (var something in data){
                    console.log(data[something])
                    var tempAppointment = {};
                    var date = new Date(data[something].startTime);
                    tempAppointment.startTime = date;
                    tempAppointment.appointmentStatus = data[something].appointmentStatus;
                    tempAppointment.active = true;
                    tempAppointment.comment = data[something].comment;
                    tempAppointment.ID = data[something].id;

                    if (data[something].appointmentStatus === "APPROVED") {
                        tempAppointment.myColor = "success";

                    }
                    if (data[something].appointmentStatus === "REJECTED" || data[something].appointmentStatus === "CANCELED") {
                        tempAppointment.myColor = "danger";
                    }
                    if (data[something].appointmentStatus === "NEW") {
                        tempAppointment.myColor = "warning";
                        tempAppointment.active = false;
                    }
                    if (data[something].appointmentStatus === "COMPLETED") {
                        tempAppointment.myColor = "info";
                    }
                    for (var link in data[something].links) {
                        if (data[something].links[link].rel === "client") {
                            tempAppointment.client = data[something].links[link].href;
                        }
                    }
                    scope.appointments.push(tempAppointment);
                }
                console.log(scope.appointments);
            });
        },
        controller: function($scope, $http) {

        }
    };
});


scheduleDirectives.directive('client', function($http) {
    return {
        restrict: 'E',
        scope: {
            clienturl: '='
        },
        replace: true,
        template: '<span>{{client.firstName}} {{client.lastName}}</span>',
        link: function(scope, elm, attr) {
            var config = {headers:  {
                    'authToken': readCookie("myAccessToken"),
                    'Content-Type': 'application/json',
                    'Cache-Control': 'no-cache',
                    // 'debug': 'asd'
                }
            };
            $http.get(scope.clienturl, config).success(function(data){

                scope.client = data;
            });
        },
        controller: function($scope) {
        }
    };
});
