const path = require('path');

module.exports = {
  mode: 'development',
  target: 'electron-main',
  entry: './src/main/main.ts',
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
    filename: 'main.js',
    path: path.resolve(__dirname, 'dist')
  }
};
