// test/script.js
import { environment, database, module } from '../dist/js/index.js';

console.log("%c run.js #4 || DEBUT RUN.JS", 'background:blue;color:#fff;font-weight:bold;');

const envInit = () => {
  return new Promise((resolve, reject) => {
    environment.initialize(resolve, reject)
  })
}

const databaseInit = () => {
  console.log("%c run.js #11 || ici", 'background:blue;color:#fff;font-weight:bold;');
  return new Promise((resolve, reject) => {
    database.initialize(resolve, reject)
  })
}

const executeRequest = (sql, params) => {
  return new Promise((resolve, reject) => {
    database.executeRequest(sql, params, resolve, reject)
  })
}

const getConfigList = () => {
  return new Promise((resolve, reject) => {
    database.config.getConfigList(resolve, reject)
  })
}

const updateValue = () => {
  return new Promise((resolve, reject) => database.config.updateConfigValue(resolve, reject, { id: 3, value: 'mange bien ta soeur' }))
}

const getPdaList = () => {
  return new Promise((resolve, reject) => module.adb.getPdaList(resolve, reject))
}

const getApplicationList = () => {
  return new Promise((resolve, reject) => database.application.getApplicationList(resolve, reject))
}

const launchStreamPda = () => {
  return new Promise((resolve, reject) => module.adb.launchScrcpy(resolve, reject, '18344B686A'))
}

const launchApp = () => {
  return new Promise((resolve, reject) => module.adb.launchApp(resolve, reject, 'net.distrilog.easymobile', '18344B686A'))
}

const uninstallApp = () => {
  return new Promise((resolve, reject) => module.adb.uninstallApp(resolve, reject, 'net.distrilog.easymobile', '18344B686A'))
}

const clearApp = () => {
  return new Promise((resolve, reject) => module.adb.clearApp(resolve, reject, 'net.distrilog.easymobile', '18344B686A'))
}

const runCordova = () => {
  return new Promise((resolve, reject) => module.adb.runCordova(resolve, reject,
    (progress) => console.log(progress.progressMessage), '18344B686A'/* , 1 */)
  )
}

(async () => {
  console.log("%c run.js #34 || DEBUT METHODE ANONYME", 'background:blue;color:#fff;font-weight:bold;');
  try {
    console.log("%c run.js #33 || ici", 'background:blue;color:#fff;font-weight:bold;');
    //? init des fichiers
    const envResult = await envInit();
    console.log("%c run.js #18 || envResult : ", 'background:red;color:#fff;font-weight:bold;', envResult);
    
    //? init de la base
    const databaseResult = await databaseInit();
    console.log("%c run.js #21 || databaseResult : ", 'background:red;color:#fff;font-weight:bold;', databaseResult);

    //? test requête en direct
    /* const result = await executeRequest('update config_param set value = ?1 where id = ?2', [null, 3])
    console.log("%c run.js #31 || result : ", 'background:red;color:#fff;font-weight:bold;', result); */

    //? test récupération depuis controlleur
    /* const { data: configList } = await getConfigList()
    console.log("%c run.js #44 || configList : ", 'background:red;color:#fff;font-weight:bold;', configList); */

    //? test updateValue
    /* const resultUpdateValue = await updateValue()
    console.log("%c run.js #48 || resultUpdateValue : ", 'background:red;color:#fff;font-weight:bold;', resultUpdateValue); */

    //? liste des pda avec adb
    const pdaList = await getPdaList()
    console.log("%c run.js #61 || pdaList : ", 'background:red;color:#fff;font-weight:bold;', pdaList);

    //? liste des applications
    const appList = await getApplicationList()
    console.log("%c run.js #72 || appList : ", 'background:red;color:#fff;font-weight:bold;', appList);

    //? lancement de scrcpy sur le redmi note 11
    /* const res = await launchStreamPda()
    console.log("%c run.js #81 || res : ", 'background:red;color:#fff;font-weight:bold;', res); */

    //? lancement d'une app
    /* const res = await launchApp()
    console.log("%c run.js #92 || res : ", 'background:red;color:#fff;font-weight:bold;', res); */
  
    //? désinstallation d'une app
    /* const res = await uninstallApp()
    console.log("%c run.js #92 || res : ", 'background:red;color:#fff;font-weight:bold;', res); */
  
    //? clear d'une app
    /* const res = await clearApp()
    console.log("%c run.js #92 || res : ", 'background:red;color:#fff;font-weight:bold;', res);
    await launchApp() */
  
    //? run cordova
    await runCordova()
    console.log("%c run.js #116 || fin du build", 'background:blue;color:#fff;font-weight:bold;');
    // console.log("%c run.js #92 || res : ", 'background:red;color:#fff;font-weight:bold;', res);

  } catch(err) {
    console.log("%c run.js #19 || err : ", 'background:red;color:#fff;font-weight:bold;', err);
  }
})();
