package pages.meter

import pages.ScaffoldPage

class CreatePage extends ScaffoldPage {
    static url = "meter/create"

    static at = {
        title ==~ /Opret Måler/
    }

    static content = {
        createLink (to: pages.meter.ShowPage, required: true) { $("#create") }
        form{$("form")}

        meterType { $("#meterType")}
    }
}




