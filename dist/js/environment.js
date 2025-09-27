// environment.ts
import runCoreJar from "./javaBridge.js";
const className = 'com.easyrun.environment.EnvironmentInitializer';
const environment = {
    initialize: (successCallback, errorCallback, ctxt) => {
        runCoreJar([className, 'initialize', JSON.stringify([ctxt !== null && ctxt !== void 0 ? ctxt : {}])], successCallback, errorCallback);
    }
};
export default environment;
