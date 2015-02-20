package pages.patient.create

import geb.Page

class CommentsPage extends Page {
    static at = {
        $("h1").text() == "Opret patient: Kommentarer til patient"
    }

    static content = {
        createPatientForm { $("form") }
        nextButton { $("input", class: "gonext") }
        doneButton { $("input", name: "_eventId_saveAndShow") }
        saveAndGoToMonitoringPlanButton { $("input", name: "_eventId_saveAndGotoMonplan")}
    }
}
