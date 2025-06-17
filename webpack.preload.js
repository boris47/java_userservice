const path = require('path');

module.exports = {
  mode: 'development',
  target: 'electron-preload',
  entry: './src/preload.ts',
  module: {
    rules: [
      {
        test: /\.ts$/,
        include: /src/,
        use: {
          loader: 'ts-loader',
          options: {
            configFile: 'tsconfig.electron.json'
          }
        }
      }
    ]
  },
  resolve: {
    extensions: ['.ts', '.js']
  },
  output: {
    filename: 'preload.js',
    path: path.resolve(__dirname, 'dist')
  }
};
