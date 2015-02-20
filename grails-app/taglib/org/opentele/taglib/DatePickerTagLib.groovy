package org.opentele.taglib

class DatePickerTagLib {
    static namespace = "jq"

    def datePicker = { attrs ->
        String name = attrs.name
        String id = attrs.id ?: name
        Date date = attrs.value
        boolean nullable = attrs.noSelection
        if(!nullable && !date) {
            date = new Date()
        }
        String precision = attrs.precision ?: 'day'
        if (!(precision in ['day', 'minute'])) {
            throwTagError("jq.datePicker only supports precision 'day' and 'minute'. Implement the rest, if you need it")
        }
        boolean showTime = (precision == 'minute')
        IntRange years = attrs.years

        String format = attrs.format ?: message(code: 'default.date.format.notime')
        String javascriptFormat = format.toLowerCase().replaceAll(/yy/,'y') // Javascript long year is yy

        String dateAsText = date ? date.format(format) : ''

        def model = [
                name: name, id: id,
                date: date,
                dateAsText: dateAsText,
                showTime: showTime,
                nullable: nullable,
                format: format,
                javascriptFormat: javascriptFormat
        ]
        if(years) {
            model << calculateMinAndMax(new Date().clearTime(), years, format)
        }
        out << g.render(template: "/layouts/templates/datePicker", model: model)
    }

    private calculateMinAndMax(Date reference, IntRange years, String format) {
        Date minDate = reference.clone()
        minDate[Calendar.YEAR] = years.fromInt
        Date maxDate = reference.clone()
        maxDate[Calendar.YEAR] = years.toInt
        [minDate: minDate.format(format),maxDate: maxDate.format(format)]
    }

}
