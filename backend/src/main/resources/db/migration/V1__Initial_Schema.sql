-- SNL Banking Application Database Schema
-- PostgreSQL Database Migration Script

-- Create USERS table
CREATE TABLE IF NOT EXISTS users (
    user_id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    mobile_number VARCHAR(10) UNIQUE,
    pan_number VARCHAR(100),
    aadhar_number VARCHAR(12),
    address VARCHAR(255),
    city VARCHAR(50),
    state VARCHAR(50),
    pincode VARCHAR(6),
    role VARCHAR(50) NOT NULL DEFAULT 'CUSTOMER',
    status VARCHAR(50) NOT NULL DEFAULT 'VERIFIED_PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    CONSTRAINT chk_email_pattern CHECK (email ~ '^[A-Za-z0-9+_.-]+@(.+)$'),
    CONSTRAINT chk_mobile_pattern CHECK (mobile_number ~ '^\d{10}$' OR mobile_number IS NULL),
    CONSTRAINT chk_pincode_pattern CHECK (pincode ~ '^\d{6}$' OR pincode IS NULL)
);

-- Create indexes for USERS table
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_mobile ON users(mobile_number);
CREATE INDEX idx_users_status ON users(status);

-- Create ACCOUNTS table
CREATE TABLE IF NOT EXISTS accounts (
    account_id BIGSERIAL PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL REFERENCES users(user_id),
    account_type VARCHAR(50) NOT NULL,
    balance DECIMAL(15, 2) NOT NULL DEFAULT 0,
    minimum_balance DECIMAL(15, 2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    ifsc_code VARCHAR(50),
    mic_r_code VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for ACCOUNTS table
CREATE INDEX idx_accounts_account_number ON accounts(account_number);
CREATE INDEX idx_accounts_user_id ON accounts(user_id);
CREATE INDEX idx_accounts_status ON accounts(status);
CREATE INDEX idx_accounts_account_type ON accounts(account_type);

-- Create TRANSACTIONS table
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id BIGSERIAL PRIMARY KEY,
    transaction_reference VARCHAR(50) NOT NULL UNIQUE,
    from_account_id BIGINT NOT NULL REFERENCES accounts(account_id),
    to_account_id BIGINT NOT NULL REFERENCES accounts(account_id),
    amount DECIMAL(15, 2) NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    description VARCHAR(500),
    reference_number VARCHAR(100),
    charges DECIMAL(15, 2),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100)
);

-- Create indexes for TRANSACTIONS table
CREATE INDEX idx_transactions_transaction_ref ON transactions(transaction_reference);
CREATE INDEX idx_transactions_from_account ON transactions(from_account_id);
CREATE INDEX idx_transactions_to_account ON transactions(to_account_id);
CREATE INDEX idx_transactions_created_at ON transactions(created_at);
CREATE INDEX idx_transactions_status ON transactions(status);

-- Create LOANS table
CREATE TABLE IF NOT EXISTS loans (
    loan_id BIGSERIAL PRIMARY KEY,
    loan_number VARCHAR(20) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL REFERENCES users(user_id),
    loan_type VARCHAR(50) NOT NULL,
    principal_amount DECIMAL(15, 2) NOT NULL,
    interest_rate DOUBLE PRECISION NOT NULL,
    tenure_months INTEGER NOT NULL,
    emi_amount DECIMAL(15, 2) NOT NULL,
    remaining_amount DECIMAL(15, 2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'APPLIED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for LOANS table
CREATE INDEX idx_loans_loan_number ON loans(loan_number);
CREATE INDEX idx_loans_user_id ON loans(user_id);
CREATE INDEX idx_loans_status ON loans(status);

-- Create BILL_PAYMENTS table
CREATE TABLE IF NOT EXISTS bill_payments (
    bill_payment_id BIGSERIAL PRIMARY KEY,
    bill_reference VARCHAR(50) NOT NULL UNIQUE,
    account_id BIGINT NOT NULL REFERENCES accounts(account_id),
    bill_type VARCHAR(50) NOT NULL,
    biller_name VARCHAR(100) NOT NULL,
    bill_amount DECIMAL(15, 2) NOT NULL,
    biller_reference_number VARCHAR(100),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for BILL_PAYMENTS table
CREATE INDEX idx_bill_payments_bill_reference ON bill_payments(bill_reference);
CREATE INDEX idx_bill_payments_account_id ON bill_payments(account_id);
CREATE INDEX idx_bill_payments_status ON bill_payments(status);

-- Create AUDIT_LOGS table
CREATE TABLE IF NOT EXISTS audit_logs (
    audit_log_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(user_id),
    action VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    ip_address VARCHAR(50),
    user_agent VARCHAR(100),
    status VARCHAR(50) NOT NULL DEFAULT 'SUCCESS',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for AUDIT_LOGS table
CREATE INDEX idx_audit_logs_user_id ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);

-- Create ROLES enum type
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'user_role_enum') THEN
        CREATE TYPE user_role_enum AS ENUM ('CUSTOMER', 'ADMIN', 'SUPPORT_STAFF');
    END IF;
END
$$;

-- Create STATUS enum type
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'user_status_enum') THEN
        CREATE TYPE user_status_enum AS ENUM ('ACTIVE', 'INACTIVE', 'SUSPENDED', 'VERIFIED_PENDING');
    END IF;
END
$$;

-- Add comments to tables and columns
COMMENT ON TABLE users IS 'Stores user/customer information for the banking system';
COMMENT ON TABLE accounts IS 'Stores bank account information';
COMMENT ON TABLE transactions IS 'Stores all banking transactions';
COMMENT ON TABLE loans IS 'Stores loan information';
COMMENT ON TABLE bill_payments IS 'Stores bill payment records';
COMMENT ON TABLE audit_logs IS 'Stores audit trail for user actions';

COMMENT ON COLUMN users.user_id IS 'Unique identifier for user';
COMMENT ON COLUMN users.email IS 'User email address (unique)';
COMMENT ON COLUMN accounts.account_number IS 'Unique bank account number';
COMMENT ON COLUMN accounts.balance IS 'Current account balance';
COMMENT ON COLUMN transactions.transaction_reference IS 'Unique transaction reference number';
COMMENT ON COLUMN loans.loan_number IS 'Unique loan identifier';

-- Create function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create triggers for updated_at columns
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_accounts_updated_at BEFORE UPDATE ON accounts
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_loans_updated_at BEFORE UPDATE ON loans
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_bill_payments_updated_at BEFORE UPDATE ON bill_payments
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
