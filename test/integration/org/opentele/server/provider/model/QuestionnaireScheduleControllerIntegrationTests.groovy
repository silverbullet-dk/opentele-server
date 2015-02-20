package org.opentele.server.provider.model

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin
import org.junit.Test
import org.opentele.server.provider.constants.Constants
import org.opentele.server.core.test.AbstractControllerIntegrationTest
import org.opentele.server.model.MonitoringPlan
import org.opentele.server.model.Patient
import org.opentele.server.model.QuestionnaireSchedule
import org.opentele.server.provider.model.questionnaire.QuestionnaireScheduleController

@TestMixin(IntegrationTestMixin)
class QuestionnaireScheduleControllerIntegrationTests extends AbstractControllerIntegrationTest {
    def grailsApplication
    def questionnaireService

    void setUp() {
        // Avoid conflicts with objects in session created earlier. E.g. in bootstrap
        grailsApplication.mainContext.sessionFactory.currentSession.clear()
        
        authenticate 'HelleAndersen','HelleAndersen1'
    }

    @Test
    void testCompletedQuestionnaireIsShownAfterScheduleIsDeleted() {
        def scheduleController = new QuestionnaireScheduleController()

        //For this test, assume NancyAnn exists and have data
        Patient patient = Patient.findByFirstName("Nancy Ann")
        assert patient != null

        //Get completed questionnaires for NancyAnn
        def completedBeforeDelete = questionnaireService.extractCompletedQuestionnaireWithAnswers(patient.id)
        assert completedBeforeDelete.results.size() > 0

        //Find and remove a questionnaire from the monPlan
        MonitoringPlan plan = MonitoringPlan.findByPatient(patient)
        QuestionnaireSchedule toDelete = QuestionnaireSchedule.findAllByMonitoringPlan(plan).get(0)
        assert toDelete != null

        scheduleController.session[Constants.SESSION_PATIENT_ID] = patient.id
        scheduleController.params.id = toDelete.id
        scheduleController.del()

        //Check delete went well
        assert scheduleController.response.redirectedUrl == "/monitoringPlan/show/"+patient.id
        assert scheduleController.flash.message != null
        assert scheduleController.flash.message == "Questionnaire Schedule deleted"

        //Check we have the same number of results as before delete
        def completedAfterDelete = questionnaireService.extractCompletedQuestionnaireWithAnswers(patient.id)
        assert completedAfterDelete.results.size() > 0
        assert completedBeforeDelete.results.size() == completedAfterDelete.results.size()
        assert completedBeforeDelete.questions.size() == completedAfterDelete.questions.size()
        assert completedBeforeDelete.columnHeaders.size() == completedAfterDelete.columnHeaders.size()
    }
}
