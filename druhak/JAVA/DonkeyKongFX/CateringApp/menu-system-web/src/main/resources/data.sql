INSERT INTO roles (role_id, name, can_manage_users, can_manage_projects, can_publish)
VALUES
    (1, 'manager', TRUE, TRUE, TRUE),
    (2, 'editor', FALSE, TRUE, FALSE)
ON CONFLICT (role_id) DO NOTHING;

INSERT INTO users (user_id, role_id, name, email, password_hash, is_active)
VALUES
    (1, 1, 'Admin Manager', 'manager@example.com', '{noop}manager', TRUE),
    (2, 2, 'Editor User', 'editor@example.com', '{noop}editor', TRUE)
ON CONFLICT (user_id) DO NOTHING;

INSERT INTO projects (project_id, name, event_date, guest_count, status, created_by)
VALUES
    (1, 'Sample Wedding Catering', CURRENT_DATE + 30, 120, 'draft', 1)
ON CONFLICT (project_id) DO NOTHING;

INSERT INTO project_collaborators (project_id, user_id, role_in_project, added_by)
VALUES
    (1, 1, 'manager', 1),
    (1, 2, 'editor', 1)
ON CONFLICT (project_id, user_id) DO NOTHING;

INSERT INTO templates (template_id, name, font, background_image, style_json)
VALUES
    (1, 'Classic', 'Georgia', NULL, '{"layout":"classic"}'),
    (2, 'Minimal', 'Arial', NULL, '{"layout":"minimal"}')
ON CONFLICT (template_id) DO NOTHING;
