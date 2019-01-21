// Allow angular using electron module (native node modules)
const fs = require('fs');
const git = require('git-describe');
const path = require('path');
const f_angular = 'node_modules/@angular-devkit/build-angular/src/angular-cli-files/models/webpack-configs/browser.js';

fs.readFile(f_angular, 'utf8', function(err, data) {
    if (err) {
        return console.log(err);
    }
    var result = data.replace(/target: "electron-renderer",/g, '');
    var result = result.replace(/target: "web",/g, '');
    var result = result.replace(/return \{/g, 'return {target: "web",');

    fs.writeFile(f_angular, result, 'utf8', function(err) {
        if (err) return console.log(err);
    });
});

// @ts-check

const info = git.gitDescribeSync({
    longSemver: true
});
const {
    version: appVersion
} = require('./package.json')
const file = path.resolve(__dirname, 'src', 'assets', 'version.json');

fs.writeFileSync(
    file,
    JSON.stringify(Object.assign(info, { appVersion }),null, 2), { encoding: 'utf-8' });

console.log(`Wrote version info ${info.raw} to ${path.relative(path.resolve(__dirname, '..'), file)}`);