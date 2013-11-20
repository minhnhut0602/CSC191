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