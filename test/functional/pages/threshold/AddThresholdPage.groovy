package pages.threshold

import geb.Page

class AddThresholdPage extends Page {
    static url = "standardThresholdSet/addThreshold"
    static at = { title == "Opret standardtærskelværdier" }
    static content = {
        form {$('form')}
        thresholdFields(wait: true) {$('#thresholdFields')}
        alertHigh(wait: true) {thresholdFields.find(id: 'alertHigh')}
        warningHigh(wait: true) {thresholdFields.find(id: 'warningHigh')}
        warningLow(wait: true) {thresholdFields.find(id: 'warningLow')}
        alertLow(wait: true) {thresholdFields.find(id: 'alertLow')}

        saveButton { form.find(id: 'saveThreshold') }
    }
}
