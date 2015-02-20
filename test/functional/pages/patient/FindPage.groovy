package pages.patient

import pages.ScaffoldPage

class FindPage extends ScaffoldPage {
    static url = "patient/search"

    static at = {
		title == "Find patient"
    }

    static content = {
        searchForm { $("form")}
        resetFieldsButton { $("input", name: "_action_resetSearch") }
        findPatientButton { $("input", name: "_action_doSearch") }
        results { $("div#list-patient >* tr")}
        firstResultCPR {results[1].find("td")[3].text()}
    }
}
