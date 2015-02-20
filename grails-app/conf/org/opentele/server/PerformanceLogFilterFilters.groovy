package org.opentele.server

import java.util.concurrent.atomic.AtomicLong
import grails.plugin.springsecurity.SpringSecurityUtils
import org.springframework.security.core.context.SecurityContextHolder

class PerformanceLogFilterFilters {
	private static final AtomicLong REQUEST_NUMBER_COUNTER = new AtomicLong()
	private static final String START_TIME_ATTRIBUTE = 'Controller__START_TIME__'
	private static final String REQUEST_NUMBER_ATTRIBUTE = 'Controller__REQUEST_NUMBER__'

	// Inject spring security
	def springSecurityService

	def filters = {
		all(controller:'*', action:'*') {

			before = {
				long start = System.currentTimeMillis()
				long currentRequestNumber = REQUEST_NUMBER_COUNTER.incrementAndGet()

				request[START_TIME_ATTRIBUTE] = start
				request[REQUEST_NUMBER_ATTRIBUTE] = currentRequestNumber

				return true
			}

			after = { Map model ->

			}

			afterView = { Exception e ->
                def user = SecurityContextHolder.context.authentication?.name

                long currentRequestNumber = REQUEST_NUMBER_COUNTER.incrementAndGet()

                long start = request[START_TIME_ATTRIBUTE]
				long end = System.currentTimeMillis()
				long requestNumber = request[REQUEST_NUMBER_ATTRIBUTE]

				def msg = "User: $user completed request #$requestNumber: " +
						"start ${new Date(start)} end ${new Date()}, total time ${end - start}ms. " +
                        "'$request.servletPath'/'$request.forwardURI', " +
                        "from $request.remoteHost ($request.remoteAddr) " +
                        " at ${new Date(start)}, Ajax: $request.xhr, controller: $controllerName, " +
                        "action: $actionName"
                if (e) {
					log.debug "$msg \n\texception: $e.message", e
				}
				else {
					log.debug msg
				}
			}
		}
	}
}




