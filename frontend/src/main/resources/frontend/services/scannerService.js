
angular.module('scannerSNMP')
    .service('scannerService', function ($http) {

        this.getAllDevices = function () {
            return $http({
                method: 'GET',
                url: 'api/scanner/getAllDevices/'
            })
        };

    });