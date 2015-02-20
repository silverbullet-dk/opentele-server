<div>
    ${title}: <g:field type="string" name="${name}" value="${value}" class="${measurement.warningFields.contains(name) ? 'warning' : ''}"/>
</div>
<div name="${name}-error-message" style="display:none">
    ${body()}
</div>
