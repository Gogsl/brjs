/* automatically generated by JSCoverage - do not edit */
if (typeof _$jscoverage === 'undefined') _$jscoverage = {};
if (! _$jscoverage['streams/DateRollingFileStream.js']) {
  _$jscoverage['streams/DateRollingFileStream.js'] = [];
  _$jscoverage['streams/DateRollingFileStream.js'][1] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][2] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][9] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][11] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][12] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][13] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][14] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][15] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][16] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][18] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][19] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][20] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][21] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][22] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][24] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][25] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][26] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][27] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][29] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][30] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][31] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][34] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][36] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][38] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][40] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][41] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][44] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][47] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][48] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][50] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][53] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][54] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][56] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][58] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][59] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][60] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][65] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][66] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][74] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][77] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][79] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][83] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][84] = 0;
  _$jscoverage['streams/DateRollingFileStream.js'][85] = 0;
}
_$jscoverage['streams/DateRollingFileStream.js'][1]++;
"use strict";
_$jscoverage['streams/DateRollingFileStream.js'][2]++;
var BaseRollingFileStream = require("./BaseRollingFileStream"), debug = require("../debug")("DateRollingFileStream"), format = require("../date_format"), async = require("async"), fs = require("fs"), util = require("util");
_$jscoverage['streams/DateRollingFileStream.js'][9]++;
module.exports = DateRollingFileStream;
_$jscoverage['streams/DateRollingFileStream.js'][11]++;
function DateRollingFileStream(filename, pattern, options, now) {
  _$jscoverage['streams/DateRollingFileStream.js'][12]++;
  debug("Now is " + now);
  _$jscoverage['streams/DateRollingFileStream.js'][13]++;
  if (pattern && typeof pattern === "object") {
    _$jscoverage['streams/DateRollingFileStream.js'][14]++;
    now = options;
    _$jscoverage['streams/DateRollingFileStream.js'][15]++;
    options = pattern;
    _$jscoverage['streams/DateRollingFileStream.js'][16]++;
    pattern = null;
  }
  _$jscoverage['streams/DateRollingFileStream.js'][18]++;
  this.pattern = pattern || ".yyyy-MM-dd";
  _$jscoverage['streams/DateRollingFileStream.js'][19]++;
  this.now = now || Date.now;
  _$jscoverage['streams/DateRollingFileStream.js'][20]++;
  this.lastTimeWeWroteSomething = format.asString(this.pattern, new Date(this.now()));
  _$jscoverage['streams/DateRollingFileStream.js'][21]++;
  this.baseFilename = filename;
  _$jscoverage['streams/DateRollingFileStream.js'][22]++;
  this.alwaysIncludePattern = false;
  _$jscoverage['streams/DateRollingFileStream.js'][24]++;
  if (options) {
    _$jscoverage['streams/DateRollingFileStream.js'][25]++;
    if (options.alwaysIncludePattern) {
      _$jscoverage['streams/DateRollingFileStream.js'][26]++;
      this.alwaysIncludePattern = true;
      _$jscoverage['streams/DateRollingFileStream.js'][27]++;
      filename = this.baseFilename + this.lastTimeWeWroteSomething;
    }
    _$jscoverage['streams/DateRollingFileStream.js'][29]++;
    delete options.alwaysIncludePattern;
    _$jscoverage['streams/DateRollingFileStream.js'][30]++;
    if (Object.keys(options).length === 0) {
      _$jscoverage['streams/DateRollingFileStream.js'][31]++;
      options = null;
    }
  }
  _$jscoverage['streams/DateRollingFileStream.js'][34]++;
  debug("this.now is " + this.now + ", now is " + now);
  _$jscoverage['streams/DateRollingFileStream.js'][36]++;
  DateRollingFileStream.super_.call(this, filename, options);
}
_$jscoverage['streams/DateRollingFileStream.js'][38]++;
util.inherits(DateRollingFileStream, BaseRollingFileStream);
_$jscoverage['streams/DateRollingFileStream.js'][40]++;
DateRollingFileStream.prototype.shouldRoll = (function () {
  _$jscoverage['streams/DateRollingFileStream.js'][41]++;
  var lastTime = this.lastTimeWeWroteSomething, thisTime = format.asString(this.pattern, new Date(this.now()));
  _$jscoverage['streams/DateRollingFileStream.js'][44]++;
  debug("DateRollingFileStream.shouldRoll with now = " + this.now() + ", thisTime = " + thisTime + ", lastTime = " + lastTime);
  _$jscoverage['streams/DateRollingFileStream.js'][47]++;
  this.lastTimeWeWroteSomething = thisTime;
  _$jscoverage['streams/DateRollingFileStream.js'][48]++;
  this.previousTime = lastTime;
  _$jscoverage['streams/DateRollingFileStream.js'][50]++;
  return thisTime !== lastTime;
});
_$jscoverage['streams/DateRollingFileStream.js'][53]++;
DateRollingFileStream.prototype.roll = (function (filename, callback) {
  _$jscoverage['streams/DateRollingFileStream.js'][54]++;
  var that = this;
  _$jscoverage['streams/DateRollingFileStream.js'][56]++;
  debug("Starting roll");
  _$jscoverage['streams/DateRollingFileStream.js'][58]++;
  if (this.alwaysIncludePattern) {
    _$jscoverage['streams/DateRollingFileStream.js'][59]++;
    this.filename = this.baseFilename + this.lastTimeWeWroteSomething;
    _$jscoverage['streams/DateRollingFileStream.js'][60]++;
    async.series([this.closeTheStream.bind(this), this.openTheStream.bind(this)], callback);
  }
  else {
    _$jscoverage['streams/DateRollingFileStream.js'][65]++;
    var newFilename = this.baseFilename + this.previousTime;
    _$jscoverage['streams/DateRollingFileStream.js'][66]++;
    async.series([this.closeTheStream.bind(this), deleteAnyExistingFile, renameTheCurrentFile, this.openTheStream.bind(this)], callback);
  }
  _$jscoverage['streams/DateRollingFileStream.js'][74]++;
  function deleteAnyExistingFile(cb) {
    _$jscoverage['streams/DateRollingFileStream.js'][77]++;
    fs.unlink(newFilename, (function (err) {
  _$jscoverage['streams/DateRollingFileStream.js'][79]++;
  cb();
}));
}
  _$jscoverage['streams/DateRollingFileStream.js'][83]++;
  function renameTheCurrentFile(cb) {
    _$jscoverage['streams/DateRollingFileStream.js'][84]++;
    debug("Renaming the " + filename + " -> " + newFilename);
    _$jscoverage['streams/DateRollingFileStream.js'][85]++;
    fs.rename(filename, newFilename, cb);
}
});
_$jscoverage['streams/DateRollingFileStream.js'].source = ["\"use strict\";","var BaseRollingFileStream = require('./BaseRollingFileStream')",", debug = require('../debug')('DateRollingFileStream')",", format = require('../date_format')",", async = require('async')",", fs = require('fs')",", util = require('util');","","module.exports = DateRollingFileStream;","","function DateRollingFileStream(filename, pattern, options, now) {","  debug(\"Now is \" + now);","  if (pattern &amp;&amp; typeof(pattern) === 'object') {","    now = options;","    options = pattern;","    pattern = null;","  }","  this.pattern = pattern || '.yyyy-MM-dd';","  this.now = now || Date.now;","  this.lastTimeWeWroteSomething = format.asString(this.pattern, new Date(this.now()));","  this.baseFilename = filename;","  this.alwaysIncludePattern = false;","  ","  if (options) {","    if (options.alwaysIncludePattern) {","      this.alwaysIncludePattern = true;","      filename = this.baseFilename + this.lastTimeWeWroteSomething;","    }","    delete options.alwaysIncludePattern;","    if (Object.keys(options).length === 0) { ","      options = null; ","    }","  }","  debug(\"this.now is \" + this.now + \", now is \" + now);","  ","  DateRollingFileStream.super_.call(this, filename, options);","}","util.inherits(DateRollingFileStream, BaseRollingFileStream);","","DateRollingFileStream.prototype.shouldRoll = function() {","  var lastTime = this.lastTimeWeWroteSomething,","  thisTime = format.asString(this.pattern, new Date(this.now()));","  ","  debug(\"DateRollingFileStream.shouldRoll with now = \" + ","        this.now() + \", thisTime = \" + thisTime + \", lastTime = \" + lastTime);","  ","  this.lastTimeWeWroteSomething = thisTime;","  this.previousTime = lastTime;","  ","  return thisTime !== lastTime;","};","","DateRollingFileStream.prototype.roll = function(filename, callback) {","  var that = this;","  ","  debug(\"Starting roll\");","  ","  if (this.alwaysIncludePattern) {","    this.filename = this.baseFilename + this.lastTimeWeWroteSomething;","    async.series([","      this.closeTheStream.bind(this),","      this.openTheStream.bind(this)","    ], callback);","  } else {","    var newFilename = this.baseFilename + this.previousTime;","    async.series([","      this.closeTheStream.bind(this),","      deleteAnyExistingFile,","      renameTheCurrentFile,","      this.openTheStream.bind(this)","    ], callback);","  }","  ","  function deleteAnyExistingFile(cb) {","    //on windows, you can get a EEXIST error if you rename a file to an existing file","    //so, we'll try to delete the file we're renaming to first","    fs.unlink(newFilename, function (err) {","      //ignore err: if we could not delete, it's most likely that it doesn't exist","      cb();","    });","  }","","  function renameTheCurrentFile(cb) {","    debug(\"Renaming the \" + filename + \" -&gt; \" + newFilename);","    fs.rename(filename, newFilename, cb);","  }","","};"];