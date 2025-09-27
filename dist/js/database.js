// database.ts
import runCoreJar from "./javaBridge.js";
const database = {
    initialize: (successCallback, errorCallback, ctxt) => {
        runCoreJar(['com.easyrun.database.DatabaseInitializer', 'initialize', JSON.stringify([ctxt !== null && ctxt !== void 0 ? ctxt : {}])], successCallback, errorCallback);
    },
    executeRequest: (sql, params, successCallback, errorCallback, ctxt) => {
        runCoreJar(['com.easyrun.database.DatabaseInitializer', 'executeRequest', JSON.stringify([ctxt !== null && ctxt !== void 0 ? ctxt : {}, { sql, params }])], successCallback, errorCallback);
    },
    config: {
        getConfigList: (successCallback, errorCallback, ctxt) => {
            runCoreJar(['com.easyrun.database.config.controller.ConfigParamController', 'getConfigList', JSON.stringify([ctxt !== null && ctxt !== void 0 ? ctxt : {}])], successCallback, errorCallback);
        },
        updateConfigValue: (successCallback, errorCallback, args, ctxt) => {
            runCoreJar(['com.easyrun.database.config.controller.ConfigParamController', 'updateConfigValue', JSON.stringify([ctxt !== null && ctxt !== void 0 ? ctxt : {}, args])], successCallback, errorCallback);
        },
    },
    application: {
        getApplicationList: (successCallback, errorCallback, ctxt) => {
            runCoreJar(['com.easyrun.database.application.controller.ApplicationController', 'getApplicationList', JSON.stringify([ctxt !== null && ctxt !== void 0 ? ctxt : {}])], successCallback, errorCallback);
        },
        getDefaultApplication: (successCallback, errorCallback, ctxt) => {
            runCoreJar(['com.easyrun.database.application.controller.ApplicationController', 'getDefaultApplication', JSON.stringify([ctxt !== null && ctxt !== void 0 ? ctxt : {}])], successCallback, errorCallback);
        },
        getApplicationById: (successCallback, errorCallback, id, ctxt) => {
            runCoreJar(['com.easyrun.database.application.controller.ApplicationController', 'getApplicationById', JSON.stringify([ctxt !== null && ctxt !== void 0 ? ctxt : {}, id])], successCallback, errorCallback);
        },
        createApplication: (successCallback, errorCallback, data, ctxt) => {
            runCoreJar(['com.easyrun.database.application.controller.ApplicationController', 'createApplication', JSON.stringify([ctxt !== null && ctxt !== void 0 ? ctxt : {}, data])], successCallback, errorCallback);
        },
        updateApplication: (successCallback, errorCallback, data, ctxt) => {
            runCoreJar(['com.easyrun.database.application.controller.ApplicationController', 'updateApplication', JSON.stringify([ctxt !== null && ctxt !== void 0 ? ctxt : {}, data])], successCallback, errorCallback);
        },
        deleteApplication: (successCallback, errorCallback, id, ctxt) => {
            runCoreJar(['com.easyrun.database.application.controller.ApplicationController', 'deleteApplication', JSON.stringify([ctxt !== null && ctxt !== void 0 ? ctxt : {}, id])], successCallback, errorCallback);
        },
    },
};
export default database;
