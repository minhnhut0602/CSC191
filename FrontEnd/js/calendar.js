angular.module('tsCalendarGenerator', ['ng'])
    .directive('tsCalendar', function () {
        return {
            restrict : 'A',
            template :  '<div class="month" ng-repeat="month in months">' +
                            '<div class="month-name"><h1>{{month.name}}</h1></div>' +
                            '<div class="week" ng-repeat="week in month.weeks">' +
                                '<div ng-repeat="day in week" class="day {{day.name}} {{day.class}}">' +
                                    '<strong>{{day.number}}</strong>' +
                                '</div>' +
                            '</div>' +
                        '</div>',
            scope : {
                dummy: '='
            },
            link : function(scope, elem, attrs) {
                // arrays that hold the names of the months and days used to convert the ints returned by the Date object
                var monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
                var dayNames = ["sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"];

                if (scope.date === undefined) {
                    scope.date = new Date();
                }

                // get current date
                var current = new Date();
                var monthIndex = scope.date.getMonth();
                scope.date.setDate(1);

                var months = [];

                //loop for 6 more months
                for (var i=0 ; i < 3 ; i++) {
                    var month = {};
                    var weeks = [];

                    month.name = monthNames[(monthIndex+i)%12];

                    // loop for weeks
                    for (var j=0 ; scope.date.getMonth() === (monthIndex+i)%12 && j<5 ; j++) {
                        //loop for days
                        var weekdays = [];
                        for (var k=0 ; scope.date.getMonth() === (monthIndex+i)%12 && k<31 ; k++){
//                            console.log(scope.date);

                            if (scope.date.getDay() === 0 && k !== 0) {
                                break;
                            }
                            //loop until you see the day go to 0 then jump out OR the month changes
                            var dayNumber = scope.date.getDate();
                            var dayName   = dayNames[scope.date.getDay()];
                            var dayClass  = "";
                            if (scope.date.getTime() < current.getTime()) {
                                dayClass = "inactive";
                            }
                            if (scope.date.getDate()  === current.getDate() &&
                                scope.date.getMonth() === current.getMonth() &&
                                scope.date.getYear()  === current.getYear()) {
                                dayClass = "today";
                            }
                            weekdays.push({number: dayNumber, name: dayName, class:dayClass});

                            // increment the date
                            scope.date.setDate(scope.date.getDate()+1);
                        }
                        weeks.push(weekdays);
                    }
                    month.weeks = weeks;
                    months.push(month);
                }
//                console.log(months);
                scope.months = months;
            }
        };
    });