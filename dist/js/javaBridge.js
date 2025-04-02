import * as path from 'path';
import { spawn } from 'child_process';
import { fileURLToPath } from 'url';
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const javaExecutable = path.join(__dirname, '..', '..', 'lib', 'jre', 'jre17.0.12', 'bin', 'java');
function runCoreJar(args, successCallback, errorCallback, progressCallback) {
    // Pour chaque argument, si c'est un objet, on le convertit avec JSON.stringify
    const processedArgs = args.map((arg) => typeof arg === 'object' ? JSON.stringify(arg) : arg);
    const jarPath = path.join(__dirname, '..', '..', 'target', 'core-0.0.1.jar');
    // On construit le tableau d'arguments pour spawn : -jar, jarPath, puis processedArgs
    const fileArgs = ['-jar', jarPath, ...processedArgs];
    const child = spawn(javaExecutable, fileArgs);
    let output = '';
    child.stdout.on('data', (data) => {
        const chunk = data.toString();
        output += chunk;
        if (progressCallback) {
            // Découper le chunk en lignes, retirer les espaces et filtrer les lignes vides
            const lines = chunk
                .split(/\r?\n/)
                .map(line => line.trim())
                .filter(line => line.length > 0);
            lines.forEach(line => {
                try {
                    const parsed = JSON.parse(line);
                    progressCallback(parsed);
                }
                catch (e) {
                    // Ignorer la ligne si elle ne se parse pas en JSON
                }
            });
        }
    });
    child.stderr.on('data', (data) => {
        const chunk = data.toString();
        output += chunk;
        if (progressCallback) {
            const lines = chunk
                .split(/\r?\n/)
                .map(line => line.trim())
                .filter(line => line.length > 0);
            lines.forEach(line => {
                try {
                    const parsed = JSON.parse(line);
                    progressCallback(parsed);
                }
                catch (e) {
                    // Ignorer la ligne si elle ne se parse pas en JSON
                }
            });
        }
    });
    child.on('close', (code) => {
        if (code === 0) {
            let parsedOut;
            try {
                parsedOut = JSON.parse(output);
            }
            catch (e) {
                parsedOut = output;
            }
            successCallback(parsedOut);
        }
        else {
            let parsedErr;
            try {
                parsedErr = JSON.parse(output);
            }
            catch (e) {
                parsedErr = output;
            }
            errorCallback(parsedErr);
        }
    });
    child.on('error', (err) => {
        errorCallback(err.toString());
    });
}
export default runCoreJar;
