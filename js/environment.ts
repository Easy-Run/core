// environment.ts
import runCoreJar from "./javaBridge";

export function initialize(): Promise<void> {
  return new Promise((resolve, reject) => {
    runCoreJar(['initialize'], (err: any, stdout: string, stderr: string) => {
      if (err) {
        reject(err);
      } else {
        resolve();
      }
    });
  });
}