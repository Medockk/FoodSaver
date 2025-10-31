console.log("[SQLJS WORKER] started");
importScripts('/distributions/sql-wasm.js');

async function main() {
  try {
    const SQL = await initSqlJs({
      locateFile: file => `/distributions/${file}` // путь к sql-wasm.wasm
    });

    const dbData = await loadDbFromIndexedDB();
    const db = dbData ? new SQL.Database(new Uint8Array(dbData)) : new SQL.Database();

    console.log("[SQLJS WORKER] database initialized");
    postMessage({ type: "ready" });

    // --- ИСПРАВЛЕНИЕ: ПРАВИЛЬНЫЙ ОБРАБОТЧИК СООБЩЕНИЙ ---
    self.onmessage = async (e) => {
      const msg = e.data;
      console.log(msg);

      try {
        let results;

        switch (msg && msg.action) {
          case "exec":
            if (!msg["sql"]) {
               throw new Error("exec: Missing query string");
            }

            // Выполнение SQL
            if (msg.params && Object.keys(msg.params).length > 0) {
                // Выполнение с параметрами
                const stmt = db.prepare(msg.sql);
                const boundParams = Object.values(msg.params);
                stmt.bind(boundParams);
                stmt.step();
                stmt.free();
                results = { columns: [], values: [] };
            } else {
                // Выполнение без параметров
                const execResult = db.exec(msg.sql);
                results =
                  Array.isArray(execResult) && execResult.length > 0
                    ? execResult[0]
                    : { columns: [], values: [] };
            }

            // АСИНХРОННОЕ СОХРАНЕНИЕ: Если здесь произойдет сбой (IndexedDB),
            // он будет пойман внешним try/catch.
            try {
                await saveDbToIndexedDB(db);
            } catch (saveError) {
                console.error("[IndexedDB SAVE ERROR] Failed to save DB:", saveError);
                // Продолжаем выполнение, Worker не должен сбоить из-за сбоя IndexedDB
            }

            return postMessage({
                id: msg.id,
                results
            });

          case "begin_transaction":
          case "end_transaction":
          case "rollback_transaction":
            const sql = msg.action.split('_')[0].toUpperCase() + ' TRANSACTION;';
            results = db.exec(sql);
            // Сохранение обязательно после commit/rollback
            if (msg.action === "end_transaction" || msg.action === "rollback_transaction") {
                await saveDbToIndexedDB(db);
            }
            return postMessage({ id: msg.id, results });

          default:
            throw new Error("Unsupported action: " + msg.action);
        }
      } catch (err) {
        console.error("[SQL WORKER ERROR] SQL constraint failed, sending empty results:", err.message);

        return postMessage({
            id: msg.id,
            error: err.message,
            stack: err.stack
        });
      }
    };
  } catch (err) {
    console.error("[INIT ERROR] Worker failed to initialize:", err);
      // Отправляем ТОЛЬКО сообщение об ошибке инициализации
    postMessage({ type: "worker-error", message: err.message, stack: err.stack });
  }
}

main();

// --- функции IndexedDB ---
async function saveDbToIndexedDB(db) {
  return new Promise((resolve, reject) => {
    const request = indexedDB.open("MainAppDatabase", 1);
    request.onupgradeneeded = () => request.result.createObjectStore("db");
    request.onsuccess = () => {
      const tx = request.result.transaction("db", "readwrite");
      tx.objectStore("db").put(db.export(), "data");
      tx.oncomplete = resolve;
      tx.onerror = reject;
    };
    request.onerror = reject;
  });
}

async function loadDbFromIndexedDB() {
  return new Promise((resolve, reject) => {
    const request = indexedDB.open("MainAppDatabase", 1);
    request.onupgradeneeded = () => request.result.createObjectStore("db");
    request.onsuccess = () => {
      const tx = request.result.transaction("db", "readonly");
      const getReq = tx.objectStore("db").get("data");
      getReq.onsuccess = () => resolve(getReq.result);
      getReq.onerror = reject;
    };
    request.onerror = reject;
  });
}