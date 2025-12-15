-- Tạo các database riêng biệt cho từng service
CREATE DATABASE product_db;
CREATE DATABASE inventory_db;
CREATE DATABASE order_db;
CREATE DATABASE auth_db;
CREATE DATABASE customer_db;

-- (Tùy chọn) Gán quyền nếu bạn muốn tạo user riêng
-- GRANT ALL PRIVILEGES ON DATABASE product_db TO myuser;
