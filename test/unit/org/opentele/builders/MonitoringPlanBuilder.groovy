package org.opentele.builders
import org.opentele.server.model.Patient
import org.opentele.server.model.MonitoringPlan

class MonitoringPlanBuilder {
    private Patient patient
    private Date startDate = new Date()

    MonitoringPlanBuilder forPatient(Patient patient) {
        this.patient = patient
        this
    }

    MonitoringPlanBuilder forStartDate(Date startDate) {
        this.startDate = startDate
        this
    }

    MonitoringPlan build() {
        if (!patient) {
            patient = new PatientBuilder().build()
        }

        MonitoringPlan monitoringPlan = MonitoringPlan.build(patient: patient, startDate: startDate)
        monitoringPlan.save(failOnError: true)

        //Is it really necessary to wire both ends of the relation?
        patient.monitoringPlan = monitoringPlan
        patient.save(failOnError: true)

        monitoringPlan
    }

}
