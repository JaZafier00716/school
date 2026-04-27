CREATE TYPE project_status AS ENUM ('draft', 'sent', 'approved', 'archived');
CREATE TYPE project_roles AS ENUM ('manager', 'editor');

CREATE TABLE roles (
    role_id BIGSERIAL PRIMARY KEY,
    name project_roles NOT NULL,
    can_manage_users BOOLEAN NOT NULL,
    can_manage_projects BOOLEAN NOT NULL,
    can_publish BOOLEAN NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,
    role_id BIGINT NOT NULL REFERENCES roles(role_id),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE projects (
    project_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    event_date DATE,
    guest_count INTEGER,
    status project_status NOT NULL,
    created_by BIGINT NOT NULL REFERENCES users(user_id),
    active_version_id BIGINT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE project_collaborators (
    project_id BIGINT NOT NULL REFERENCES projects(project_id),
    user_id BIGINT NOT NULL REFERENCES users(user_id),
    role_in_project project_roles NOT NULL,
    added_by BIGINT NOT NULL REFERENCES users(user_id),
    added_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (project_id, user_id)
);

CREATE TABLE menus (
    menu_id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL REFERENCES projects(project_id),
    name VARCHAR(255) NOT NULL,
    created_by BIGINT NOT NULL REFERENCES users(user_id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE templates (
    template_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    font VARCHAR(255),
    background_image VARCHAR(2048),
    style_json TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE menu_versions (
    version_id BIGSERIAL PRIMARY KEY,
    menu_id BIGINT NOT NULL REFERENCES menus(menu_id),
    template_id BIGINT REFERENCES templates(template_id),
    version_number INTEGER NOT NULL,
    with_prices BOOLEAN NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

ALTER TABLE projects
    ADD CONSTRAINT fk_projects_active_version
    FOREIGN KEY (active_version_id)
    REFERENCES menu_versions(version_id);

CREATE TABLE sections (
    section_id BIGSERIAL PRIMARY KEY,
    version_id BIGINT NOT NULL REFERENCES menu_versions(version_id),
    name VARCHAR(255) NOT NULL,
    display_order INTEGER NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE items (
    item_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(255),
    price NUMERIC(12, 2),
    dph NUMERIC(5, 2),
    allergens VARCHAR(255),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE menu_items (
    menu_item_id BIGSERIAL PRIMARY KEY,
    section_id BIGINT NOT NULL REFERENCES sections(section_id),
    item_id BIGINT NOT NULL REFERENCES items(item_id),
    servings_per_person NUMERIC(8, 2),
    price_at_version NUMERIC(12, 2),
    display_order INTEGER NOT NULL,
    notes TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
