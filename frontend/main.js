// Modules to control application life and create native browser window
const { app, BrowserWindow } = require('electron');
const path = require('path');
const url = require('url');
const jd = require('./js/java-daemon.js');

// Keep a global reference of the window object, if you don't, the window will
// be closed automatically when the JavaScript object is garbage collected.
let mainWindow;
let splash;

global.sharedObj = { serverProcess: undefined, serverCompanies: undefined, eventSources: {}, selectedCompany: undefined, maxrecords: undefined };

function createWindow() {
    makeSingleInstance();

    mainWindow = new BrowserWindow({
        width: 800,
        height: 600,
        show: false,
        frame: true
    });

    splash = new BrowserWindow({
        width: 800,
        height: 600,
        transparent: true,
        alwaysOnTop: true,
        frame: false
    });

    splash.loadFile('splash.html');

    let mainPageUrl = url.format({
        pathname: path.join(__dirname, 'index.html'),
        protocol: 'file:',
        slashes: true
    });
    mainWindow.loadURL(mainPageUrl);

    // Open the DevTools.
    // mainWindow.webContents.openDevTools()

    mainWindow.once('ready-to-show', () => {
        splash.destroy();
        splash = null;
        mainWindow.show();
    });

    mainWindow.on('closed', () => {
        mainWindow = null;
    });

    mainWindow.on('close', (evt) => {
        if (global.sharedObj.serverProcess) {
            evt.preventDefault();
            jd.kill(() => {
                global.sharedObj.serverProcess = undefined;
                mainWindow.close();
            }, global.sharedObj.serverProcess);            
        }
    });
}

app.on('ready', createWindow);

app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') {
        app.quit();
    }
});

app.on('activate', () => {
    if (mainWindow === null) {
        createWindow();
    }
});

// Make this app a single instance app.
//
// The main window will be restored and focused instead of a second window
// opened when a person attempts to launch a second instance.
//
// Returns true if the current version of the app should quit instead of
// launching.
function makeSingleInstance() {
    if (process.mas) return

    app.requestSingleInstanceLock()

    app.on('second-instance', () => {
        if (mainWindow) {
            if (mainWindow.isMinimized()) mainWindow.restore()
            mainWindow.focus()
        }
    })
}