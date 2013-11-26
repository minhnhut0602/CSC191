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
            $('.day:not(.inactive)').click(function(e) {
                $('.day:not(.inactive)').not($(this)).popover('hide');
                console.log("day event click");
                e.stopPropagation();
            });
            $('body').on('click', '.popover', function(e) {
                // $('.day:not(.inactive)').not($(this)).popover('hide');
                // e.stopPropagation();
            });
            $(document).on('click', '.selectpicker-popover', function(e) {
                // $('.day:not(.inactive)').not($(this)).popover('hide');
                console.log(".select-popover event click");
                e.stopPropagation();
            });
            $('body').click(function() {
               $('.day:not(.inactive)').popover('hide');
               console.log("body click event");
            });
        }
    }
});

scheduleDirectives.directive('selectpicker', function() {
    return {
        restrict: 'A',
        template:   '<select class="selectpicker" multiple data-live-search="true" data-width="40%" >'+
                        '<option ng-repeat="staff in allStaff" >{{staff.name}}</option>'+
                    '</select>',
        controller: function($scope, $http) {
            $http.get('json/stylists.json').success(function(data) {
                $scope.allStaff = data;
            });
        },
        link: function(scope, elm, attr) {
            scope.$watch('allStaff', function(newValue, oldValue, scope) {
                console.log(scope.allStaff);
                console.log(" staff changed");
                elm.selectpicker({container: 'body'});
            }), true;
        }
    }
});
scheduleDirectives.directive('clienttakeoff', function() {
    return {
        restrict: 'A',
        template:   '<div ng-repeat="appointment in appointments">'+
                        '<div class="alert alert-{{appointment.myColor}} " ng-click="calendar(appointment.startTime.getFullYear(),appointment.startTime.getMonth(),appointment.startTime.getDate())" >You have an appointment with <span cockeyes stylisturl="{{appointment.stylist}}"></span> at {{appointment.startTime.toLocaleTimeString() }}   {{appointment.myColor}}' +
                        '</div>'+
                    '</div>',
        controller: function($scope, $http) {
            var config = {headers:  {
                    'authToken': readCookie("myAccessToken"),
                    'Content-Type': 'application/json',
                    'Cache-Control': 'no-cache',
                    // 'debug': 'asd'
                }
            };
            $http.get('http://home.joubin.me/salon-scheduler-api/appointments', config).success(function(data) {
                $scope.appointments = [];

                for (var something in data){
                    var tempAppointment = {};
                    var date = new Date(data[something].startTime);
                    tempAppointment.startTime = date;
                    tempAppointment.appointmentStatus = data[something].appointmentStatus;

                    if (data[something].appointmentStatus === "APPROVED") {
                        tempAppointment.myColor = "success";
                    }
                    if (data[something].appointmentStatus === "REJECTED" || data[something].appointmentStatus === "CANCELED") {
                        tempAppointment.myColor = "danger";
                    }
                    if (data[something].appointmentStatus === "NEW") {
                        tempAppointment.myColor = "warning";
                    }
                    if (data[something].appointmentStatus === "COMPLETED") {
                        tempAppointment.myColor = "info";
                    }
                    for (var link in data[something].links) {
                        if (data[something].links[link].rel === "stylist") {
                            console.log(data[something].links[link].href);
                            tempAppointment.stylist = data[something].links[link].href;
                        }
                    }
                    $scope.appointments.push(tempAppointment);
                }
                console.log($scope.appointments);
            });
        }
    };
});


scheduleDirectives.directive('cockeyes', function() {
    return {
        restrict: 'A',
        scope: {
            stylisturl: '@'
        },
        template: '{{stylist.firstname}}',
        controller: function($scope, $http) {
            var config = {headers:  {
                    'authToken': readCookie("myAccessToken"),
                    'Content-Type': 'application/json',
                    'Cache-Control': 'no-cache',
                    // 'debug': 'asd'
                }
            };
            console.log($scope);
            console.log($scope.stylisturl);
            $http.get($scope.stylisturl, config).success(function(data){
                $scope.stlyist = data;
            });
        }
    };
});