// database.ts
import runCoreJar from "./javaBridge.js";

const database = {
  initialize: (
    successCallback: (result: any) => void,
    errorCallback: (err: any) => void,
    ctxt?: { appId?: string; profile?: string; baseDir?: string }
  ): void => {
    runCoreJar(['com.easyrun.database.DatabaseInitializer', 'initialize', JSON.stringify([ctxt ?? {}])], successCallback, errorCallback);
  },

  executeRequest: (
    sql: string,
    params: Array<string | number | null> | object,
    successCallback: (result: any) => void,
    errorCallback: (err: any) => void,
    ctxt?: { appId?: string; profile?: string; baseDir?: string }
  ): void => {
    runCoreJar(['com.easyrun.database.DatabaseInitializer', 'executeRequest', JSON.stringify([ctxt ?? {}, { sql, params }])], successCallback, errorCallback);
  },

  config: {
    getConfigList: (
      successCallback: (result: any) => void,
      errorCallback: (err: any) => void,
      ctxt?: { appId?: string; profile?: string; baseDir?: string }
    ): void => {
      runCoreJar(['com.easyrun.database.config.controller.ConfigParamController', 'getConfigList', JSON.stringify([ctxt ?? {}])], successCallback, errorCallback);
    },

    updateConfigValue: (
      successCallback: (result: any) => void,
      errorCallback: (err: any) => void,
      args: object,
      ctxt?: { appId?: string; profile?: string; baseDir?: string }
    ): void => {
      runCoreJar(['com.easyrun.database.config.controller.ConfigParamController', 'updateConfigValue', JSON.stringify([ctxt ?? {}, args])], successCallback, errorCallback);
    },
  },

  application: {
    getApplicationList: (
      successCallback: (result: any) => void,
      errorCallback: (err: any) => void,
      ctxt?: { appId?: string; profile?: string; baseDir?: string }
    ): void => {
      runCoreJar(['com.easyrun.database.application.controller.ApplicationController', 'getApplicationList', JSON.stringify([ctxt ?? {}])], successCallback, errorCallback);
    },

    getDefaultApplication: (
      successCallback: (result: any) => void,
      errorCallback: (err: any) => void,
      ctxt?: { appId?: string; profile?: string; baseDir?: string }
    ): void => {
      runCoreJar(['com.easyrun.database.application.controller.ApplicationController', 'getDefaultApplication', JSON.stringify([ctxt ?? {}])], successCallback, errorCallback);
    },

    getApplicationById: (
      successCallback: (result: any) => void,
      errorCallback: (err: any) => void,
      id: number,
      ctxt?: { appId?: string; profile?: string; baseDir?: string }
    ): void => {
      runCoreJar(['com.easyrun.database.application.controller.ApplicationController', 'getApplicationById', JSON.stringify([ctxt ?? {}, id])], successCallback, errorCallback);
    },

    createApplication: (
      successCallback: (result: any) => void,
      errorCallback: (err: any) => void,
      data: object,
      ctxt?: { appId?: string; profile?: string; baseDir?: string }
    ): void => {
      runCoreJar(['com.easyrun.database.application.controller.ApplicationController', 'createApplication', JSON.stringify([ctxt ?? {}, data])], successCallback, errorCallback);
    },

    updateApplication: (
      successCallback: (result: any) => void,
      errorCallback: (err: any) => void,
      data: object,
      ctxt?: { appId?: string; profile?: string; baseDir?: string }
    ): void => {
      runCoreJar(['com.easyrun.database.application.controller.ApplicationController', 'updateApplication', JSON.stringify([ctxt ?? {}, data])], successCallback, errorCallback);
    },

    deleteApplication: (
      successCallback: (result: any) => void,
      errorCallback: (err: any) => void,
      id: number,
      ctxt?: { appId?: string; profile?: string; baseDir?: string }
    ): void => {
      runCoreJar(['com.easyrun.database.application.controller.ApplicationController', 'deleteApplication', JSON.stringify([ctxt ?? {}, id])], successCallback, errorCallback);
    },
  },
};

export default database;