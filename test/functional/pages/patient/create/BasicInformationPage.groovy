package pages.patient.create

import geb.Page

class BasicInformationPage extends Page {
    static at = { title == "Opret patient: Stamdata" }

    static content = {
        createPatientForm { $("form") }
        nextButton { $("input", class: "gonext") }
    }
}
