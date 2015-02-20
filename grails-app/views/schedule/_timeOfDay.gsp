<div class="timeOfDay">
     <g:field name="${name}.hour" type="number"
              value="${value?.hour?.toString()?.padLeft(2, '0')}" size="2" min="0" max="23"
              class="twoCharacterInput"
              data-tooltip="${message(code: 'tooltip.patient.questionnaireSchedule.create.hours')}"/>
     :
     <g:field name="${name}.minute" type="number"
              value="${value?.minute?.toString()?.padLeft(2, '0')}" size="2" min="0" max="59"
              class="twoCharacterInput"
              data-tooltip="${message(code: 'tooltip.patient.questionnaireSchedule.create.minutes')}"/>
    <%
        if(body) {
            out <<  body()
        }
    %>
</div>
