import * as path from 'path';
import { execFile } from 'child_process';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const javaExecutable = path.join(__dirname, '..', '..', 'lib', 'jre', 'jre17.0.12', 'bin', 'java');

function runCoreJar(
  args: any,
  successCallback: (result: any) => void,
  errorCallback: (err: any) => void
) {
  // Pour chaque argument, si c'est un objet, on le convertit avec JSON.stringify (de manière récursive)
  const processedArgs = args.map((arg: any) =>
    typeof arg === 'object' ? JSON.stringify(arg) : arg
  );

  const jarPath = path.join(__dirname, '..', '..', 'target', 'core-0.0.1.jar');
  // On construit le tableau d'arguments pour execFile : -jar, jarPath, puis processedArgs
  const fileArgs = ['-jar', jarPath, ...processedArgs];

  execFile(javaExecutable, fileArgs, (err: any, stdout: any, stderr: any) => {
    if (stdout && stdout.trim().length > 0) {
      let parsedOut;
      try {
        parsedOut = JSON.parse(stdout);
      } catch (e) {
        parsedOut = stdout;
      }
      successCallback(parsedOut);
      return;
    }
    let parsedErr;
    const errorString = err ? err.toString() : stderr;
    try {
      parsedErr = JSON.parse(errorString);
    } catch (e) {
      parsedErr = errorString;
    }
    errorCallback(parsedErr);
  });
}

export default runCoreJar;
