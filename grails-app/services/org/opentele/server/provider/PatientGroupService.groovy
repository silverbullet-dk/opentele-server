package org.opentele.server.provider

import grails.plugin.springsecurity.annotation.Secured
import org.opentele.server.model.Clinician2PatientGroup
import org.opentele.server.model.Patient2PatientGroup
import org.opentele.server.model.PatientGroup
import org.opentele.server.model.StandardThresholdSet
import org.opentele.server.core.model.types.PermissionName
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.transaction.annotation.Transactional

class PatientGroupService {

    @Secured(PermissionName.PATIENT_GROUP_DELETE)
    public boolean deletePatientGroup(Long patientGroupID) {
        return deletePatientGroup(PatientGroup.get(patientGroupID))
    }

    @Secured(PermissionName.PATIENT_GROUP_DELETE)
    @Transactional
    public boolean deletePatientGroup(PatientGroup patientGroup) {
        if (patientGroup == null) {
            throw new IllegalArgumentException("Cannot delete PatientGroup: "+patientGroup)
        }
        try {

            Clinician2PatientGroup.executeUpdate("delete Clinician2PatientGroup c2p where c2p.patientGroup = ?", [patientGroup])
            Patient2PatientGroup.executeUpdate("delete Patient2PatientGroup p2p where p2p.patientGroup = ?", [patientGroup])

            StandardThresholdSet st = patientGroup.standardThresholdSet

            st.patientGroup = null
            st.save(failOnError: true, flush: true)

            PatientGroup.executeUpdate("delete PatientGroup pg where pg = ?", [patientGroup])

            st.delete(failOnError: true, flush: true)

        } catch (DataIntegrityViolationException e) {
            println e.getMessage()
            e.printStackTrace()

            return false
        }
        return true
    }
}
