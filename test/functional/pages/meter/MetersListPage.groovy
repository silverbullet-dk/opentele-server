package pages.meter

import pages.ScaffoldPage
import geb.Module
import pages.LogoutPage

class MetersListPage extends ScaffoldPage {
    static url = "meter/list"

    static at = {
        title ==~ /Målerliste/
    }

    static content = {
        meterTable { $("div.content table", 0) }
        meterRow { module MeterRow, meterRows[it] }
        meterRows(required: true) { meterTable.find("tbody").find("tr") }
        createLink (to: pages.meter.CreatePage, required: true) { $("a", text: "Ny Måler") }
    }
}

class MeterRow extends Module {
    static content = {
        cell { $("td", it) }
        cellText { cell(it).text() }
        cellHrefText{ cell(it).find('a').text() }
        active { cellHrefText(0).text == "Ja" }
        ID { cellText(1) }
        Model { cellText(2) }
        Type { cellText(3) }
        Patient { cellText(4) }
        Kit { cellText(5) }
        showLink(to: pages.meter.ShowPage) { cell(0).find("a") }
    }
}



