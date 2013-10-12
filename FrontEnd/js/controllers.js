var scheduleControllers = angular.module('scheduleControllers', []);

scheduleControllers.controller('CalendarGenerator', function CalendarGenerator($scope) {
    $scope.calendar = function(year, monthIndex, day) {
        var monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
        var dayNames = ["sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"];
        if (year === undefined &&
            monthIndex === undefined &&
            day === undefined) {
            var setCurrentDate = new Date();
            year = setCurrentDate.getYear();
            monthIndex = setCurrentDate.getMonth();
            day = 0;
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
    // $scope.$on('$viewContentLoaded', function(){
        
        // $('body').popover({selector: '.day:not(.inactive)', html: true, placement:'bottom', container:'.wrapper', content: function() {
        //     console.log('does this work');
        //     var year = $(this).attr('data-year');
        //     var month = $(this).attr('data-month');
        //     var day = $(this).attr('data-day');
        //     var date = new Date(year, month, day);

        //     return  '<form class="new-appointment-form">' +
        //                 '<div class="clearfix">' +
        //                     '<label class="control-label appointment-type-label">Stylist</label>' +
        //                     '<div class="appointment-stylist-selection">' +
        //                         '<select class="selectpicker-popover" id="appointment-stylist" data-width="100%">' +
        //                             '<option>Alexa Johnson</option>' +
        //                             '<option>Amy Brown</option>' +
        //                             '<option>Amanda Rinkey</option>' +
        //                             '<option>Cris Rio</option>' +
        //                             '<option>Shoni Summers</option>' +
        //                         '</select>' +
        //                     '</div>' +
        //                 '</div>' +
        //                 '<div class="clearfix">' +
        //                     '<label class="control-label appointment-type-label">Type</label>' +
        //                     '<div class="appointment-type-selection">' +
        //                         '<select class="selectpicker-popover" id="appointment-type" data-width="100%">' +
        //                             '<option>Color</option>' +
        //                             '<option>Cut</option>' +
        //                             '<option>Perm</option>' +
        //                             '<option>Hilight</option>' +
        //                             '<option>Shave</option>' +
        //                         '</select>' +
        //                     '</div>' +
        //                 '</div>' +
        //                 '<div class="clearfix">' +
        //                     '<label class="control-label">Start</label>' +
        //                     '<div class="appointment-start-time">' +
        //                         '<input type="text" class="form-control" id="start-time-hour" placeholder="Hour">' +
        //                     '</div>' +
        //                     '<div class="appointment-start-time-colon">:</div>' +
        //                     '<div class="appointment-start-time">' +
        //                         '<input type="text" class="form-control" id="start-time-minute" placeholder="Minute">' +
        //                     '</div>' +
        //                 '</div>' +
        //                 '<div class="form-group">' +
        //                     '<div class="submit-button">' +
        //                         '<button type="submit" class="btn btn-primary">Done</button>' +
        //                     '</div>' +
        //                 '</div>' +
        //             '</form>';
        // }});

        // $('.selectpicker').selectpicker();

        // $('body').on('shown.bs.popover', '.day:not(.inactive)', function () {
        //     console.log('popover shown');
        //     $('.selectpicker-popover').selectpicker();
        // });

        // $('body:not(.day)').mousedown(function() {
        //     console.log('mouse down');
        //     //$('.day:not(.inactive)').popover('destroy');
        //     //$('.selectpicker-popover').selectpicker('hide');
        // });
    // });
});