// test/script.js
import { environment, database, module } from '../dist/js/index.js';

console.log("%c run.js #4 || DEBUT RUN.JS", 'background:blue;color:#fff;font-weight:bold;');

const pdaTest = '0182dbc9955c'; // 2201116SG
const ctxt = { appId: 'cli', profile: 'default' };

// -- ENV
const envInit = () => new Promise((resolve, reject) => {
  environment.initialize(resolve, reject, ctxt);
});

// -- DB
const databaseInit = () => new Promise((resolve, reject) => {
  console.log("%c run.js #11 || ici", 'background:blue;color:#fff;font-weight:bold;');
  database.initialize(resolve, reject, ctxt);
});

const executeRequest = (sql, params) => new Promise((resolve, reject) => {
  database.executeRequest(sql, params, resolve, reject, ctxt);
});

const getConfigList = () => new Promise((resolve, reject) => {
  database.config.getConfigList(resolve, reject, ctxt);
});

const updateValue = () => new Promise((resolve, reject) => {
  database.config.updateConfigValue(
    resolve,
    reject,
    { id: 3, value: 'mange bien ta soeur' },
    ctxt
  );
});

// -- ADB / MODULE
const getPdaList = () => new Promise((resolve, reject) => {
  module.adb.getPdaList(resolve, reject, ctxt);
});

const getApplicationList = () => new Promise((resolve, reject) => {
  database.application.getApplicationList(resolve, reject, ctxt);
});

const launchStreamPda = () => new Promise((resolve, reject) => {
  module.adb.launchScrcpy(resolve, reject, pdaTest, ctxt);
});

const launchApp = () => new Promise((resolve, reject) => {
  module.adb.launchApp(resolve, reject, 'net.distrilog.easymobile', pdaTest, ctxt);
});

const uninstallApp = () => new Promise((resolve, reject) => {
  module.adb.uninstallApp(resolve, reject, 'net.distrilog.easymobile', pdaTest, ctxt);
});

const clearApp = () => new Promise((resolve, reject) => {
  module.adb.clearApp(resolve, reject, 'net.distrilog.easymobile', pdaTest, ctxt);
});

const runCordova = () => new Promise((resolve, reject) => {
  module.adb.runCordova(
    resolve,
    reject,
    (progress) => console.log(progress.progressMessage),
    pdaTest,
    /* 1, */
    ctxt
  );
});

(async () => {
  console.log("%c run.js #34 || DEBUT METHODE ANONYME", 'background:blue;color:#fff;font-weight:bold;');
  try {
    console.log("%c run.js #33 || ici", 'background:blue;color:#fff;font-weight:bold;');

    // init fichiers
    const envResult = await envInit();
    console.log("%c run.js #18 || envResult : ", 'background:red;color:#fff;font-weight:bold;', envResult);

    // init base
    const databaseResult = await databaseInit();
    console.log("%c run.js #21 || databaseResult : ", 'background:red;color:#fff;font-weight:bold;', databaseResult);

    // tests éventuels
    const result = await executeRequest('select * from config_param');
    console.log("%c run.js #88 || result : ", 'background:red;color:#fff;font-weight:bold;', result);
    const { data: configList } = await getConfigList();
    console.log("%c run.js #89 || configList : ", 'background:red;color:#fff;font-weight:bold;', configList);
    /* const resultUpdateValue = await updateValue(); */

    /* const pdaList = await getPdaList();
    console.log("%c run.js #61 || pdaList : ", 'background:red;color:#fff;font-weight:bold;', pdaList);

    const appList = await getApplicationList();
    console.log("%c run.js #72 || appList : ", 'background:red;color:#fff;font-weight:bold;', appList);

    const res = await launchStreamPda();
    console.log("%c run.js #81 || res : ", 'background:red;color:#fff;font-weight:bold;', res); */

    /* const res = await launchApp(); */
    /* const res = await uninstallApp(); */
    /* const res = await clearApp(); await launchApp(); */
    /* const res2 = await runCordova(); */

  } catch (err) {
    console.log("%c run.js #19 || err : ", 'background:red;color:#fff;font-weight:bold;', err);
  }
})();
