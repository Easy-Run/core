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
        }
    }
};
export default database;
