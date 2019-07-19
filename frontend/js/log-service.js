module.exports = {
    assemble: function(msg, clazz) {
        msg = moment().format('DD/MM/YYYY HH:mm:ss') + " - " + msg;
    
        var log = $("#logModalBody");
        var html = "<div class='row'><div class='col-12 " + clazz + "'>" + msg + "</div></div>";
        log.append(html);
    
        while (log.children().length > 50) {
            log.children().first().remove();
        }
    
        var lastLogLine = $(uiconst.LASTLOGLINE);
        lastLogLine.html(html);
    }
};