MERGE INTO roles r
USING (SELECT 1 AS role_id, 'manager' AS name, 1 AS can_manage_users, 1 AS can_manage_projects, 1 AS can_publish FROM dual) s
ON (r.role_id = s.role_id)
WHEN NOT MATCHED THEN
  INSERT (role_id, name, can_manage_users, can_manage_projects, can_publish)
  VALUES (s.role_id, s.name, s.can_manage_users, s.can_manage_projects, s.can_publish);

MERGE INTO roles r
USING (SELECT 2 AS role_id, 'editor' AS name, 0 AS can_manage_users, 1 AS can_manage_projects, 0 AS can_publish FROM dual) s
ON (r.role_id = s.role_id)
WHEN NOT MATCHED THEN
  INSERT (role_id, name, can_manage_users, can_manage_projects, can_publish)
  VALUES (s.role_id, s.name, s.can_manage_users, s.can_manage_projects, s.can_publish);

MERGE INTO users u
USING (
    SELECT 1 AS user_id, 1 AS role_id, 'Admin Manager' AS name, 'manager@example.com' AS email, '{noop}manager' AS password_hash, 1 AS is_active
    FROM dual
) s
ON (u.user_id = s.user_id)
WHEN NOT MATCHED THEN
  INSERT (user_id, role_id, name, email, password_hash, is_active)
  VALUES (s.user_id, s.role_id, s.name, s.email, s.password_hash, s.is_active);

MERGE INTO users u
USING (
    SELECT 2 AS user_id, 2 AS role_id, 'Editor User' AS name, 'editor@example.com' AS email, '{noop}editor' AS password_hash, 1 AS is_active
    FROM dual
) s
ON (u.user_id = s.user_id)
WHEN NOT MATCHED THEN
  INSERT (user_id, role_id, name, email, password_hash, is_active)
  VALUES (s.user_id, s.role_id, s.name, s.email, s.password_hash, s.is_active);

MERGE INTO projects p
USING (
    SELECT 1 AS project_id, 'Sample Wedding Catering' AS name, TRUNC(SYSDATE) + 30 AS event_date, 120 AS guest_count, 'draft' AS status, 1 AS created_by
    FROM dual
) s
ON (p.project_id = s.project_id)
WHEN NOT MATCHED THEN
  INSERT (project_id, name, event_date, guest_count, status, created_by)
  VALUES (s.project_id, s.name, s.event_date, s.guest_count, s.status, s.created_by);

MERGE INTO project_collaborators pc
USING (SELECT 1 AS project_id, 1 AS user_id, 'manager' AS role_in_project, 1 AS added_by FROM dual) s
ON (pc.project_id = s.project_id AND pc.user_id = s.user_id)
WHEN NOT MATCHED THEN
  INSERT (project_id, user_id, role_in_project, added_by)
  VALUES (s.project_id, s.user_id, s.role_in_project, s.added_by);

MERGE INTO project_collaborators pc
USING (SELECT 1 AS project_id, 2 AS user_id, 'editor' AS role_in_project, 1 AS added_by FROM dual) s
ON (pc.project_id = s.project_id AND pc.user_id = s.user_id)
WHEN NOT MATCHED THEN
  INSERT (project_id, user_id, role_in_project, added_by)
  VALUES (s.project_id, s.user_id, s.role_in_project, s.added_by);

MERGE INTO templates t
USING (SELECT 1 AS template_id, 'Classic' AS name, 'Georgia' AS font, CAST(NULL AS VARCHAR2(2048)) AS background_image, '{"layout":"classic"}' AS style_json FROM dual) s
ON (t.template_id = s.template_id)
WHEN NOT MATCHED THEN
  INSERT (template_id, name, font, background_image, style_json)
  VALUES (s.template_id, s.name, s.font, s.background_image, s.style_json);

MERGE INTO templates t
USING (SELECT 2 AS template_id, 'Minimal' AS name, 'Arial' AS font, CAST(NULL AS VARCHAR2(2048)) AS background_image, '{"layout":"minimal"}' AS style_json FROM dual) s
ON (t.template_id = s.template_id)
WHEN NOT MATCHED THEN
  INSERT (template_id, name, font, background_image, style_json)
  VALUES (s.template_id, s.name, s.font, s.background_image, s.style_json);

