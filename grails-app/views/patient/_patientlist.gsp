<div id="list-patient" class="content scaffold-list" role="main">
    <h1>
        <g:message code="patient.list.label" />
    </h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">
            ${flash.message}
        </div>
    </g:if>
    <table>
        <thead>
        <tr>
            <!--  Rows
           		 Situation, CPR nummer, Fornavn, Efternavn, Seneste spørgeskema, Type, Kvitteret
         		 -->
            <g:sortableColumn property="severity" title="${message(code: 'patient.situation.label')}" params="${params}"/>
            <g:sortableColumn property="firstName" title="${message(code: 'patient.firstNames.label')}" params="${params}"/>
            <g:sortableColumn property="lastName" title="${message(code: 'patient.lastName.label')}" params="${params}"/>
            <g:sortableColumn property="cpr" title="${message(code: 'patient.cpr.label', default: 'Cpr')}" params="${params}"/>

            <th>${message(code: 'patient.latestQuestionnaire.label')}</th>
            <th>${message(code: 'patient.group.label')}</th>

            <g:sortableColumn property="state" title="${message(code: 'patient.status')}" params="${params}"/>
        </tr>
        </thead>
        <tbody>
        <g:each in="${patients}" status="i" var="patientInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                <td>
                    <patientList:showIcon patient="${patientInstance}"/>
                </td>
                <td>
                    <g:link action="questionnaires" id="${patientInstance.id}"
                            data-tooltip="${message(code: 'patientOverview.goToPatient.tooltip')}">
                        ${fieldValue(bean: patientInstance, field: "firstName")}
                    </g:link>
                </td>
                <td>
                    <g:link action="questionnaires" id="${patientInstance.id}"
                            data-tooltip="${message(code: 'patientOverview.goToPatient.tooltip')}">
                        ${fieldValue(bean: patientInstance, field: "lastName")}
                    </g:link>
                </td>
                <td>
                    <g:link action="questionnaires" id="${patientInstance.id}"
                            data-tooltip="${message(code: 'patientOverview.goToPatient.tooltip')}">
                        ${patientInstance.formattedCpr}
                    </g:link>
                </td>
                <td>${formatDate(date: patientInstance.latestQuestionnaireUploadDate)}</td>
                <td>
                    <ul class="table">
                        <g:each in="${patientInstance.groups}" var="group">
                            <li>${group.name.encodeAsHTML()}</li>
                        </g:each>
                    </ul>
                </td>
                <td>
                    <g:message code="enum.patientstate.${patientInstance.stateWithPassiveIntervals}"/>
                </td>
            </tr>
        </g:each>
        </tbody>
    </table>
</div>
