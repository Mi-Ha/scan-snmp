angular.module('scannerSNMP')
    .controller('scanCtrl', function (scannerService, $scope, $timeout) {

        var emptyScope = $scope;

        $scope.main = {};

        $scope.main = {
            showMainView: undefined,
            settingsScope: undefined,
            widgetScope: undefined,
            mainScope: undefined
        }

        $scope.counter = 0;

        $scope.devices = [];  // "AAA", "BBB", "CCC", "DDD"

        $scope.init=function(){
            updateData();
        }

        $scope.getListDevices = function () {
            scannerService.getAllDevices().then(function (dataResponse) {

                //if (dataResponse.data.messages[0].code.localeCompare('OK') == true) {
                if (dataResponse.data.success == true) {
                    $scope.devices = JSON.parse(dataResponse.data.data);
                }
            });
        }

        var updateData = function() {
            $scope.counter++;
            $scope.getListDevices();
            $timeout(updateData, 5000);
        };




    });

