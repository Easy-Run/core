// environment.ts
import runCoreJar from "./javaBridge.js";

const className: string = 'com.easyrun.environment.EnvironmentInitializer'

const environment = {
  initialize: (
    successCallback: (result: any) => void,
    errorCallback: (err: any) => void
  ): void => {
    runCoreJar([className, 'initialize'], successCallback, errorCallback);
  }
};

export default environment