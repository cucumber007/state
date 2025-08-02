const path = require("path");
const CopyPlugin = require("copy-webpack-plugin");

module.exports = {
  entry: "./src/index.tsx",
  output: {
    path: path.resolve(__dirname, "../src/jsMain/resources"),
    filename: "bundle.js",
    clean: true,
  },
  resolve: {
    extensions: [".tsx", ".ts", ".js"],
  },
  module: {
    rules: [
      {
        test: /\.tsx?$/,
        use: "babel-loader",
        exclude: /node_modules/,
      },
    ],
  },
  plugins: [
    new CopyPlugin({
      patterns: [
        {
          from: "src/index.html",
          to: "index.html",
          transform(content) {
            return content.toString().replace(
              '<script type="module" src="./index.js"></script>',
              '<script src="bundle.js"></script>'
            );
          },
        },
      ],
    }),
  ],
  mode: "development",
  devtool: "source-map",
};
