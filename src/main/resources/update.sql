insert into event (name, current_progress, target_progress, start_date,
                   last_modified_date, is_closed, is_finished, type)
  select name, current_progress, target_progress, start_date, last_modified_date,false ,false ,0
  from task;

drop table if exists task;

insert into progress(eid,progress,date)
  select id,1,date
  from diary
  where title <> 'card';

drop table diary;