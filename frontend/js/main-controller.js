const ui = require('./ui-controller');
const mainService = require('./main-service');

function init() {
    ui.show(uiconst.WELCOMEMAT, welcomemat.init);
    $(uiconst.VARIABLEMODAL).on('show.bs.modal', mainService.variableModalShow);
}

init();