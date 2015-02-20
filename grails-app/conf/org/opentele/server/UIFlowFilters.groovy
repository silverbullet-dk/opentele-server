package org.opentele.server

public class UIFlowFilters {
	
	def filters = {
		/*
		 * Annotates the session object with the controller/action the user was coming from.
		 * This includes actions such as save() that uses redirect(...).
		 * 
		 * Ignores page refreshes. 
		 */
		oneStepHistoryFilter(controller:'*', action:'*') {
			before = {
                if(request.xhr) {
                    log.debug "Ajax request"
                } else if ((session.currentAction != actionName) || (session.currentController != controllerName)) {
					if (params?.ignoreNavigation) {
						return
					}
					
					session.lastAction = session.currentAction ? session.currentAction : ''
					session.lastController = session.currentController ? session.currentController : '/'
					session.lastParams = session.currentParams ? session.currentParams : []
					
					session.currentController = controllerName
					session.currentAction = actionName
					session.currentParams = params
					
					log.debug "Last page was: /${session.lastController}/${session.lastAction}"
				} else {
					log.debug "Refresh - last page not updated!"
				}
			}
		}
	}
}
