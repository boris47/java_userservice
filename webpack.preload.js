const path = require('path');

module.exports = {
  entry: './src/preload.ts',
  target: 'electron-preload',
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
  },
  output: {
    filename: 'preload.js',
    path: path.resolve(__dirname, 'dist')
  }
};
