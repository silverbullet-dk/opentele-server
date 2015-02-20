package pages.patient.create

import geb.Page

class AddPatientRelationsPage extends Page {
    static at = {
        $("h1").text() == "Opret patient: Tilføj pårørende"
    }

    static content = {
        addRelationButton { $("input", name: "_eventId_create") }
        nextButton { $("input", class: "gonext") }
        doneButton { $("input", name: "_eventId_saveAndShow") }
        saveAndGoToMonitoringPlanButton { $("input", name: "_eventId_saveAndGotoMonplan")}
    }
}
