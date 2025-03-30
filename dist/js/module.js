// environment.ts
import runCoreJar from "./javaBridge.js";
const module = {
    adb: {
        getPdaList: (successCallback, errorCallback) => {
            runCoreJar(['com.easyrun.module.adb.AdbManager', 'getPdaList'], successCallback, errorCallback);
        }
    }
};
export default module;
