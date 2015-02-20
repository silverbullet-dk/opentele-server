package org.opentele.builders

import org.opentele.server.model.Patient
import org.opentele.server.model.Threshold
import org.opentele.server.model.User
import org.opentele.server.core.model.types.PatientState
import org.opentele.server.core.model.types.Sex

class PatientBuilder {
    def cpr = '1234567890'
    def firstName = 'Mette'
    def lastName ='Andersen'
    def sex =  Sex.FEMALE
    def address = 'Gade 15'
    def postalCode = '8000'
    def city = 'Aarhus C'
    def mobilePhone =  null
    def phone =  null
    def email =  null
    def thresholds = new ArrayList<Threshold>()
    def state = PatientState.ACTIVE

    def password = "asd"
    def reenteredpassword = "asd"
    def username = "MetteAndersen"

    public Patient build() {

        def result = Patient.build( cpr: cpr,
                                    firstName: firstName,
                                    lastName: lastName,
                                    sex: sex,
                                    address: address,
                                    postalCode: postalCode,
                                    city: city,
                                    mobilePhone: mobilePhone,
                                    phone: phone,
                                    email: email,
                                    thresholds: thresholds,
                                    state: state)

        result.save(failOnError:true)
        result
    }

    PatientBuilder forState(PatientState state) {
        this.state = state

        this
    }

    def buildParams(params) {
        def map = params
        map.cpr = cpr
        map.firstName = firstName
        map.lastName = lastName
        map.sex = sex
        map.address = address
        map.postalCode = postalCode
        map.city = city
        map.mobilePhone = mobilePhone
        map.phone = phone
        map.email = email
        map.thresholds = thresholds
        map.state = state
        map.username = username
        map.password = password
        map.reenteredpassword = reenteredpassword
        map
    }
}
