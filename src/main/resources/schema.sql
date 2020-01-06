drop table if exists progress;
drop table if exists event;
CREATE TABLE event
(
  id              int PRIMARY KEY AUTO_INCREMENT,
  name            varchar(20),
  target_progress int,
  start_date      date,
  is_closed       bit,
  is_finished     bit,
  type            int
);

CREATE TABLE progress
(
  id       int primary key AUTO_INCREMENT,
  eid      int,
  progress int,
  date     date,
  unique key eid_date_key (eid, date),
  CONSTRAINT progress_event_id_fk FOREIGN KEY (eid) REFERENCES event (id)
);