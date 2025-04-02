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
    },
    launchApp: (
      successCallback: (result: any) => void,
      errorCallback: (err: any) => void,
      packageName: string,
      deviceSerial: string
    ): void => {
      runCoreJar(['com.easyrun.module.adb.AdbManager', 'launchApp', [packageName, deviceSerial]], successCallback, errorCallback);
    },
    uninstallApp: (
      successCallback: (result: any) => void,
      errorCallback: (err: any) => void,
      packageName: string,
      deviceSerial: string
    ): void => {
      runCoreJar(['com.easyrun.module.adb.AdbManager', 'uninstallApp', [packageName, deviceSerial]], successCallback, errorCallback);
    },
    clearApp: (
      successCallback: (result: any) => void,
      errorCallback: (err: any) => void,
      packageName: string,
      deviceSerial: string
    ): void => {
      runCoreJar(['com.easyrun.module.adb.AdbManager', 'clearApp', [packageName, deviceSerial]], successCallback, errorCallback);
    },
    runCordova: (
      successCallback: (result: any) => void,
      errorCallback: (err: any) => void,
      progressCallback: (progress: Object) => void,
      deviceSerial: string,
      idapplication: number | null
    ): void => {
      runCoreJar(['com.easyrun.module.adb.AdbManager', 'runCordova', [deviceSerial, idapplication]], successCallback, errorCallback, progressCallback);
    },
  }
}

export default module