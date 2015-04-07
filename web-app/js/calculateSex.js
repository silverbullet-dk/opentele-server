function calculateSex(idSelector, resultSelector) {
    var identification = $(idSelector).val();
    $.ajax("${g.createLink(action: "patientSex", controller: "patient")}", {data: {identification: identification}, cache: false}) // IE will cache the result unless we force cache-disabling
        .done(function(data) {
            if (data.sex === 'UNKNOWN') {
                return;
            }
            $(resultSelector).attr('value', data.sex);
        });
}