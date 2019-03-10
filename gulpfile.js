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

function watchAssets() {
    return watch(['assets/**/*.css'], {ignoreInitial: false}, exports.default)
}

exports.css = css

exports.default = series(css)
exports.watch = watchAssets
