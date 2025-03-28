import * as path from 'path';
import { exec } from 'child_process';

const javaExecutable = path.join(__dirname, '..', 'lib', 'jre', 'jre17.0.12', 'bin', 'java');

function runCoreJar(args: string[], callback: (err: any, stdout: string, stderr: string) => void) {
  const jarPath = path.join(__dirname, '..', 'target', 'core-0.0.1.jar');
  const cmd = `"${javaExecutable}" -jar "${jarPath}" ${args.join(' ')}`;
  exec(cmd, callback);
}

export default runCoreJar