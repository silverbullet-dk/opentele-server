package pages.patient.create

import geb.Page

class UserInformationPage extends Page {
    static at = {
        $("h1").text() == "Opret patient: Brugerdata"
    }

    static content = {
        createPatientForm { $("form") }
        nextButton { $("input", class: "gonext") }
    }
}
