const gulp = require("gulp");
const { src, dest, watch, series } = require("gulp");

const sass = require("gulp-sass")(require("sass"));

const browserSync = require("browser-sync").create();
const os = require("os");

// Lấy IP Radmin VPN của máy tự động
function getLocalExternalIP() {
  const interfaces = os.networkInterfaces();
  for (const name of Object.keys(interfaces)) {
    for (const iface of interfaces[name]) {
      if (
        iface.family === "IPv4" &&
        !iface.internal &&
        iface.address.startsWith("26.")
      ) {
        return iface.address;
      }
    }
  }
  return "localhost";
}

const host = getLocalExternalIP();

browserSync.init({
  server: {
    baseDir: "./",
  },
  host: host,
  port: 3000,
  open: "external",
  notify: false,
});

// Sass Task
function scssTask() {
  return src("./assets/scss/*.scss")
    .pipe(sass())
    .pipe(gulp.dest("./assets/css/"));
}

// BrowserSync Task
function browserSyncServer(cb) {
  // browserSync.init({
  //   server: {
  //     baseDir: ".",
  //   },
  // });
  // cb();
  browserSync.init({
    server: {
      baseDir: "./",
    },
    host: host,
    port: 3000,
    open: "external",
    notify: false,
  });
}

function browserSyncReload(cb) {
  browserSync.reload();
  cb();
}

// Watch Task
function watchTask() {
  watch(["*.html", "./assets/js/**/*.js"], browserSyncReload);
  watch(["./assets/scss/**/*.scss"], series(scssTask, browserSyncReload));
}

exports.default = series(scssTask, browserSyncServer, watchTask);
