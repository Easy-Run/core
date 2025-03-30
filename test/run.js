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
  console.log("%c run.js #40 || database.application : ", 'background:red;color:#fff;font-weight:bold;', database.application);
  return new Promise((resolve, reject) => database.application.getApplicationList(resolve, reject))
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

    const appList = await getApplicationList()
    console.log("%c run.js #72 || appList : ", 'background:red;color:#fff;font-weight:bold;', appList);

  } catch(err) {
    console.log("%c run.js #19 || err : ", 'background:red;color:#fff;font-weight:bold;', err);
  }
})();
