// Format currency in INR
export const formatCurrency = (amount) => {
  return new Intl.NumberFormat('en-IN', {
    style: 'currency',
    currency: 'INR',
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  }).format(amount);
};

// Format date
export const formatDate = (date) => {
  return new Intl.DateTimeFormat('en-IN', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  }).format(new Date(date));
};

// Format date for display (short format)
export const formatDateShort = (date) => {
  return new Intl.DateTimeFormat('en-IN', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
  }).format(new Date(date));
};

// Validate email
export const validateEmail = (email) => {
  const emailRegex = /^[A-Za-z0-9+_.-]+@(.+)$/;
  return emailRegex.test(email);
};

// Validate phone number
export const validatePhoneNumber = (phone) => {
  const phoneRegex = /^\d{10}$/;
  return phoneRegex.test(phone);
};

// Validate PAN
export const validatePAN = (pan) => {
  const panRegex = /^[A-Z]{5}[0-9]{4}[A-Z]{1}$/;
  return panRegex.test(pan);
};

// Validate Aadhar
export const validateAadhar = (aadhar) => {
  const aadharRegex = /^\d{12}$/;
  return aadharRegex.test(aadhar);
};

// Mask account number (show only last 4 digits)
export const maskAccountNumber = (accountNumber) => {
  if (!accountNumber || accountNumber.length < 4) return accountNumber;
  const last4 = accountNumber.slice(-4);
  return `****${last4}`;
};

// Get initials from name
export const getInitials = (firstName, lastName) => {
  return `${firstName?.charAt(0) || ''}${lastName?.charAt(0) || ''}`.toUpperCase();
};

// Get account type color
export const getAccountTypeColor = (accountType) => {
  const colors = {
    SAVINGS: '#4CAF50',
    CURRENT: '#2196F3',
    SALARY: '#FF9800',
  };
  return colors[accountType] || '#1F3A93';
};

// Get transaction status color
export const getTransactionStatusColor = (status) => {
  const colors = {
    COMPLETED: '#4CAF50',
    PENDING: '#FF9800',
    FAILED: '#F44336',
    CANCELLED: '#999999',
    REVERSED: '#2196F3',
  };
  return colors[status] || '#666666';
};

// Get status badge
export const getStatusBadge = (status) => {
  const badges = {
    ACTIVE: { label: 'Active', color: 'success' },
    INACTIVE: { label: 'Inactive', color: 'error' },
    SUSPENDED: { label: 'Suspended', color: 'warning' },
    VERIFIED_PENDING: { label: 'Pending Verification', color: 'info' },
    FROZEN: { label: 'Frozen', color: 'error' },
    CLOSED: { label: 'Closed', color: 'error' },
  };
  return badges[status] || { label: status, color: 'default' };
};

// Truncate text
export const truncateText = (text, maxLength) => {
  if (!text) return '';
  if (text.length <= maxLength) return text;
  return `${text.slice(0, maxLength)}...`;
};

// Get user role color
export const getRoleColor = (role) => {
  const colors = {
    CUSTOMER: '#4CAF50',
    ADMIN: '#F44336',
    SUPPORT_STAFF: '#2196F3',
  };
  return colors[role] || '#666666';
};
