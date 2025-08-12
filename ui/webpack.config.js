const path = require('path');
const HtmlWebPackPlugin = require('html-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');

const config = {
    name: 'ui',
    target: 'web',
    entry: './src/index.js',
    output: {
        path: path.join(__dirname, 'build'),
        filename: "[name].js",
        publicPath: '/',
        assetModuleFilename: 'assets/[hash][ext][query]'
    },
    plugins: [
        new MiniCssExtractPlugin({
            filename: '[name].[contenthash].css'
        }),
        new HtmlWebPackPlugin({
            template: path.join(__dirname, 'public', "index.html"),
            filename: "index.html",
            favicon: path.join(__dirname, 'public', "favicon.ico")
        })
    ],
    devServer: {
        historyApiFallback: true,
        client: {
            logging: 'verbose'
        }
    },
    resolve: {
        extensions: ['.js', '.jsx', '.json']
    },
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                exclude: /node_modules/,
                use: {
                    loader: "babel-loader",
                    options: {
                        presets: ['@babel/preset-env', '@babel/preset-react']
                    }
                }
            },
            {
                test: /\.css$/i,
                use: ["style-loader", "css-loader"],
            },
            // {
            //     test: /\.svg$/,
            //     use: ['@svgr/webpack'],
            // },
            {
                test: /\.(png|jp(e*)g|svg|gif)$/,
                type: 'asset/resource',
                generator: {
                    filename: 'assets/[name]-[contenthash][ext]'
                },
            },
        ]
    }
}

module.exports = config;
