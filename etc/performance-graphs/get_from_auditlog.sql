select ale.duration, en.action_name, en.controller_name, ale.calling_ip, 
ale.created_date, ale.exception, ale.start_date, ale.start_time, 
ale.end_date, ale.end_time, ale.operation, ale.success 
from audit_log_entry ale
inner join audit_log_controller_entity en on en.id = ale.action_id
where ale.created_date >= DATEADD(day,-30, GETDATE()) -- Sidste 30 dage
and ale.duration is not null
order by ale.created_date;
