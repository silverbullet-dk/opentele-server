<g:if test="${bloodSugarData.empty}">
    <h1 class="information"><g:message code="patient.bloodSugarTable.noMeasurements"/></h1>
</g:if>
<g:else>
    <table id="bloodsugartable">
        <thead>
        <th><g:message code="patient.bloodSugarTable.date"/></th>
        <th>00.00-04.59</th>
        <th>05.00-10.59</th>
        <th>11.00-16.59</th>
        <th>17.00-23.59</th>
        </thead>
        <tbody>
        <g:each in="${bloodSugarData}" status="i" var="bloodsugarDataOnDate">
            <tr>
                <td>
                    ${bloodsugarDataOnDate.date.format("dd/MM/yyyy")}
                </td>
                <g:render template="/measurement/bloodsugarMeasurements" bean="${bloodsugarDataOnDate.zeroToFour}" var="measurements"/>
                <g:render template="/measurement/bloodsugarMeasurements" bean="${bloodsugarDataOnDate.fiveToTen}" var="measurements"/>
                <g:render template="/measurement/bloodsugarMeasurements" bean="${bloodsugarDataOnDate.elevenToSixteen}" var="measurements"/>
                <g:render template="/measurement/bloodsugarMeasurements" bean="${bloodsugarDataOnDate.seventeenToTwentyThree}" var="measurements"/>
            </tr>
        </g:each>
        </tbody>
    </table>
    <div id="bloodsugardescription">
        <g:img file="beforeMeal.png" />
        <g:message code="patient.overview.bloodsugar.beforemeal"/>
        <br />
        <g:img file="afterMeal.png" />
        <g:message code="patient.overview.bloodsugar.aftermeal"/>
    </div>
</g:else>
