// environment.ts
import runCoreJar from "./javaBridge.js";

const className: string = 'com.easyrun.environment.EnvironmentInitializer'

const environment = {
  initialize: (
    successCallback: (result: any) => void,
    errorCallback: (err: any) => void,
    ctxt: { appId: string, profile: string }
  ): void => {
    runCoreJar([className, 'initialize', JSON.stringify([ctxt ?? {}])], successCallback, errorCallback);
  }
};

export default environment