module.exports = (config, env, helpers) => {
    // Node-полифилы
    config.resolve.fallback = {
        ...config.resolve.fallback,
        path: require.resolve("path-browserify"),
        crypto: require.resolve("crypto-browserify"),
        stream: require.resolve("stream-browserify"),
        buffer: require.resolve("buffer/"),
        vm: require.resolve("vm-browserify"),
        fs: false,
        events: require.resolve("events/"),
    };

    // Поддержка WASM
    config.module.rules.push({
        test: /\.wasm$/,
        type: "asset/resource",
    });

    config.experiments = {
        ...config.experiments,
        asyncWebAssembly: true,
    };

    return config;
};
