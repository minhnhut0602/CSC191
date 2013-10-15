var scheduleDirectives = angular.module('scheduleDirectives', []);

scheduleDirectives.directive('initCalendar', function() {
    return function(scope, element, attrs) {
        var visiblePopover;

        // enable popovers - all of mine in this instance have a class of .hov
        $('body').popover({selector: '.day:not(.inactive)', html: true, placement:'bottom', container:'.wrapper', content: function() {
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
                            '<label class="control-label">Start</label>' +
                            '<div class="appointment-start-time">' +
                                '<input type="text" class="form-control" id="start-time-hour" placeholder="Hour">' +
                            '</div>' +
                            '<div class="appointment-start-time-colon">:</div>' +
                            '<div class="appointment-start-time">' +
                                '<input type="text" class="form-control" id="start-time-minute" placeholder="Minute">' +
                            '</div>' +
                        '</div>' +
                        '<div class="form-group">' +
                            '<div class="submit-button">' +
                                '<button type="submit" class="btn btn-primary">Done</button>' +
                            '</div>' +
                        '</div>' +
                    '</form>';
        }});
        
        // only allow 1 popover at a time
        // all my popovers hav
        $('body').on('click', '.day:not(.inactive)', function(e) {
            // don't fall through
            e.stopPropagation();
            //$('.day:not(.inactive)').not(this).popover('hide');
        });

        $('.selectpicker').selectpicker({container: 'body'});

        $('body').on('shown.bs.popover', '.day:not(.inactive)', function () {
            $('.selectpicker-popover').selectpicker();
        });
    };
});