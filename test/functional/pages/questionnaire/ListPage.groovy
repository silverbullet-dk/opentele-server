package pages.questionnaire

import pages.ScaffoldPage
import geb.Module

class ListPage extends ScaffoldPage {
    static url = "questionnaire/list"

    static at = {
        title ==~ /Spørgeskemaliste/
    }

    static content = {
        questionnaireTable { $("div.content table", 0) }
        questionnaireRow { module QuestionnaireRow, questionnaireRows[it] }
        questionnaireRows(required: false) { questionnaireTable.find("tbody").find("tr") }
    }
}

class QuestionnaireRow extends Module {
    static content = {
        cell { $("td", it) }
        cellText { cell(it).text() }
        cellHrefText{ cell(it).find('a').text() }
        enabled { Boolean.valueOf(cellHrefText(0)) }
        firstName { cellText(1) }
        lastName { cellText(2) }
        showLink(to: ShowPage) { cell(0).find("a") }
    }
}
