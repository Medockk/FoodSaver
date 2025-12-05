config.devServer = config.devServer || {};

config.devServer.host = '127.0.0.1';
config.devServer.port = 8087;

config.devServer.allowedHosts = [
    'mylocalhost.com',
    'localhost'
];

config.devServer.open = false;