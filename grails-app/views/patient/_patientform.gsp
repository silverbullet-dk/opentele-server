<%@ page import="org.opentele.server.core.model.types.PatientState" %>
<%@ page import="org.opentele.server.core.command.PatientSearchCommand" %>

<div id="filter-box" class="filter-box">
    <table>
        <tbody>
        <tr>
            <td><g:message code="patient.overview.form.SSN"/></td>
            <td><g:textField name="ssn" value="${searchCommand.ssn}"/></td>
            <td data-tooltip="${message(code: 'tooltip.patient.search.phone')}">
                <g:message code="patient.overview.form.phone" default="Telefonnummer"/>
            </td>
            <td><g:textField name="phone" value="${searchCommand.phone}"/></td>
        </tr>
        <tr>
            <td><g:message code="patient.overview.form.firstname"/></td>
            <td><g:textField name="firstName" value="${searchCommand.firstName}"/></td>
            <td><g:message code="patient.overview.form.lastname"/></td>
            <td><g:textField name="lastName" value="${searchCommand.lastName}"/></td>
        </tr>
        <tr>
            <td data-tooltip="${message(code: 'tooltip.patient.search.state')}">
                <g:message code="patient.overview.form.status"/>
            </td>
            <td>
                <g:select name="status" from="${PatientState.valuesForPersisting}" valueMessagePrefix="enum.patientstate"
                          value="${searchCommand.status}"
                          noSelection="${['': message(code: "patient.search.state.all")]}"/>
            </td>
            <td><g:message code="patient.overview.form.username"/></td>
            <td><g:textField name="username" value="${searchCommand.username}"/></td>
        </tr>
        <tr>
            <td data-tooltip="${message(code: 'tooltip.patient.search.patientgroup')}">
                <g:message code="patient.overview.form.type"/>
            </td>
            <td>
                <g:select class="patientGroup" name="patientGroup.id" from="${clinicianPatientGroups}"
                          value="${searchCommand.patientGroup?.id}" noSelection="${[null: "..."]}" optionKey="id"/>
            </td>
        </tr>
        <tr>
            <td class="buttons" align="center" colspan="6">
                <g:actionSubmit action="doSearch"
                                value="${message(code: "patient.search.form.submit")}"
                                class="search" data-tooltip="${message(code: 'tooltip.patient.search.limit')}"/>
                <g:actionSubmit action="resetSearch" value="${message(code: 'patient.search.form.reset')}"/>
            </td>
        </tr>
        </tbody>
    </table>
</div>
