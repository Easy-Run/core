// environment.ts
import runCoreJar from "./javaBridge.js";
const module = {
    adb: {
        getPdaList: (successCallback, errorCallback) => {
            runCoreJar(['com.easyrun.module.adb.AdbManager', 'getPdaList'], successCallback, errorCallback);
        },
        launchScrcpy: (successCallback, errorCallback, args) => {
            runCoreJar(['com.easyrun.module.adb.AdbManager', 'launchScrcpy', [args]], successCallback, errorCallback);
        },
        launchApp: (successCallback, errorCallback, packageName, deviceSerial) => {
            runCoreJar(['com.easyrun.module.adb.AdbManager', 'launchApp', [packageName, deviceSerial]], successCallback, errorCallback);
        },
        uninstallApp: (successCallback, errorCallback, packageName, deviceSerial) => {
            runCoreJar(['com.easyrun.module.adb.AdbManager', 'uninstallApp', [packageName, deviceSerial]], successCallback, errorCallback);
        },
        clearApp: (successCallback, errorCallback, packageName, deviceSerial) => {
            runCoreJar(['com.easyrun.module.adb.AdbManager', 'clearApp', [packageName, deviceSerial]], successCallback, errorCallback);
        },
        runCordova: (successCallback, errorCallback, progressCallback, deviceSerial, idapplication) => {
            runCoreJar(['com.easyrun.module.adb.AdbManager', 'runCordova', [deviceSerial, idapplication]], successCallback, errorCallback, progressCallback);
        },
    }
};
export default module;
