package org.opentele.server.provider.model
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.opentele.server.core.model.Schedule
import org.opentele.server.model.ScheduleWindow

@TestFor(ScheduleWindowController)
@Mock(ScheduleWindow)
class ScheduleWindowControllerTests {


    def populateValidParams(params) {
        assert params != null
        params["scheduleType"] = Schedule.ScheduleType.UNSCHEDULED
        params["windowSizeMinutes"]= 1
        params
    }

    void testIndex() {
        controller.index()
        assert "/scheduleWindow/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.scheduleWindowInstanceList.size() == 0
        assert model.scheduleWindowInstanceTotal == 0
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/scheduleWindow/list'


        populateValidParams(params)
        def scheduleWindow = new ScheduleWindow(params)

        assert scheduleWindow.save() != null

        params.id = scheduleWindow.id

        def model = controller.show()

        assert model.scheduleWindowInstance == scheduleWindow
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/scheduleWindow/list'


        populateValidParams(params)
        def scheduleWindow = new ScheduleWindow(params)

        assert scheduleWindow.save() != null

        params.id = scheduleWindow.id

        def model = controller.edit()

        assert model.scheduleWindowInstance == scheduleWindow
    }




}
