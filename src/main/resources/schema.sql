drop table if exists progress;
drop table if exists event;
CREATE TABLE event
(
  id                 int PRIMARY KEY AUTO_INCREMENT,
  name               varchar(20),
  current_progress   int,
  target_progress    int,
  start_date         date,
  last_modified_date date,
  is_closed          bit,
  is_finished        bit,
  type               int
);

CREATE TABLE progress
(
  id       int PRIMARY KEY AUTO_INCREMENT,
  eid      int,
  progress int,
  date     date,
  CONSTRAINT progress_event_id_fk FOREIGN KEY (eid) REFERENCES event (id)
);