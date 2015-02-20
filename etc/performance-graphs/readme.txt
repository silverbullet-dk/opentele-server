I denne folder ligger filer til udtræk og behandlings af OpenTele svartidsdata.

Dataudtræk hentes fra OpenTeles database (MSSQL) med SQL:

select ale.duration, en.action_name, en.controller_name, ale.calling_ip, 
ale.created_date, ale.exception, ale.start_date, ale.start_time, 
ale.end_date, ale.end_time, ale.operation, ale.success 
from audit_log_entry ale
inner join audit_log_controller_entity en on en.id = ale.action_id
where ale.created_date >= DATEADD(day,-30, GETDATE()) -- Sidste 30 dage
and ale.duration is not null
order by ale.created_date;

Tidsintervallet kan justeres ved at korrigere parameteren til DATEADD funktionen.

Ovenstående SQL udtræk kan f.eks. eksekveres i SQL Management Center, og derfra eksporteres til CSV.

Indholdet i CSV filen er samtlige kald til samtlige actions på samtlige controllers i OpenTele.

CSV filen kan splittes op, i en række mindre filer, for hver enkelt controller->action kombination. Opsplitningen kan f.eks. ske med kommandoen:

awk -F ";" '{f="splits/opentele_"$3"_"$2".csv"; print $0 >> f;close(f)}' input.csv


Efterfølgende kan man lave fine d3 grafer, ved at tilpasse index.html til at pege på csv-filen for den controller.action man er interesseret i at visualisere svartiderne for. Se efter linien med:

"dsv("splits/opentele_patientOverview_index.csv", function(error, data)"
 
Load index.html i en browser, og vupti: grafer :-)

OBS: I nuværende version skal csv-filerne indeholde en header-række med følgende indhold:

Duration;Action;Controller;IP;Created;Exception;StartDate;StartTime;EndDate;EndTime;Operation;Success
