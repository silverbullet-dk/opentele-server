package pages.patient

import pages.ScaffoldPage

class QuestionnairesPage extends ScaffoldPage {
    static url = "patient/overview"

    static at = {
        title ==~ /Nancy Ann Berggren/
    }

    static content = {
        messageLink { $("a", text: "Beskeder") }
        completedQuestionnariesLink { $("a", text: "Vis egne besvarede skemaer") }
    }
}
