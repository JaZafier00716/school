-- DROP TABLES IN THE RIGHT ORDER TO AVOID FK ERRORS
-- IF OBJECT_ID('menu_items') IS NOT NULL DROP TABLE menu_items;
-- IF OBJECT_ID('sections') IS NOT NULL DROP TABLE sections;
-- IF OBJECT_ID('menus') IS NOT NULL DROP TABLE menus;
-- IF OBJECT_ID('project_collaborators') IS NOT NULL DROP TABLE project_collaborators;
-- IF OBJECT_ID('projects') IS NOT NULL DROP TABLE projects;
-- IF OBJECT_ID('items') IS NOT NULL DROP TABLE items;
-- IF OBJECT_ID('templates') IS NOT NULL DROP TABLE templates;
-- IF OBJECT_ID('users') IS NOT NULL DROP TABLE users;
-- IF OBJECT_ID('roles') IS NOT NULL DROP TABLE roles;
-- GO


IF NOT EXISTS(SELECT *
              FROM sys.tables
              where name = 'roles')
    BEGIN
        CREATE TABLE [roles]
        (
            [role_id]             integer IDENTITY PRIMARY KEY,
            [name]                varchar(255) UNIQUE NOT NULL,
            [can_manage_users]    bit,
            [can_manage_projects] bit,
            [can_publish]         bit,
            [created_at]          datetime,
            [updated_at]          datetime
        )
    END
GO

IF NOT EXISTS(SELECT *
              FROM sys.tables
              where name = 'users')
    BEGIN
        CREATE TABLE [users]
        (
            [user_id]       integer IDENTITY PRIMARY KEY,
            [role_id]       integer             NOT NULL,
            [name]          varchar(255)        NOT NULL,
            [email]         varchar(255) UNIQUE NOT NULL,
            [password_hash] varchar(255)        NOT NULL,
            [is_active]     bit DEFAULT (1)     NOT NULL,
            [created_at]    datetime,
            [updated_at]    datetime
        )
    END
GO

IF NOT EXISTS(SELECT *
              FROM sys.tables
              where Name = 'projects')
    BEGIN
        CREATE TABLE [projects]
        (
            [project_id]  integer IDENTITY PRIMARY KEY,
            [name]        varchar(255) NOT NULL,
            [event_date]  date,
            [guest_count] integer,
            [status]      varchar(8)   NOT NULL CHECK ([status] IN ('draft', 'sent', 'approved', 'archived')),
            [created_by]  integer      NOT NULL,
            [created_at]  datetime,
            [updated_at]  datetime
        )
    END
GO

IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'project_collaborators')
    BEGIN
        CREATE TABLE [project_collaborators]
        (
            [project_id]      integer,
            [user_id]         integer,
            [role_in_project] varchar(8) NOT NULL CHECK ([role_in_project] IN ('manager', 'editor')),
            [added_by]        integer    NOT NULL,
            [added_at]        datetime,
            PRIMARY KEY ([project_id], [user_id])
        )
    END
GO

IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'items')
    BEGIN
        CREATE TABLE [items]
        (
            [item_id]     integer IDENTITY PRIMARY KEY,
            [name]        varchar(255) NOT NULL,
            [description] nvarchar(max),
            [category]    varchar(255),
            [price]       decimal(10, 2),
            [dph]         decimal(4, 2),
            [allergens]   varchar(255),
            [created_at]  datetime,
            [updated_at]  datetime
        )
    END
GO

IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'templates')
    BEGIN
        CREATE TABLE [templates]
        (
            [template_id]      integer IDENTITY PRIMARY KEY,
            [name]             varchar(255),
            [font]             varchar(255),
            [background_image] varchar(255),
            [style_json]       nvarchar(max),
            [created_at]       datetime
        )
    END
GO

IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'menus')
    BEGIN
        CREATE TABLE [menus]
        (
            [menu_id]     integer IDENTITY PRIMARY KEY,
            [template_id] integer      NOT NULL,
            [project_id]  integer      NOT NULL,
            [name]        varchar(255) NOT NULL,
            [version]     integer      NOT NULL,
            [with_prices] bit,
            [created_by]  integer      NOT NULL,
            [created_at]  datetime,
            [updated_at]  datetime
        )
    END
GO

IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'sections')
    BEGIN
        CREATE TABLE [sections]
        (
            [section_id]    integer IDENTITY PRIMARY KEY,
            [menu_id]       integer      NOT NULL,
            [name]          varchar(255) NOT NULL,
            [display_order] integer,
            [created_at]    datetime,
            [updated_at]    datetime
        )
    END
GO

IF NOT EXISTS(SELECT *
              FROM sys.tables
              WHERE name = 'menu_items')
    BEGIN
        CREATE TABLE [menu_items]
        (
            [menu_item_id]        integer IDENTITY PRIMARY KEY,
            [section_id]          integer NOT NULL,
            [item_id]             integer NOT NULL,
            [servings_per_person] decimal,
            [price_at_version]    decimal,
            [display_order]       integer,
            [notes]               nvarchar(max),
            [created_at]          datetime,
            [updated_at]          datetime
        )
    END
GO

ALTER TABLE [users]
    ADD FOREIGN KEY ([role_id]) REFERENCES [roles] ([role_id])
GO

ALTER TABLE [projects]
    ADD FOREIGN KEY ([created_by]) REFERENCES [users] ([user_id])
GO

ALTER TABLE [menus]
    ADD FOREIGN KEY ([project_id]) REFERENCES [projects] ([project_id])
GO

ALTER TABLE [menus]
    ADD FOREIGN KEY ([template_id]) REFERENCES [templates] ([template_id])
GO

ALTER TABLE [menus]
    ADD FOREIGN KEY ([created_by]) REFERENCES [users] ([user_id])
GO

ALTER TABLE [sections]
    ADD FOREIGN KEY ([menu_id]) REFERENCES [menus] ([menu_id])
GO

ALTER TABLE [menu_items]
    ADD FOREIGN KEY ([section_id]) REFERENCES [sections] ([section_id])
GO

ALTER TABLE [menu_items]
    ADD FOREIGN KEY ([item_id]) REFERENCES [items] ([item_id])
GO

ALTER TABLE [project_collaborators]
    ADD FOREIGN KEY ([user_id]) REFERENCES [users] ([user_id])
GO

ALTER TABLE [project_collaborators]
    ADD FOREIGN KEY ([project_id]) REFERENCES [projects] ([project_id])
GO

ALTER TABLE [project_collaborators]
    ADD FOREIGN KEY ([added_by]) REFERENCES [users] ([user_id])
GO


INSERT INTO items (name, description, category, price, dph, allergens, created_at, updated_at)
VALUES ('Grilled Chicken Salad', 'A healthy mix of grilled chicken, fresh greens, and vinaigrette.', 'Salads', 12.99,
        0.15, 'None', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Spaghetti Bolognese', 'Classic Italian pasta with rich meat sauce.', 'Main Courses', 14.99, 0.15, 'Gluten',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Vegetarian Pizza', 'Pizza topped with a variety of fresh vegetables and mozzarella cheese.', 'Main Courses',
        11.99, 0.15, 'Gluten, Dairy', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Chocolate Lava Cake', 'Warm chocolate cake with a gooey center served with vanilla ice cream.', 'Desserts',
        6.99, 0.15, 'Dairy, Eggs', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Fresh Fruit Platter', 'A selection of seasonal fresh fruits.', 'Desserts', 5.99, 0.15, 'None',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
GO
INSERT INTO roles (name, can_manage_users, can_manage_projects, can_publish, created_at, updated_at)
VALUES ('Admin', 1, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project Manager', 0, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Editor', 0, 0, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Viewer', 0, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
GO
INSERT INTO users (name, role_id, email, password_hash, is_active, created_at, updated_at)
VALUES ('Alice Johnson', 1, 'alice.johnson@smart-catering.com', 'hashed_password_1', 1, CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       ('Bob Smith', 2, 'bob.smith@smart-catering.com', 'hashed_password_2', 1, CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       ('Charlie Brown', 3, 'charlie.brown@smart-catering.com', 'hashed_password_3', 1, CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       ('Diana Prince', 4, 'diana.prince@smart-catering.com', 'hashed_password_4', 1, CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP);
GO
INSERT INTO templates (name, font, background_image, style_json, created_at)
VALUES ('Classic Elegance', 'Times New Roman', 'classic_elegance_bg.jpg',
        '{"color_scheme": "gold and white", "layout": "traditional"}', CURRENT_TIMESTAMP),
       ('Modern Chic', 'Helvetica', 'modern_chic_bg.jpg', '{"color_scheme": "black and white", "layout": "minimalist"}',
        CURRENT_TIMESTAMP);
GO
INSERT INTO projects (name, event_date, guest_count, status, created_by, created_at, updated_at)
VALUES ('Johnson Wedding', '2024-09-15', 150, 'draft', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Corporate Gala', '2024-10-20', 300, 'sent', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
GO
INSERT INTO menus (template_id, project_id, name, version, with_prices, created_by, created_at, updated_at)
VALUES (1, 1, 'Johnson Wedding Menu', 1, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 2, 'Corporate Gala Menu', 1, 0, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
GO
INSERT INTO sections (menu_id, name, display_order, created_at, updated_at)
VALUES (1, 'Starters', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (1, 'Main Courses', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (1, 'Desserts', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 'Appetizers', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 'Entrees', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 'Sweets', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
GO
INSERT INTO menu_items (section_id, item_id, servings_per_person, price_at_version, display_order, notes, created_at,
                        updated_at)
VALUES (1, 1, 1.0, 12.99, 1, 'Serve chilled', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 2, 1.0, 14.99, 1, 'Gluten-free option available', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 3, 1.0, 11.99, 2, 'Extra cheese on request', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 4, 1.0, 6.99, 1, 'Serve warm', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 5, 1.0, 5.99, 2, 'Freshly cut', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
GO
INSERT INTO project_collaborators (project_id, user_id, role_in_project, added_by, added_at)
VALUES (1, 2, 'manager', 1, CURRENT_TIMESTAMP),
       (1, 3, 'editor', 1, CURRENT_TIMESTAMP),
       (2, 3, 'manager', 2, CURRENT_TIMESTAMP),
       (2, 4, 'editor', 2, CURRENT_TIMESTAMP);
GO


SELECT p.project_id,
       p.name AS project_name,
       m.name AS menu_name,
       s.name AS section_name,
       i.name AS item_name,
       mi.price_at_version
FROM projects p
         JOIN menus m ON m.project_id = p.project_id
         JOIN sections s ON s.menu_id = m.menu_id
         LEFT JOIN menu_items mi ON mi.section_id = s.section_id
         LEFT JOIN items i ON i.item_id = mi.item_id
WHERE p.project_id = 1
   or p.project_id = 2;

