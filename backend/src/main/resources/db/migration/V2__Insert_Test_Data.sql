-- Add initial test data to SNL Banking Application

-- Insert test users
INSERT INTO users (first_name, last_name, email, password, mobile_number, pan_number, aadhar_number, 
                   address, city, state, pincode, role, status)
VALUES 
('Rajesh', 'Kumar', 'rajesh.kumar@gmail.com', 'hashed_password_123', '9876543210', 'AAAAA0001A', 
 '123456789012', '123 Main Street', 'Mumbai', 'Maharashtra', '400001', 'CUSTOMER', 'ACTIVE'),
('Priya', 'Sharma', 'priya.sharma@gmail.com', 'hashed_password_456', '9876543211', 'BBBBB0002B', 
 '234567890123', '456 Park Avenue', 'Delhi', 'Delhi', '110001', 'CUSTOMER', 'ACTIVE'),
('Admin', 'User', 'admin@snlbanking.com', 'hashed_admin_password', '9876543212', 'CCCCC0003C', 
 '345678901234', '789 Admin Lane', 'Bangalore', 'Karnataka', '560001', 'ADMIN', 'ACTIVE');

-- Insert test accounts
INSERT INTO accounts (account_number, user_id, account_type, balance, minimum_balance, status, ifsc_code, mic_r_code)
VALUES 
('SNL1001000001', 1, 'SAVINGS', '50000.00', '1000.00', 'ACTIVE', 'HDFC0001234', 'HDFC000123'),
('SNL1002000002', 1, 'CURRENT', '150000.00', '5000.00', 'ACTIVE', 'HDFC0001234', 'HDFC000123'),
('SNL1003000003', 2, 'SAVINGS', '75000.00', '1000.00', 'ACTIVE', 'HDFC0001234', 'HDFC000123'),
('SNL1004000004', 2, 'SALARY', '100000.00', '500.00', 'ACTIVE', 'HDFC0001234', 'HDFC000123');

-- Insert test transactions
INSERT INTO transactions (transaction_reference, from_account_id, to_account_id, amount, transaction_type, 
                         status, description, charges)
VALUES 
('TXN202401010001', 1, 3, '5000.00', 'INTRA_BANK_TRANSFER', 'COMPLETED', 'Payment to friend', '10.00'),
('TXN202401010002', 3, 1, '2500.00', 'INTRA_BANK_TRANSFER', 'COMPLETED', 'Return payment', '10.00'),
('TXN202401010003', 2, 4, '10000.00', 'INTRA_BANK_TRANSFER', 'PENDING', 'Business transfer', '10.00');

-- Insert test loans
INSERT INTO loans (loan_number, user_id, loan_type, principal_amount, interest_rate, tenure_months, 
                  emi_amount, remaining_amount, status)
VALUES 
('LN001-2024-001', 1, 'PERSONAL_LOAN', '100000.00', '8.5', 24, '4535.00', '100000.00', 'ACTIVE'),
('LN002-2024-002', 2, 'HOME_LOAN', '2000000.00', '7.2', 240, '14567.00', '2000000.00', 'ACTIVE');

-- Insert test bill payments
INSERT INTO bill_payments (bill_reference, account_id, bill_type, biller_name, bill_amount, status)
VALUES 
('BILL202401010001', 1, 'ELECTRICITY', 'Mumbai Power Company', '2500.00', 'COMPLETED'),
('BILL202401010002', 3, 'MOBILE', 'Airtel', '499.00', 'COMPLETED'),
('BILL202401010003', 1, 'WATER', 'Water Board', '800.00', 'PENDING');

-- Insert test audit logs
INSERT INTO audit_logs (user_id, action, description, ip_address, status)
VALUES 
(1, 'LOGIN', 'User logged in', '192.168.1.100', 'SUCCESS'),
(1, 'FUND_TRANSFER', 'Transferred 5000 to account', '192.168.1.100', 'SUCCESS'),
(2, 'LOGIN', 'User logged in', '192.168.1.101', 'SUCCESS'),
(3, 'LOGIN', 'Admin logged in', '192.168.1.102', 'SUCCESS');
