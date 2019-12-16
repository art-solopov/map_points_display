const { src, dest, series, watch } = require('gulp')
const concat = require('gulp-concat')
const postcss = require('gulp-postcss')
const mergeStreams = require('merge-stream')

const postcssPlugins = {
    fontFamilySystemUI: require('postcss-font-family-system-ui'),
    easyImport: require('postcss-easy-import'),
    nesting: require('postcss-nesting'),
    simpleVars: require('postcss-simple-vars'),
    each: require('postcss-each'),
    extend: require('postcss-extend')
}

function css() {
    let assets =  src(['assets/css/app.css'])
        .pipe(postcss([
            postcssPlugins.easyImport(),
            postcssPlugins.each(),
            postcssPlugins.nesting(),
            postcssPlugins.extend(),
            postcssPlugins.simpleVars(),
            postcssPlugins.fontFamilySystemUI()
        ]))

    return assets.pipe(concat('app.css'))
        .pipe(dest('resources/public/css/'))
}

function js() {
    let mapScripts = src(['assets/js/map/**/*.js', 'assets/js/map.js']).pipe(concat('map.js'))
    let geoScripts = src(['assets/js/geocode.js'])


    let assets = mergeStreams(mapScripts, geoScripts)

    return assets.pipe(dest('resources/public/js'))
}

function watchAssets() {
    return watch(['assets/**/*.css', 'assets/**/*.js'], {ignoreInitial: false}, exports.default)
}

exports.css = css
exports.js = js

exports.default = series(css, js)
exports.watch = watchAssets
