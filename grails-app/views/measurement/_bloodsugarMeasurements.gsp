<td>
    <table>
        <tr>
            <g:each in="${measurements}" var="measurement">
                <td class="measurement ${measurement.beforeMeal ? 'before_meal' : (measurement.afterMeal ? 'after_meal' : 'unknown_meal')}"
                    data-time="${measurement.timestamp.format("dd/MM/yyyy HH:mm")}"
                    data-value="${measurement.value()}"
                    data-before="${measurement.beforeMeal}"
                    data-after="${measurement.afterMeal}"
                    data-control="${measurement.controlMeasurement}"
                    data-other="${measurement.otherInformation}"
                >
                    <g:if test="${measurement.beforeMeal}">
                        <g:img file="beforeMeal.png" />
                        <br />
                    </g:if>
                    <g:elseif test="${measurement.afterMeal}">
                        <g:img file="afterMeal.png" />
                        <br />
                    </g:elseif>
                    ${measurement.value()}
                </td>
            </g:each>
        </tr>
    </table>
</td>
