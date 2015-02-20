import grails.util.Environment

class UrlMappings {

	static mappings = {
		"/login/$action?"(controller: "login", namespace: "org.opentele.server.core")
		"/logout/$action?"(controller: "logout", namespace: "org.opentele.server.core")

		// Default one
		"/$controller/$action?/$id?"{
            constraints {
            }
        }
		name patientMeasurements: "/patient/$patientId/measurements" {
            controller = "measurement"
            action= "patientMeasurements"
        }
		name patientGraphs: "/patient/$patientId/graphs" {
            controller = "measurement"
            action = "patientGraphs"
        }
		name patientMeasurementGraph: "/patient/$patientId/measurements/$measurementType/graph" {
            controller = "measurement"
            action:"graph"
        }
        name patientMeasurementBloodsugar: "/patient/$patientId/measurements/bloodsugar" {
            controller = "measurement"
            action = "bloodsugar"
        }

        //For the meta controller
        "/currentVersion"(controller: "meta", action: "currentServerVersion")
        "/isAlive"(controller: "meta", action: "isAlive")
        "/isAlive/html"(controller: "meta", action: "isAlive")
        "/isAlive/json"(controller: "meta", action: "isAliveJSON")
        "/noAccess"(controller: "meta", action: "noAccess")

        "/"(controller: "home", action:"index")

        "500"(view:"${(Environment.current == Environment.DEVELOPMENT) ? '/error' : '/productionError'}")

//        "404"(controller: 'error', action:'index')
//        "500"(view:''/productionError'')
	}
}
