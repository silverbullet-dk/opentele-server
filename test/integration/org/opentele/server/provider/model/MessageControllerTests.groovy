package org.opentele.server.provider.model

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin
import org.junit.*
import org.opentele.server.core.test.AbstractControllerIntegrationTest
import org.opentele.server.core.util.JSONMarshallerUtil
import org.opentele.server.model.User
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

@TestMixin(IntegrationTestMixin)
class MessageControllerTests extends AbstractControllerIntegrationTest {
	def controller
    def grailsApplication
    
    @Before
	void setUp() {
        // Avoid conflicts with objects in session created earlier. E.g. in bootstrap
        grailsApplication.mainContext.sessionFactory.currentSession.clear()
        
		println "Setting up JSON marshallers"
		JSONMarshallerUtil.registerCustomJSONMarshallers(grailsApplication)
		
		controller = new MessageController()
		controller.response.format = "json"
	}

	void testCreate() {
		def login = "testCreate"
        User.build(username: login, password: login + '1', enabled: true)

        def auth = new UsernamePasswordAuthenticationToken(login,login+'1')
		def authtoken = caseInsensitivePasswordAuthenticationProvider.authenticate(auth)
		SecurityContextHolder.getContext().setAuthentication(authtoken)

		
		def controller = new MessageController()
		def model = controller.create()

		assert model.messageInstance != null
	}
}
