// test/script.js
import { environment, database } from '../dist/js/index.js';

const envInit = () => {
  return new Promise((resolve, reject) => {
    environment.initialize(resolve, reject)
  })
}

const databaseInit = () => {
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

(async () => {
  try {
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
    const { data: configList } = await getConfigList()
    console.log("%c run.js #44 || configList : ", 'background:red;color:#fff;font-weight:bold;', configList);

  } catch(err) {
    console.log("%c run.js #19 || err : ", 'background:red;color:#fff;font-weight:bold;', err);
  }
})();
