drop table event;
CREATE TABLE event
(
  id                 int PRIMARY KEY AUTO_INCREMENT,
  name               varchar(20),
  current_progress    int,
  target_progress     int,
  start_date         date,
  last_modified_date date,
  is_closed int,
  is_finished int,
  type int
);

insert into event (name, current_progress,
                   target_progress, start_date,
                   last_modified_date, is_closed,
                   is_finished, type)
    VALUES('test',0,100,'2018-06-12','2018-06-12',0,0,0);