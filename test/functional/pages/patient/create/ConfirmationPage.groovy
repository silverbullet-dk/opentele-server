package pages.patient.create

import geb.Page

class ConfirmationPage extends Page {
    static at = {
        $("h1", text: startsWith("Bekræft oprettelse af")).size() == 1
    }

    static content = {

        saveButton { $("input", name: "_eventId_save") }
        saveAndGoToMonitoringPlanButton { $("input", name: "_eventId_saveAndGotoMonplan")}
        cancelButton { $("input", name: "_eventId_quitNoSaving")}
    }
}
