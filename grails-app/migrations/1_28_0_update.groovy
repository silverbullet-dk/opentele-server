databaseChangeLog = {

    changeSet(author: 'km', id: 'add_user_preference') {
        createTable(tableName: 'user_preference') {
            column(autoIncrement: 'true', name: 'id', type: '${id.type}') {
                constraints(nullable: 'false', primaryKey: 'true', primaryKeyName: 'user_preference_PK')
            }

            column(name: "clinician_id", type: '${id.type}') {
                constraints(nullable: "false")
            }

            column(name: "preference", type: '${text.type}')
            column(name: "value", type: '${text.type}')

            column(name: "version", type: '${id.type}') { constraints(nullable: "false") }
            column(name: "created_by", type: '${string.type}')
            column(name: "created_date", type: '${datetime.type}')
            column(name: "modified_by", type: '${string.type}')
            column(name: "modified_date", type: '${datetime.type}')
        }
    }
}
