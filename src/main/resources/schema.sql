drop table if exists progress;
drop table if exists achievement;
drop table if exists event;

CREATE TABLE event
(
  id              int PRIMARY KEY AUTO_INCREMENT,
  name            varchar(20),
  target_progress int,
  start_date      date,
  running         bit,
  type            int
);

CREATE TABLE progress
(
  id       int primary key AUTO_INCREMENT,
  eid      int,
  progress int,
  record   int,
  date     date,
  unique key eid_date_key (eid, date),
  CONSTRAINT progress_event_id_fk FOREIGN KEY (eid) REFERENCES event (id)
);

create table achievement (
  eid  int key,
  date date,
  constraint achievement_event_id_fk foreign key (eid) references event (id)
);