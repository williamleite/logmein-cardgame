module.exports = {
    loading: function () {
        $("#loading").removeClass("d-none");
        $("#tabcontainer").addClass("d-none");
    },

    unloading: function() {
        $("#loading").addClass("d-none");
        $("#tabcontainer").removeClass("d-none");
    },

    show: function (container, callback) {
        $("#loading").addClass("d-none");

        $(container).load("./html/" + container.replace("#", "") + ".html", callback);
        $(`${uiconst.TABLINKS} a[href="${container}"]`).tab("show");
        
        $("#tabcontainer").removeClass("d-none");
    }
};