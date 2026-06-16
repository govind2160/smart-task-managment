INSERT INTO users (name, email, password, role) VALUES ('John Doe', 'john.doe@example.com', '$2a$10$8.KOh/A4S2M0JD.NlEEtuee.h6bE1S.zB5aYLzB1lA5qUuP0qG3X.', 'ROLE_USER');
INSERT INTO users (name, email, password, role) VALUES ('Jane Smith', 'jane.smith@example.com', '$2a$10$8.KOh/A4S2M0JD.NlEEtuee.h6bE1S.zB5aYLzB1lA5qUuP0qG3X.', 'ROLE_USER');

INSERT INTO projects (name, description, status, user_id) VALUES ('College Portal', 'Web app for managing college task assignments.', 'ACTIVE', 1);
INSERT INTO projects (name, description, status, user_id) VALUES ('Smart Task App', 'Spring Boot backend task management backend.', 'PLANNED', 2);

-- Insert project memberships (owner auto-joins project)
INSERT INTO project_members (project_id, user_id) VALUES (1, 1);
INSERT INTO project_members (project_id, user_id) VALUES (2, 2);

-- Insert tasks with created_by_id, assigned_to_id, due_date, and created_at
INSERT INTO tasks (title, status, project_id, created_by_id, assigned_to_id, due_date, created_at) VALUES ('Setup Schema', 'COMPLETED', 1, 1, 1, '2026-06-10', '2026-06-08 10:00:00');
INSERT INTO tasks (title, status, project_id, created_by_id, assigned_to_id, due_date, created_at) VALUES ('Connect Database', 'IN_PROGRESS', 1, 1, 1, '2026-06-25', '2026-06-08 11:30:00');
INSERT INTO tasks (title, status, project_id, created_by_id, assigned_to_id, due_date, created_at) VALUES ('Review Relationships', 'PENDING', 2, 2, 2, '2026-06-12', '2026-06-09 09:00:00');
