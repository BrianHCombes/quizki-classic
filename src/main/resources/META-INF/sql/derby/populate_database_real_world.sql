-- Copyright 2013,2014 Johnathan E. James - haxwell.org - jj-ccs.com - quizki.com
--
-- This file is part of Quizki.
--
-- Quizki is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as published by
-- the Free Software Foundation, either version 2 of the License, or
-- (at your option) any later version.
--
-- Quizki is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
-- GNU General Public License for more details.
--
-- You should have received a copy of the GNU General Public License
-- along with Quizki. If not, see http://www.gnu.org/licenses.



INSERT INTO topic(text) values('Java');
INSERT INTO topic(text) values('Servlets');

INSERT INTO question_type(id, text) values(1, 'Single');
INSERT INTO question_type(id, text) values(2, 'Multiple');
INSERT INTO question_type(id, text) values(3, 'String');
INSERT INTO question_type(id, text) values(4, 'Sequence');

INSERT INTO difficulty(id, text) values(1, 'Junior');
INSERT INTO difficulty(id, text) values(2, 'Intermediate');
INSERT INTO difficulty(id, text) values(3, 'Well-versed');
INSERT INTO difficulty(id, text) values(4, 'Guru');

INSERT INTO user_roles(id, text) values(1, 'Administrator');
INSERT INTO user_roles(id, text) values(2, 'General User');

INSERT INTO users(username, password) values('johnathan', 'password');
INSERT INTO users(username, password) values('billy', 'password');

INSERT INTO users_roles_map(user_id, role_id) values(1, 1);
INSERT INTO users_roles_map(user_id, role_id) values(2, 2);

-- QUESTIONS FOR USER 1

INSERT INTO question(description, text, difficulty_id, type_id, user_id) values('Servlet Lifecyle', '<p>The servlet lifecycle begins when the init() method is called.</p>', 1, 1, 1);

-------
INSERT INTO choice(text, iscorrect, sequence) values('True', 1, 0);
INSERT INTO choice(text, iscorrect, sequence) values('False', 0, 0);

INSERT INTO question_choice(question_id, choice_id) values(1,1);
INSERT INTO question_choice(question_id, choice_id) values(1,2);

INSERT INTO question_topic(question_id, topic_id)   values(1,1);
INSERT INTO question_topic(question_id, topic_id)   values(1,2);
