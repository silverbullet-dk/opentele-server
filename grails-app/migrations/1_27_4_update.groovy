databaseChangeLog = {

    changeSet(author: "hra", id: "serverity ordinal in patient overview") {
        addColumn(tableName: "patient_overview") {
            column(name: "questionnaire_severity_ordinal", type: '${integer.type}')
        }
    }
}