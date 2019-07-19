var childProcess = require("child_process");
var fs = require("fs");
var remote = require("electron").remote;

function _kill(callback, serverProcess) {
    const tKill = require('tree-kill');
    if (!serverProcess) {
        serverProcess = remote.getGlobal('sharedObj').serverProcess;
    }
    tKill(serverProcess.pid, 'SIGTERM', callback);
}

function _load(stdoutListener, stderrListener) {
    // If there is a process running, kill it
    if (remote.getGlobal('sharedObj').serverProcess) {
        _kill(() => {
            remote.getGlobal('sharedObj').serverProcess = undefined;
            _load(stdoutListener, stderrListener);
        });
    } else {
        fs.readdir('./java', (err, dir) => {
            var filePath;

            for (var i = 0, path; path = dir[i]; i++) {
                if (path.indexOf('.jar') !== -1) {
                    filePath = path;
                }
            }

            if (!filePath) {
                alert("No executable was found!");
                return false;
            }

            var cp = childProcess.exec('java -jar ./java/' + filePath);

            cp.stdout.on('data', stdoutListener);
            cp.stderr.on('data', stderrListener);

            remote.getGlobal('sharedObj').serverProcess = cp;
        });
    }
}

module.exports = {
    kill: _kill,
    load: _load
};