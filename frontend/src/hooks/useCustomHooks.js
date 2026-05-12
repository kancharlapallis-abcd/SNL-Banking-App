import { useDispatch, useSelector } from 'react-redux';
import { logout } from '../store/authSlice';
import { useCallback } from 'react';

// Custom hook for auth
export const useAuth = () => {
  const dispatch = useDispatch();
  const { user, token, isAuthenticated, loading, error } = useSelector((state) => state.auth);

  const handleLogout = useCallback(() => {
    dispatch(logout());
  }, [dispatch]);

  return {
    user,
    token,
    isAuthenticated,
    loading,
    error,
    logout: handleLogout,
  };
};

// Custom hook for accounts
export const useAccounts = () => {
  const { accounts, selectedAccount, loading, error } = useSelector((state) => state.account);

  return {
    accounts,
    selectedAccount,
    loading,
    error,
  };
};

// Custom hook for transactions
export const useTransactions = () => {
  const { transactions, selectedTransaction, loading, error } = useSelector(
    (state) => state.transaction
  );

  return {
    transactions,
    selectedTransaction,
    loading,
    error,
  };
};
