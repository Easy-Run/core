// environment.ts
import runCoreJar from "./javaBridge.js";
const className = 'com.easyrun.environment.EnvironmentInitializer';
const environment = {
    initialize: (successCallback, errorCallback) => {
        runCoreJar([className, 'initialize'], successCallback, errorCallback);
    }
};
export default environment;
