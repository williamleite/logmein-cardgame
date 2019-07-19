const ui = require('./ui-controller');

module.exports = {
    navClick: function (e) {
        e.preventDefault();
        var container = $(this).attr('href');
        var callback;

        if (container === uiconst.WELCOMEMAT) {
            callback = welcomemat.init;
        }

        ui.show(container, callback);
    },

    variableModalShow: function (event) {
        var serverProcess = "-- --";
        var sp = electron.remote.getGlobal('sharedObj').serverProcess;
        if (sp) {
            serverProcess = `PID: ${sp.pid}; Spawn: ${sp.spawnfile}; Args: ${JSON.stringify(sp.spawnargs)}`;
        }

        $("#variableModalBody").html(`
            <div class="row">
                <div class="col-4">Server Process</div>
                <div class="col-8">${serverProcess}</div>
            </div>
            <hr />
            <div class="row">
                <div class="col-4">Game ID</div>
                <div class="col-8">${welcomemat.gameId}</div>
            </div>
            <hr />
            <div class="row">
                <div class="col-4">Players</div>
                <div class="col-8">${JSON.stringify(welcomemat.players)}</div>
            </div>
        `);
    }
};