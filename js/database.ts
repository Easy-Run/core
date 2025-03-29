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
    }
  }
}

export default database