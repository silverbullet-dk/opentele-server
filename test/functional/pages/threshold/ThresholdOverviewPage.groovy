package pages.threshold

import geb.Page

class ThresholdOverviewPage extends Page {
    static url = "standardThresholdSet/list"
    static at = { title == "Vis standardtærskelværdier" }
    static content = {
        patientgroupThresholdSets {$('.standardThresholdSet')}
        patientgroupThresholdSet { i -> patientgroupThresholdSets(i)}
        addThressholdToPatientgroup { i -> patientgroupThresholdSets(i).find(name: "chooseThreshold")}
        deleteAllThresholdsFromPatientgroup { i -> patientgroupThresholdSets(i).find(name: "_action_delete")}

        thresholdType { thresholdSetIndex, thresholdText -> patientgroupThresholdSet(thresholdSetIndex).find('td', text: thresholdText) }

        thresholdAlertHigh { thresholdSetIndex, thresholdIndex -> patientgroupThresholdSet(thresholdSetIndex).find('tr')[thresholdIndex].find('td')[1].text() }
        thresholdWarningHigh { thresholdSetIndex, thresholdIndex -> patientgroupThresholdSet(thresholdSetIndex).find('tr')[thresholdIndex].find('td')[2].text() }
        thresholdWarningLow { thresholdSetIndex, thresholdIndex -> patientgroupThresholdSet(thresholdSetIndex).find('tr')[thresholdIndex].find('td')[3].text() }
        thresholdAlertLow { thresholdSetIndex, thresholdIndex -> patientgroupThresholdSet(thresholdSetIndex).find('tr')[thresholdIndex].find('td')[4].text() }
    }
}
