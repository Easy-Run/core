// environment.ts
import runCoreJar from "./javaBridge.js";

const database = {
  initialize: (
    successCallback: (result: any) => void,
    errorCallback: (err: any) => void
  ): void => {
    runCoreJar(['com.easyrun.database.DatabaseInitializer', 'initialize'], successCallback, errorCallback);
  },
  executeRequest: (
    sql: string,
    params: Array<string | number | null> | object,
    successCallback: (result: any) => void,
    errorCallback: (err: any) => void
  ): void => {
    runCoreJar(['com.easyrun.database.DatabaseInitializer', 'executeRequest', [sql, params]], successCallback, errorCallback);
  },
  config: {
    getConfigList: (
      successCallback: (result: any) => void,
      errorCallback: (err: any) => void
    ): void => {
      runCoreJar(['com.easyrun.database.config.controller.ConfigParamController', 'getConfigList'], successCallback, errorCallback);
    },
    updateConfigValue: (
      successCallback: (result: any) => void,
      errorCallback: (err: any) => void,
      args: Object
    ): void => {
      runCoreJar(['com.easyrun.database.config.controller.ConfigParamController', 'updateConfigValue', [args]], successCallback, errorCallback);
    },
  },
  application: {
    getApplicationList: (
      successCallback: (result: any) => void,
      errorCallback: (err: any) => void
    ): void => {
      runCoreJar(['com.easyrun.database.application.controller.ApplicationController', 'getApplicationList'], successCallback, errorCallback);
    },
    getDefaultApplication: (
      successCallback: (result: any) => void,
      errorCallback: (err: any) => void
    ): void => {
      runCoreJar(['com.easyrun.database.application.controller.ApplicationController', 'getDefaultApplication'], successCallback, errorCallback);
    },
    getApplicationById: (
      successCallback: (result: any) => void,
      errorCallback: (err: any) => void,
      args: number
    ): void => {
      runCoreJar(['com.easyrun.database.application.controller.ApplicationController', 'getApplicationById', [args]], successCallback, errorCallback);
    },
    createApplication: (
      successCallback: (result: any) => void,
      errorCallback: (err: any) => void,
      args: Object
    ): void => {
      runCoreJar(['com.easyrun.database.application.controller.ApplicationController', 'createApplication', [args]], successCallback, errorCallback);
    },
    updateApplication: (
      successCallback: (result: any) => void,
      errorCallback: (err: any) => void,
      args: Object
    ): void => {
      runCoreJar(['com.easyrun.database.application.controller.ApplicationController', 'updateApplication', [args]], successCallback, errorCallback);
    },
    deleteApplication: (
      successCallback: (result: any) => void,
      errorCallback: (err: any) => void,
      args: number
    ): void => {
      runCoreJar(['com.easyrun.database.application.controller.ApplicationController', 'deleteApplication', [args]], successCallback, errorCallback);
    }
  }
}

export default database