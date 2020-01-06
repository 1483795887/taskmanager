create table temptask (
  id                 int primary key auto_increment,
  current_progress   int,
  target_progress    int,
  start_date         date,
  last_modified_date date
);

insert into temptask (current_progress, target_progress, start_date, last_modified_date)
SELECT current_progress, target_progress, start_date, last_modified_date
from task; #这里的id与event的id一一对应

insert into event (name, target_progress, start_date, is_closed, is_finished, type)
select name, target_progress, start_date, false, false, 0
from task;

insert into progress (eid, progress, date)
select id, 0, start_date
from temptask;

insert into progress (eid, progress, date)
select id, current_progress, last_modified_date
from temptask;

drop table if exists task;
drop table if exists temptask;

insert into event (name, target_progress, start_date, is_closed, is_finished, type)
select title, 1, date, false, true, 0
from diary
where title <> 'card';

insert into progress (eid, progress, date)
select id, target_progress, start_date
from event
where is_finished = true;

drop table diary;