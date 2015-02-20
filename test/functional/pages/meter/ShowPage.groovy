package pages.meter

import pages.ScaffoldPage

class ShowPage extends ScaffoldPage {
    static url = "meter/show"

    static at = {
        title ==~ /Vis Måler/
    }

    static content = {
        row { $("li.fieldcontain span.property-label", text: it).parent() }
        value { row(it).find("span.property-value").text() }
        active { Boolean.valueOf(value("Aktiv")) }
        id { value("ID") }
        model { value("Model") }
        type { value("Type") }
        patient { value("Patient") }
        kit { value("Kit") }
    }
}




