package org.opentele.taglib

/* Format threshold values to 1 decimal place in Danish locale. */
class ThresholdTagLib {

    Locale locale = new Locale("da", "DK")

    def formatThreshold = { attrs, body ->

        String propertyName = getPropertyName(attrs.field, attrs.level)

        def value = attrs.threshold."${propertyName}"

        if (value != null && value != "") {

            if (value.getClass() == Float) {
                value = String.format(locale, "%.1f", value)
            }
            out << value
        } else {
            out << '-'
        }
    }

    private String getPropertyName(String field, String level) {
        if (field) {
            return field + level
        } else {
            return level[0].toLowerCase() + level.substring(1)
        }
    }
}
