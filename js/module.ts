// environment.ts
import runCoreJar from "./javaBridge.js";

const module = {
  adb: {
    getPdaList: (
      successCallback: (result: any) => void,
      errorCallback: (err: any) => void
    ): void => {
      runCoreJar(['com.easyrun.module.adb.AdbManager', 'getPdaList'], successCallback, errorCallback);
    },
    launchScrcpy: (
      successCallback: (result: any) => void,
      errorCallback: (err: any) => void,
      args: string
    ): void => {
      runCoreJar(['com.easyrun.module.adb.AdbManager', 'launchScrcpy', [args]], successCallback, errorCallback);
    }
  }
}

export default module