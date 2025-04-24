CREATE TABLE roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- 🔐 Key Pairs Table สำหรับ Signing JWT
CREATE TABLE key_pairs (
    key_id VARCHAR(64) PRIMARY KEY,                   -- ใช้เป็น kid ใน JWT header
    platform VARCHAR(64) NOT NULL,                    -- เช่น web, ios, android, partner-x
    algorithm VARCHAR(16) NOT NULL,				      -- เช่น RS256, ES256
    public_key TEXT NOT NULL,                         -- Public Key (PEM format)
    private_key TEXT NOT NULL,                        -- Private Key (ควรเข้ารหัสก่อนเก็บ) **Key สำหรับใช้ encrypt (ควรเก็บใน env / vault เท่านั้น)
    is_active BOOLEAN NOT NULL,						  -- คีย์นี้ยังใช้งานอยู่หรือไม่
    created_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP
);

-- 👤 Users Table
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL,
    is_verified BOOLEAN NOT NULL,  -- การยืนยัน ของผู้ใช้
    role_id INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
);


-- 🔑 Tokens Table สำหรับ Access/Refresh Token
CREATE TABLE tokens (
    token_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    refresh_token VARCHAR(512) NOT NULL,
    key_id VARCHAR(64),                                    -- อ้างอิงถึง key ที่ใช้เซ็น JWT
    platform VARCHAR(64),                                  -- อุปกรณ์ เช่น android, ios, web
    device_id VARCHAR(128),                                -- รหัสอุปกรณ์ (เฉพาะ mobile)
    created_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (key_id) REFERENCES key_pairs(key_id) ON DELETE RESTRICT
);

-- 📜 Login Logs Table สำหรับเก็บประวัติ Login
CREATE TABLE login_logs (
    log_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    platform VARCHAR(64),                     -- เช่น android, ios, web
    device_id VARCHAR(128),                   -- สำหรับ Mobile (Android ID / iOS UUID)
    ip_address VARCHAR(45),                   -- รองรับ IPv6
    user_agent TEXT,                          -- ข้อมูล Browser / App
    location VARCHAR(255),                    -- ตำแหน่งที่ login (optional)
    login_status VARCHAR(16) NOT NULL,        -- success / failed / locked / 2fa-required
    error_message TEXT,                       -- รายละเอียดถ้ามี error
    logged_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- สร้างตาราง sessions
CREATE TABLE sessions (
    session_id SERIAL PRIMARY KEY,               -- session_id ใช้ระบุ session แต่ละตัว
    user_id INTEGER NOT NULL,                    -- user_id ของผู้ใช้ที่ล็อกอิน
    device_id VARCHAR(128),                      -- รหัสอุปกรณ์ที่ใช้ล็อกอิน (เช่น UUID)
    platform VARCHAR(64),                        -- เช่น 'android', 'ios', 'web'
    ip_address VARCHAR(45),                      -- ที่อยู่ IP ของการล็อกอิน
    created_at TIMESTAMP NOT NULL, -- เวลาที่สร้าง session
    last_activity TIMESTAMP NOT NULL, -- เวลาที่ใช้งานล่าสุด
    expires_at TIMESTAMP,                        -- เวลาหมดอายุของ session
    is_active BOOLEAN NOT NULL,              -- session ยังใช้งานอยู่หรือไม่
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- MFA Table สำหรับจัดการ Multi-Factor Authentication
CREATE TABLE mfa (
    mfa_id SERIAL PRIMARY KEY,               -- ID ของการตั้งค่า MFA
    user_id INTEGER NOT NULL,                -- user_id ของผู้ใช้ที่เปิดใช้งาน MFA
    mfa_type VARCHAR(64) NOT NULL,           -- ประเภท MFA เช่น 'OTP', 'TOTP'
    mfa_enabled BOOLEAN NOT NULL,       -- ตรวจสอบว่า MFA เปิดใช้งานหรือไม่
    mfa_secret VARCHAR(255),                 -- ข้อมูลที่ใช้สำหรับ MFA เช่น secret key สำหรับ TOTP
    expires_at TIMESTAMP, --เพื่อกำหนดเวลา OTP หมดอายุ
    verified_at TIMESTAMP, --เพื่อเก็บเวลาที่ผู้ใช้ยืนยัน MFA สำเร็จ
    created_at TIMESTAMP NOT NULL,  -- เวลาที่ตั้ง MFA
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE ip_blacklist (
    ip_id SERIAL PRIMARY KEY,                    -- ip_id
    ip_address VARCHAR(45) NOT NULL,              -- ที่อยู่ IP
    reason VARCHAR(255),                          -- สาเหตุที่บล็อก IP
    created_at TIMESTAMP NOT NULL -- เวลาที่บล็อก IP
);

-- Login Attempts Table สำหรับการจำกัดการพยายาม login ผิด
CREATE TABLE login_attempts (
    attempt_id SERIAL PRIMARY KEY,          -- ID ของการพยายาม login
    user_id INTEGER NOT NULL,               -- user_id ของผู้ใช้ที่พยายาม login
    attempt_count INTEGER NOT NULL,        -- จำนวนการพยายาม login ผิด
    last_attempt TIMESTAMP,					-- เวลาล่าสุดที่พยายาม login
    ip_address VARCHAR(45),                 -- IP ของผู้ใช้ที่พยายาม login
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Audit Logs Table สำหรับบันทึกกิจกรรมที่สำคัญ
CREATE TABLE audit_logs (
    log_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,                     -- user_id ที่ทำการกระทำ
    action VARCHAR(255) NOT NULL,                 -- action ที่เกิดขึ้น เช่น 'login', 'logout', 'change password', 'delete record'
    target_table VARCHAR(255),                    -- ตารางหรือแหล่งข้อมูลที่ถูกกระทำ (เช่น 'users', 'tokens')
    target_record_id INTEGER,                     -- ID ของข้อมูลที่ถูกกระทำ (เช่น 'user_id' ที่ถูกเปลี่ยนแปลง)
    ip_address VARCHAR(45),                       -- ที่อยู่ IP ของการกระทำ
    device_id VARCHAR(128),                       -- อุปกรณ์ที่ใช้งาน
    created_at TIMESTAMP NOT NULL, -- เวลาที่ทำการบันทึก
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);


CREATE TABLE permissions (
    permission_id SERIAL PRIMARY KEY,
    role_id INTEGER NOT NULL,
    permission_name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
);

