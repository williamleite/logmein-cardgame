const minimalRequest = require('minimal-request-promise');
const jd = require('../js/java-daemon');
const ui = require('../js/ui-controller');

let waitServerTimer = 1;

function waitServerProcess() {
    log.assemble("Trying to establish connection with the server", "loginfo");

    minimalRequest.get('http://localhost:8080').then(function (response) {
        log.assemble("Connection established!", "loginfo");
        ui.unloading();

        $(uiconst.BTNSTARTSERVER).addClass("d-none");
        $(uiconst.BTNSTARTGAME).removeClass("d-none");
    }, function (response) {
        log.assemble(`Server wasn't available! Trying again in [${waitServerTimer}] seconds(s).`, "logerror");
        window.setTimeout(waitServerProcess, waitServerTimer * 1000);
        waitServerTimer = (waitServerTimer + 1) % 10;
    });
}

function serverStdoutListener(data) {
    log.assemble(data.toString(), "loginfo");
}

function serverStderrListener(data) {
    log.assemble(data.toString(), "logerror");
}

module.exports = {
    gameId: undefined,
    players: [],

    init: function () {
        $(uiconst.BTNSTARTSERVER).on('click', (event) => {
            ui.loading();
            jd.load(serverStdoutListener, serverStderrListener);

            window.setTimeout(waitServerProcess, 1000);
        });
        $(uiconst.BTNREMOVEPLAYER).on('click', (event) => {
            ui.loading();

            var request = http.request({
                method: 'DELETE',
                protocol: 'http:',
                hostname: 'localhost',
                port: 8080,
                path: `/api/v1/game/${welcomemat.gameId}/${welcomemat.players.pop().id}`,
                headers: {
                    'Content-Type': 'application/json'
                }
            }, function (response) {
                log.assemble(`http://localhost:8080/api/v1/game [DELETE] - ${response.statusCode}`, response.statusCode === 200 ? 'loginfo' : 'logerror');
                if (response.statusCode === 200) {
                    welcomemat.drawBoard();
                }
                ui.unloading();
            });

            request.end();
        });
        $(uiconst.BTNADDDECKTOGAME).on('click', (event) => {
            ui.loading();

            var request = http.request({
                method: 'POST',
                protocol: 'http:',
                hostname: 'localhost',
                port: 8080,
                path: `/api/v1/deck/${welcomemat.gameId}`,
                headers: {
                    'Content-Type': 'application/json'
                }
            }, function (response) {
                log.assemble(`http://localhost:8080/api/v1/deck [POST] - ${response.statusCode}`, response.statusCode === 200 ? 'loginfo' : 'logerror');
                if (response.statusCode === 200) {
                    welcomemat.drawBoard();
                }
                ui.unloading();
            });

            request.end();
        });
        $(uiconst.BTNDEAL).on('click', (event) => {
            ui.loading();
            var playerId = $(uiconst.DEALTARGET).val();

            var request = http.request({
                method: 'POST',
                protocol: 'http:',
                hostname: 'localhost',
                port: 8080,
                path: `/api/v1/game/deal/${welcomemat.gameId}/${playerId}`,
                headers: {
                    'Content-Type': 'application/json'
                }
            }, function (response) {
                log.assemble(`http://localhost:8080/api/v1/game/deal [POST] - ${response.statusCode}`, response.statusCode === 200 ? 'loginfo' : 'logerror');
                if (response.statusCode === 200) {
                    let body = "";
                    response.on('data', (chunk) => {
                        body += chunk;
                    });
                    response.on('end', () => {
                        welcomemat.players.filter(p => p.id === playerId)[0].cards.push(JSON.parse(body));
                        welcomemat.drawBoard();
                    });
                }
                ui.unloading();
            });

            request.end();
        });
        $(uiconst.BTNSHUFFLE).on('click', (event) => {
            ui.loading();

            var request = http.request({
                method: 'POST',
                protocol: 'http:',
                hostname: 'localhost',
                port: 8080,
                path: `/api/v1/game/shuffle/${welcomemat.gameId}`,
                headers: {
                    'Content-Type': 'application/json'
                }
            }, function (response) {
                log.assemble(`http://localhost:8080/api/v1/game/shuffle [POST] - ${response.statusCode}`, response.statusCode === 200 ? 'loginfo' : 'logerror');
                if (response.statusCode === 200) {
                    welcomemat.drawBoard();
                }
                ui.unloading();
            });

            request.end();
        });
        $(uiconst.BTNADDPLAYER).on('click', (event) => {
            ui.loading();

            var request = http.request({
                method: 'POST',
                protocol: 'http:',
                hostname: 'localhost',
                port: 8080,
                path: `/api/v1/game/${welcomemat.gameId}/Player${welcomemat.players.length + 1}`,
                headers: {
                    'Content-Type': 'application/json'
                }
            }, function (response) {
                log.assemble(`http://localhost:8080/api/v1/game [POST] - ${response.statusCode}`, response.statusCode === 200 ? 'loginfo' : 'logerror');
                if (response.statusCode === 200) {
                    let body = "";
                    response.on('data', (chunk) => {
                        body += chunk;
                    });
                    response.on('end', () => {
                        welcomemat.players.push({ id: body, name: `Player${welcomemat.players.length + 1}`, cards: [] });
                        welcomemat.drawBoard();
                    });
                }
                ui.unloading();
            });

            request.end();
        });
        $(uiconst.BTNSTARTGAME).on('click', (event) => {
            ui.loading();

            var request = http.request({
                method: 'POST',
                protocol: 'http:',
                hostname: 'localhost',
                port: 8080,
                path: '/api/v1/game'
            }, function (response) {
                log.assemble(`http://localhost:8080/api/v1/game [POST] - ${response.statusCode}`, response.statusCode === 200 ? 'loginfo' : 'logerror');
                if (response.statusCode === 200) {
                    let body = "";
                    response.on('data', (chunk) => {
                        body += chunk;
                    });
                    response.on('end', () => {
                        welcomemat.gameId = body;
                    });

                    $(uiconst.BTNSTARTGAME).addClass("d-none");
                    $(uiconst.BTNSHUFFLE).removeClass("d-none");
                    $(uiconst.BTNADDDECKTOGAME).removeClass("d-none");
                    $(uiconst.BTNADDPLAYER).removeClass("d-none");
                }
                ui.unloading();
            });

            request.end();
        });
    },

    drawBoard: function () {
        ui.loading();

        var html = '<table><tr>';
        var dealTargetHtml = '';
        var cards = '';
        welcomemat.players.forEach(p => {
            html += `<th>${p.id}<br />${p.name}</th>`;
            dealTargetHtml += `<option value="${p.id}">${p.name}</option>`;
            cards += `<td>`;
            p.cards.forEach(c => {
                cards += `<p><img class="suit" src="images/${c.suit}.jpg"/> - ${c.face.replace('F_', '')}</p>`;
            });
            cards += `</td>`;
        });
        html += `</tr><tr>${cards}</tr></table><hr /><p>Peek</p><p>`;

        var request = http.request({
            method: 'GET',
            protocol: 'http:',
            hostname: 'localhost',
            port: 8080,
            path: `/api/v1/game/peek/${welcomemat.gameId}`
        }, function (response) {
            log.assemble(`http://localhost:8080/api/v1/game/peek [POST] - ${response.statusCode}`, response.statusCode === 200 ? 'loginfo' : 'logerror');
            if (response.statusCode === 200) {
                let body = "";
                response.on('data', (chunk) => {
                    body += chunk;
                });
                response.on('end', () => {
                    body = body.replace(new RegExp('F_', 'g'), '').replace(new RegExp('DIAMONDS', 'g'), '<img class="suit" src="images/DIAMONDS.jpg"/>');
                    body = body.replace(new RegExp('SPADES', 'g'), '<img class="suit" src="images/SPADES.jpg"/>');
                    body = body.replace(new RegExp('HEARTS', 'g'), '<img class="suit" src="images/HEARTS.jpg"/>');
                    body = body.replace(new RegExp('CLUBS', 'g'), '<img class="suit" src="images/CLUBS.jpg"/>');

                    html += `${body}</p><hr /><p>Players Total</p><p>`;

                    var request2 = http.request({
                        method: 'GET',
                        protocol: 'http:',
                        hostname: 'localhost',
                        port: 8080,
                        path: `/api/v1/analytics/players/${welcomemat.gameId}`
                    }, function (response) {
                        log.assemble(`http://localhost:8080/api/v1/analytics/players [POST] - ${response.statusCode}`, response.statusCode === 200 ? 'loginfo' : 'logerror');
                        if (response.statusCode === 200) {
                            let body2 = "";
                            response.on('data', (chunk) => {
                                body2 += chunk;
                            });
                            response.on('end', () => {
                                var playerTotal = JSON.parse(body2);
                                playerTotal.forEach(pt => {
                                    html += `${pt.player.name} - ${pt.total};`;
                                });
                                html += `</p><hr/><p>Suits Total</p><p>`;

                                var request3 = http.request({
                                    method: 'GET',
                                    protocol: 'http:',
                                    hostname: 'localhost',
                                    port: 8080,
                                    path: `/api/v1/analytics/suits/${welcomemat.gameId}`
                                }, function (response) {
                                    log.assemble(`http://localhost:8080/api/v1/analytics/suits [POST] - ${response.statusCode}`, response.statusCode === 200 ? 'loginfo' : 'logerror');
                                    if (response.statusCode === 200) {
                                        let body3 = "";
                                        response.on('data', (chunk) => {
                                            body3 += chunk;
                                        });
                                        response.on('end', () => {
                                            var suitsTotal = JSON.parse(body3);
                                            suitsTotal.forEach(st => {
                                                html += `<img class="suit" src="images/${st.suit}.jpg"/> - ${st.total} ; `;
                                            });
                                            html += `</p><hr/><p>Remaining</p><p>`;

                                            var request4 = http.request({
                                                method: 'GET',
                                                protocol: 'http:',
                                                hostname: 'localhost',
                                                port: 8080,
                                                path: `/api/v1/analytics/remaining/${welcomemat.gameId}`
                                            }, function (response) {
                                                log.assemble(`http://localhost:8080/api/v1/analytics/remainig [POST] - ${response.statusCode}`, response.statusCode === 200 ? 'loginfo' : 'logerror');
                                                if (response.statusCode === 200) {
                                                    let body4 = "";
                                                    response.on('data', (chunk) => {
                                                        body4 += chunk;
                                                    });
                                                    response.on('end', () => {
                                                        let remaining = JSON.parse(body4);
                                                        remaining.forEach(r => {
                                                            html += `<img class="suit" src="images/${r.suit}.jpg"/> - ${r.face.replace("F_", "")} - ${r.count} ; `;
                                                        });
                                                        html += `</p><hr />`;

                                                        $(uiconst.DEALTARGET).html(dealTargetHtml);
                                                        $(uiconst.GAMEBOARD).html(html);

                                                        if (welcomemat.players.length === 0) {
                                                            $(uiconst.BTNREMOVEPLAYER).addClass("d-none");
                                                            $(uiconst.DEALER).addClass("d-none");
                                                        } else {
                                                            $(uiconst.DEALER).removeClass("d-none");
                                                            $(uiconst.BTNREMOVEPLAYER).removeClass("d-none");
                                                        }
                                                    });
                                                }
                                                ui.unloading();
                                            });

                                            request4.end();
                                        });
                                    }
                                });

                                request3.end();
                            });
                        }
                    });

                    request2.end();
                });
            }
        });

        request.end();
    }
};