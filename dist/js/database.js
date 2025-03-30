// environment.ts
import runCoreJar from "./javaBridge.js";
const database = {
    initialize: (successCallback, errorCallback) => {
        runCoreJar(['com.easyrun.database.DatabaseInitializer', 'initialize'], successCallback, errorCallback);
    },
    executeRequest: (sql, params, successCallback, errorCallback) => {
        runCoreJar(['com.easyrun.database.DatabaseInitializer', 'executeRequest', [sql, params]], successCallback, errorCallback);
    },
    config: {
        getConfigList: (successCallback, errorCallback) => {
            runCoreJar(['com.easyrun.database.config.controller.ConfigParamController', 'getConfigList'], successCallback, errorCallback);
        },
        updateConfigValue: (successCallback, errorCallback, args) => {
            runCoreJar(['com.easyrun.database.config.controller.ConfigParamController', 'updateConfigValue', [args]], successCallback, errorCallback);
        },
    },
    application: {
        getApplicationList: (successCallback, errorCallback) => {
            runCoreJar(['com.easyrun.database.application.controller.ApplicationController', 'getApplicationList'], successCallback, errorCallback);
        },
        getDefaultApplication: (successCallback, errorCallback) => {
            runCoreJar(['com.easyrun.database.application.controller.ApplicationController', 'getDefaultApplication'], successCallback, errorCallback);
        },
        getApplicationById: (successCallback, errorCallback, args) => {
            runCoreJar(['com.easyrun.database.application.controller.ApplicationController', 'getApplicationById', [args]], successCallback, errorCallback);
        },
        createApplication: (successCallback, errorCallback, args) => {
            runCoreJar(['com.easyrun.database.application.controller.ApplicationController', 'createApplication', [args]], successCallback, errorCallback);
        },
        updateApplication: (successCallback, errorCallback, args) => {
            runCoreJar(['com.easyrun.database.application.controller.ApplicationController', 'updateApplication', [args]], successCallback, errorCallback);
        },
        deleteApplication: (successCallback, errorCallback, args) => {
            runCoreJar(['com.easyrun.database.application.controller.ApplicationController', 'deleteApplication', [args]], successCallback, errorCallback);
        }
    }
};
export default database;
