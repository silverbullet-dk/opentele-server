package org.opentele.server.provider.model

import org.opentele.server.model.Meter

import grails.test.mixin.*

@TestFor(MeterController)
@Mock(Meter)
class MeterControllerTests {

    def populateValidParams(params) {
        assert params != null
    }

    void testIndex() {
        controller.index()

        assert "/meter/list" == response.redirectedUrl
    }

    void testList() {
        def model = controller.list()

        assert model.meterInstanceList.size() == 0
        assert model.meterInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.meterInstance != null
    }

    void testSave() {
        request.method = 'POST'
        controller.save()

        assert model.meterInstance != null
        assert view == '/meter/create'
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/meter/list'
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/meter/list'
    }

    void testUpdate() {
        request.method = 'POST'
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/meter/list'
    }

    void testDelete() {
        request.method = 'POST'
        controller.delete()

        assert flash.message != null
        assert response.redirectedUrl == '/meter/list'
    }
}
