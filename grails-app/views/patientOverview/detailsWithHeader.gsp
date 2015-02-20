<div class="patientEntry" id="${patientOverview.patientId}">
    <div class="questionnaireList" id="${patientOverview.patientId}">
        <cq:renderOverviewForPatient patientOverview="${patientOverview}" patientNotes="${patientNotes}" messagingEnabled="${messagingEnabled}"/>
        <div class="questionnaireListInner">
            <tmpl:details/>
        </div>
    </div>
</div>
