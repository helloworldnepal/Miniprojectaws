INSERT INTO permissions (name, description) VALUES
                                                ('s3:read',  'Read objects from Mini S3'),
                                                ('s3:write', 'Upload/delete objects in Mini S3'),
                                                ('ec2:read', 'View Mini EC2 instances'),
                                                ('ec2:write','Start/stop/create Mini EC2 instances'),
                                                ('iam:admin','Manage users, roles and permissions in Mini IAM');

INSERT INTO roles (name, description) VALUES
                                          ('USER',  'Default role for every new registered account'),
                                          ('ADMIN', 'Full administrative access across all mini-AWS services');

-- ADMIN gets every permission
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r CROSS JOIN permissions p WHERE r.name = 'ADMIN';

-- USER gets baseline read access only
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r JOIN permissions p ON p.name IN ('s3:read','ec2:read')
WHERE r.name = 'USER';