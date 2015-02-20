package pages.landing

import pages.ScaffoldPage

class LandingPage extends ScaffoldPage {
    static url = "/"

    static at = {
        title ==~ /Person List/
    }

    static content = {
        newPersonButton(to: CreatePage) { $("a", text: "New Person") }
        peopleTable { $("div.content table", 0) }
        personRow { module PersonRow, personRows[it] }
        personRows(required: false) { peopleTable.find("tbody").find("tr") }
    }
}
