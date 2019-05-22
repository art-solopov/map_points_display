const { src, dest, series, watch } = require('gulp')
const concat = require('gulp-concat')
const postcss = require('gulp-postcss')
const mergeStreams = require('merge-stream')

const postcssPlugins = {
    fontFamilySystemUI: require('postcss-font-family-system-ui')
}

function css() {
    let deps = src(['node_modules/normalize.css/normalize.css'])

    let assets =  src(['assets/css/*.css'])
        .pipe(postcss([
            postcssPlugins.fontFamilySystemUI()
        ]))

    return mergeStreams(assets, deps)
        .pipe(concat('app.css'))
        .pipe(dest('resources/public/css/'))
}

function js() {
    let assets = src(['assets/js/**/*.js'])

    return assets.pipe(concat('app.js'))
        .pipe(dest('resources/public/js'))
}

function watchAssets() {
    return watch(['assets/**/*.css', 'assets/**/*.js'], {ignoreInitial: false}, exports.default)
}

exports.css = css
exports.js = js

exports.default = series(css, js)
exports.watch = watchAssets
