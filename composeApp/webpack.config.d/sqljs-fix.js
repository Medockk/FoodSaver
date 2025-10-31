config.resolve = {
    ...config.resolve,
    fallback: {
        ...config.resolve?.fallback,
        path: require.resolve("path-browserify"),
        crypto: require.resolve("crypto-browserify"),
        stream: require.resolve("stream-browserify"),
        buffer: require.resolve("buffer/"),
        vm: require.resolve("vm-browserify"),
        fs: false, // 👈 ВАЖНО: отключаем Node-модуль fs
    },
};
