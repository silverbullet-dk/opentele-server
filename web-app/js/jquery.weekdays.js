;
(function ($, window, document, undefined) {

    var pluginName = "weekdays",
        defaults = {
            allNoneId: 'allNone',
            name: undefined


        };

    // The actual plugin constructor
    function Plugin(element, options) {
        this.element = element;
        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this.checkboxes = $('input[name="' + this.settings.name + '"]', this.element);
        this.checkAll = $('#' + this.settings.allNoneId, this.element);
        this.init();
    }

    Plugin.prototype = {
        init: function () {
            var self = this;
            this.checkAll.on('click', function () {
                var checked = $(this).prop('checked');
                self.checkboxes.each(function () {
                    $(this).prop('checked', checked);
                });
            });
            this.checkboxes.on('click', this.checkAllNone());
            this.checkAllNone()();
        },
        checkAllNone: function () {
            var self = this;
            return function () {

                var all = _.every(self.checkboxes, function (e) {
                    return $(e).prop('checked');
                });
                self.checkAll.prop('checked', all);
            }

        }
    };

    $.fn[ pluginName ] = function (options) {
        return this.each(function () {
            if (!$.data(this, "plugin_" + pluginName)) {
                $.data(this, "plugin_" + pluginName, new Plugin(this, options));
            }
        });
    };

})(jQuery, window, document);
