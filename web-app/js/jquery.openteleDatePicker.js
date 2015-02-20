/*!
 * jQuery lightweight plugin boilerplate
 * Original author: @ajpiano
 * Further changes, comments: @addyosmani
 * Licensed under the MIT license
 */
;
(function ($, window, document, undefined) {

    var pluginName = 'openteleDatePicker',
        defaults = {
            fieldName: undefined,
            nullable: false,
            minDate: undefined,
            maxDate: undefined,
            javascriptFormat: 'mm/dd/yy',
            date: undefined,
            buttonImage: undefined
        };

    function Plugin(element, options) {
        this.element = element;
        this.$element = $(element);

        this.options = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;

        options = this.options;

        function checkRange(max) {
            return function () {
                var self = $(this);
                var value = parseInt(self.val());
                if (options.nullable && (!value || value == '')) {
                    self.data('oldValue', '');
                    return
                }

                if (!value || value < 0 || value > max) {
                    self.val(self.data('oldValue'));
                    self.effect('highlight', {color: 'red'}, "slow");
                } else {
                    var stringVal = "0" + value;
                    if (stringVal.length == 3) stringVal = stringVal.substring(1);
                    self.val(stringVal);
                }
                self.data('oldValue', self.val());
            }
        }

        function getInput(postfix) {
            return $('input[name="' + options.fieldName + '_' + postfix + '"]', this.element);
        }

        function setGrailsDate(date) {
            getInput('day').val(date ? date.getDate() : '');
            getInput('month').val(date ? date.getMonth() + 1 : '');
            getInput('year').val(date ? date.getFullYear() : '');
        }

        function parseDate(dateText) {
            return $.datepicker.parseDate(options.javascriptFormat, dateText);
        }

        function handleDateChanged(event, dateText) {
            var self = $(this);
            var warn = false;
            if (options.nullable && (!dateText || dateText == '')) {
                setGrailsDate(null);
                self.data('previousDate', '');
                return;
            }
            try {
                var date = parseDate(dateText);
                var min = self.datepicker('option', 'minDate');
                var max = self.datepicker('option', 'maxDate');
                if (min && parseDate(min).getTime() > date.getTime()) {
                    date = parseDate(min);
                    warn = true;
                }
                if (max && parseDate(max).getTime() <= date.getTime()) {
                    date = parseDate(max);
                    warn = true;
                }
                setGrailsDate(date);
                self.datepicker('setDate', date);
                self.data('previousDate', date);
            } catch (e) {
                // unparsable
                self.datepicker('setDate', self.data('previousDate'));
                warn = true;
            }
            if (warn) {
                self.effect('highlight', {color: 'red'}, "slow");
            }
        }

        getInput('hour').on('change', checkRange(24)).trigger('change');
        getInput('minute').on('change', checkRange(60)).trigger('change');
        $('button', this.element).on('click',function(event) {
            event.preventDefault();
            getInput('widget').datepicker('show');
        });

        getInput('widget')
            .datepicker({
                dateFormat: this.options.javascriptFormat,
                minDate: this.options.minDate,
                maxDate: this.options.maxDate,
                showOn: 'button',
                buttonImage: options.buttonImage,
                onSelect: function (dateText) {
                    $(this).trigger('dateChanged', dateText);
                }
            })
            .data('previousDate', parseDate(options.date))
            .on('change',function () {
                $(this).trigger('dateChanged', $(this).val());
            }).on('dateChanged', handleDateChanged);

        this.init();
    }

    Plugin.prototype.init = function () {
    };

    $.fn[pluginName] = function (options) {
        return this.each(function () {
            if (!$.data(this, 'plugin_' + pluginName)) {
                $.data(this, 'plugin_' + pluginName,
                    new Plugin(this, options));
            }
        });
    }

})(jQuery, window, document);
