const CopyWebpackPlugin = require("copy-webpack-plugin");
const path = require("path");

// Копируем sql.js файлы прямо в финальную директорию сборки
config.plugins.push(
  new CopyWebpackPlugin({
    patterns: [
      {
        from: path.resolve(__dirname, "../../node_modules/sql.js/dist/sql-wasm.wasm"),
        to: path.resolve(__dirname, "../../distributions/"),
      },
      {
        from: path.resolve(__dirname, "../../node_modules/sql.js/dist/sql-wasm.js"),
        to: path.resolve(__dirname, "../../distributions/"),
      },
      {
        from: path.resolve(__dirname, "../../node_modules/@cashapp/sqldelight-sqljs-worker/sqljs.worker.js"),
        to: path.resolve(__dirname, "../../distributions/"),
      },
    ],
  })
);
