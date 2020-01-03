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