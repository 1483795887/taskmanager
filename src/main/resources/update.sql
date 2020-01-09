alter table event
  add column current_progress int;
alter table event
  add column last_modified_date date;

insert into event (name, current_progress, target_progress, start_date, event.last_modified_date, running, type)
select name, current_progress, target_progress, last_modified_date, start_date, true, 0
from task;

insert into progress (eid, progress, date)
select id, 0, start_date
from event;

insert into progress (eid, progress, date)
select id, current_progress, last_modified_date
from event
on duplicate key update progress = event.current_progress;

drop table if exists task;

alter table event
  drop column current_progress;
alter table event
  drop column last_modified_date;

insert into event (name, target_progress, start_date, running, type)
select title, 1, date, false, 1
from diary
where title <> 'card';

insert into progress (eid, progress, date)
select id, target_progress, start_date
from event
where running = false;

insert into achievement (eid, date)
select id, start_date
from event
where running = false;

drop table diary;