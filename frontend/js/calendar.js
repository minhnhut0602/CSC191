scheduleDirectives.direcive('calendar', function($http) {
	return {
		restrict: 'E',
		template: 	'<div class="week" ng-repeat="week in month.weeks" ng-init="dayInfo = true">'+
				        '<div calendar-popover ng-repeat="day in week" class="day {{day.name}} {{day.class}}" data-toggle="popover" title="" data-year="{{month.year}}" data-month="{{month.number}}" data-day="{{day.number}}" data-original-title="Create Appointment">'+
				            '<strong>{{day.number}}</strong>'+
				        '</div>'+
				        '<div class="day-info" ng-hide="dayInfo"></div>'+
				    '</div>',
		scope: {
			day: '='
		},
		controller: function($scope, $modal, $http) {
			var day = {};
			day.year = 2013;
			day.month = 11;
			day.day = 1;
			day.name = "Tuesday";
			day.stylists = ["staff 1", "staff 2", "staff 3"];
			$scope.day = day;

			$scope.open = function() {
				var modalInstance = $modal.open({
					template: 'myModalContent.html',
					controller: MocalInstanceController,
					resolve: {
						day: function() {
							return $scope.day;
						}
					}
				});
				modalInstance.result.then(function(appointment) {
					$scope.newAppointment = appointment;
					console.log($scope.newAppointment);
				});
			};
		},
		link: function(scope, elm, attr) {
			console.log(scope.day)
		}
	}
});
// scheduleControllers.controller('ModalInstanceController', [], function($scope, $modalInstance, day) {
// 	$scope.day = day;

// 	$scope.ok = function() {
// 		$modalInstance.close($scope.day);
// 	};
// 	$scope.cancel = function() {
// 		$modalInstance.dismiss('cancel');
// 	};
// })
scheduleControllers.controller('CalendarGenerator', function CalendarGenerator($scope, $modal) {
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

    var tempday = {};
    tempday.year = 2013;
    tempday.month = 11;
    tempday.day = 1;
    tempday.name = "Tuesday";
    tempday.stylists = ["staff 1", "staff 2", "staff 3"];
    $scope.day = tempday;

    $scope.open = function(tempDay) {
        var modalInstance = $modal.open({
            template:   '<div class="modal">'+
                            '<div class="modal-dialog">'+
                                '<div class="modal-content">'+
                                    '<div class="modal-header">'+
                                        '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>'+
                                        '<h4 class="modal-title">Modal title</h4>'+
                                    '</div>'+
                                    '<div class="modal-body">'+
                                        '<ul>'+
                                            '<li ng-repeat="stylist in day.stylists">'+
                                                '<a>{{stylist}}</a>'+
                                            '</li>'+
                                        '</ul>'+
                                    '</div>'+
                                    '<div class="modal-footer">'+
                                        '<button class="btn btn-primary" ng-click="ok()">OK</button>'+
                                        '<button class="btn btn-warning" ng-click="cancel()">Cancel</button>'+
                                    '</div>'+
                                '</div><!-- /.modal-content -->'+
                            '</div><!-- /.modal-dialog -->'+
                        '</div><!-- /.modal -->',
            controller: CalendarModalInstance,
            resolve: {
                day: function() {
                    return tempDay;
                }
            }
        });
        modalInstance.result.then(function(appointment) {
            $scope.newAppointment = appointment;
            console.log($scope.newAppointment);
        });
    };
});
scheduleControllers.controller('CalendarModalInstance', ['$scope', '$modalInstance', 'day'], function($scope, $modalInstance, day){
    $scope.day = day;

    $scope.ok = function() {
        $modalInstance.close($scope.day);
    };
    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };
})