-- ============================================================
-- Microservices Banking Service - Database Setup Script
-- Author: Pratham Rathod | prathamrathod200@gmail.com
-- Run this script before starting the application
-- ============================================================

-- Create all databases
CREATE DATABASE IF NOT EXISTS banking_customer_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS banking_account_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS banking_transaction_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS banking_loan_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS banking_notification_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Verify databases created
SHOW DATABASES LIKE 'banking_%';

SELECT 'All banking databases created successfully!' AS Status;

-- ============================================================
-- NOTE: Tables will be auto-created by Hibernate (ddl-auto: update)
-- when you start each microservice
-- ============================================================
