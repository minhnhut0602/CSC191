var calendarModule = angular.module('calendarModule', []);


calendarModule.directive('calendar', function($http) {
	return {
		restrict: 'E',
		template: 	'<div class="week" ng-repeat="week in month.weeks" ng-init="dayInfo = true">'+
				        '<div ng-repeat="day in week" class="day {{day.name}} {{day.class}}" ng-click="open(appointmentInfo)">'+
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
					'authType': readCookie('myAccessToken')
				}
			};
			$http.get('http://home.joubin.me/salon-scheduler-api/appointmentTypes', config).success(function(data) {
				var types = [];
				for (type in data) {
					types.push({
						type: data[type].appointmentType,
						duration: data[type].durationInMinutes
					});
				}
				$scope.appointmentInfo.types = types;
			});
			$http.get('http://home.joubin.me/salon-scheduler-api/users/stylists', config).success(function(data) {
				var stylists = [];
				for (stylist in data) {
					stylists.push({
						name: data[stylist].firstName +' '+ data[stylist].lastName
					})
				}
				$scope.appointmentInfo.stylists = stylists;
			});
			$scope.appointmentInfo.times = [
				{time: "8:00am"},
				{time: "8:00am"},
				{time: "9:00am"},
				{time: "9:00am"},
				{time: "10:00am"},
				{time: "10:00am"},
				{time: "11:00am"},
				{time: "11:00am"},
				{time: "12:00pm"},
				{time: "12:00pm"},
				{time: "1:00pm"},
				{time: "1:00pm"},
				{time: "2:00pm"},
				{time: "2:00pm"},
				{time: "3:00pm"},
				{time: "3:00pm"},
				{time: "4:00pm"},
				{time: "4:00pm"},
				{time: "5:00pm"},
				{time: "5:00pm"},
				{time: "6:00pm"},
				{time: "6:00pm"},
				{time: "7:00pm"},
				{time: "7:00pm"},
				{time: "8:00pm"},
				{time: "8:00pm"},
			]

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
		    $scope.makeAppointment = function() {
		    	//TODO do things here to make the appointment
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
function CalendarModalInstance($scope, $modalInstance, day) {
    $scope.day = day;
    $scope.newAppointment = {
    	stylist: $scope.day.stylists[0],
    	type: $scope.day.types[0],
    	time: $scope.day.times[0]
    };

    $scope.ok = function() {
        $modalInstance.close($scope.newAppointment);
    };
    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };
}