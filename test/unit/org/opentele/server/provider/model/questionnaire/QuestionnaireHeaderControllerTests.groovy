package org.opentele.server.provider.model.questionnaire
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.junit.Ignore
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.provider.model.questionnaire.QuestionnaireHeaderController

@TestFor(QuestionnaireHeaderController)
@Mock(QuestionnaireHeader)
class QuestionnaireHeaderControllerTests {


    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/questionnaireHeader/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.questionnaireHeaderInstanceList.size() == 0
        assert model.questionnaireHeaderInstanceTotal == 0
    }

    @Ignore
    void testCreate() {
        def model = controller.create()

        assert model.questionnaireHeaderInstance != null
    }

    @Ignore
    void testSave() {
        controller.save()

        assert model.questionnaireHeaderInstance != null
        assert view == '/questionnaireHeader/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/questionnaireHeader/show/1'
        assert controller.flash.message != null
        assert QuestionnaireHeader.count() == 1
    }

    @Ignore
    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/questionnaireHeader/list'


        populateValidParams(params)
        def questionnaireHeader = new QuestionnaireHeader(params)

        assert questionnaireHeader.save() != null

        params.id = questionnaireHeader.id

        def model = controller.show()

        assert model.questionnaireHeaderInstance == questionnaireHeader
    }

    @Ignore
    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/questionnaireHeader/list'


        populateValidParams(params)
        def questionnaireHeader = new QuestionnaireHeader(params)

        assert questionnaireHeader.save() != null

        params.id = questionnaireHeader.id

        def model = controller.edit()

        assert model.questionnaireHeaderInstance == questionnaireHeader
    }

    @Ignore
    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/questionnaireHeader/list'

        response.reset()


        populateValidParams(params)
        def questionnaireHeader = new QuestionnaireHeader(params)

        assert questionnaireHeader.save() != null

        // test invalid parameters in update
        params.id = questionnaireHeader.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/questionnaireHeader/edit"
        assert model.questionnaireHeaderInstance != null

        questionnaireHeader.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/questionnaireHeader/show/$questionnaireHeader.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        questionnaireHeader.clearErrors()

        populateValidParams(params)
        params.id = questionnaireHeader.id
        params.version = -1
        controller.update()

        assert view == "/questionnaireHeader/edit"
        assert model.questionnaireHeaderInstance != null
        assert model.questionnaireHeaderInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    @Ignore
    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/questionnaireHeader/list'

        response.reset()

        populateValidParams(params)
        def questionnaireHeader = new QuestionnaireHeader(params)

        assert questionnaireHeader.save() != null
        assert QuestionnaireHeader.count() == 1

        params.id = questionnaireHeader.id

        controller.delete()

        assert QuestionnaireHeader.count() == 0
        assert QuestionnaireHeader.get(questionnaireHeader.id) == null
        assert response.redirectedUrl == '/questionnaireHeader/list'
    }
}
