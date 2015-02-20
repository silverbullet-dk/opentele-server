package pages.questionnaireHeader
import geb.Module
import pages.ScaffoldPage
import pages.meter.ShowPage

class QuestionnaireHeaderListPage extends ScaffoldPage {
    static url = "questionnaireHeader/list"

    static at = {
        title ==~ /Spørgeskema Liste/
    }

    static content = {
        questionnaireTable { $("div.content table", 0) }
        questionnaireRow { module QuestionnaireHeaderRow, questionnaireRows[it] }
        questionnaireRows(required: false) { questionnaireTable.find("tbody").find("tr") }
        createLink(required: false) { $('a', text: 'Opret Spørgeskema')}
    }
}

class QuestionnaireHeaderRow extends Module {
    static content = {
        cell { $("td", it) }
        cellText { cell(it).text() }
        cellHrefText{ cell(it).find('a').text() }
        enabled { Boolean.valueOf(cellHrefText(0)) }
        name { cellText(0) }
        revision { cellText(1) }
        createdDate { cellText(2) }
        showLink(to: ShowPage) { cell(0).find("a") }
    }
}
