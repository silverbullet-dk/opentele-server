package pages.patient.create

import geb.Page

class ThresholdsPage extends Page {
    static at = {
        $("h1").text() == "Opret patient: Tærskelværdier"
    }

    static content = {
        createPatientForm { $("form") }
        nextButton { $("input", class: "gonext") }
        doneButton { $("input", name: "_eventId_saveAndShow") }
        saveAndGoToMonitoringPlanButton { $("input", name: "_eventId_saveAndGotoMonplan")}
    }
}
