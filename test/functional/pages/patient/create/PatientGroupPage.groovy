package pages.patient.create

import geb.Page

class PatientGroupPage extends Page {
    static at = { title == "Opret patient: Patientgruppe" }

    static content = {
        createPatientForm { $("form") }
        nextButton { $("input", class: "gonext") }
        doneButton { $("input", name: "_eventId_saveAndShow") }
        saveAndGoToMonitoringPlanButton { $("input", name: "_eventId_saveAndGotoMonplan")}
    }
}
