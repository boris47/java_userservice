const path = require('path');

module.exports = {
  entry: './src/main/main.ts',
  target: 'electron-main',
  mode: 'development',
  module: {
    rules: [
      {
        test: /\.ts$/,
        include: [
          path.resolve(__dirname, 'src'),
          path.resolve(__dirname, 'shared')
        ],
        use: [{
          loader: 'ts-loader',
          options: {
            configFile: 'tsconfig.electron.json'
          }
        }]
      }
    ]
  },
  resolve: {
    extensions: ['.ts', '.js'],
    alias: {
      '@shared': path.resolve(__dirname, 'shared')
    }
  },
  output: {
    filename: 'main.js',
    path: path.resolve(__dirname, 'dist')
  }
};
