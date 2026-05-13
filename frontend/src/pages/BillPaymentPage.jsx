import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import {
  Box,
  Container,
  Paper,
  TextField,
  Button,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TablePagination,
  Card,
  CardContent,
  CircularProgress,
  Chip,
  Typography,
  Alert,
  Grid,
} from '@mui/material';
import { toast } from 'react-toastify';
import MainLayout from '../layouts/MainLayout';
import { payBill, getAccountBillPayments, clearError, clearSuccess } from '../store/billSlice';
import { getUserAccounts } from '../store/accountSlice';
import { useAuth } from '../hooks/useCustomHooks';
import { formatCurrency, formatDateShort, getStatusBadge } from '../utils/formatters';

const BillPaymentPage = () => {
  const dispatch = useDispatch();
  const { user } = useAuth();
  const { accounts } = useSelector((state) => state.account);
  const { bills, loading, error, successMessage, pagination } = useSelector((state) => state.bill);

  const [selectedAccount, setSelectedAccount] = useState('');
  const [billType, setBillType] = useState('ELECTRICITY');
  const [billerName, setBillerName] = useState('');
  const [billAmount, setBillAmount] = useState('');
  const [billerReference, setBillerReference] = useState('');
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);

  const billTypes = [
    'ELECTRICITY',
    'WATER',
    'GAS',
    'INTERNET',
    'MOBILE',
    'CREDIT_CARD',
    'INSURANCE',
    'LOAN'
  ];

  useEffect(() => {
    // Fetch user accounts
    if (user?.userId) {
      dispatch(getUserAccounts({ userId: user.userId }));
    }
  }, [dispatch, user]);

 useEffect(() => {
  // Fetch bill payments for selected account whenever the account changes or page loads
  if (selectedAccount) {
    console.log("Refreshing bill history for account:", selectedAccount);
    dispatch(getAccountBillPayments({ 
      accountId: selectedAccount, 
      page: page, 
      size: rowsPerPage 
    }));
  }
}, [dispatch, selectedAccount, page, rowsPerPage]);

  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage);
      dispatch(clearSuccess());
      // Refresh bills
      if (selectedAccount) {
        dispatch(getAccountBillPayments({ accountId: selectedAccount, page: 0, size: rowsPerPage }));
      }
      // Refresh account balance
      if (user?.userId) {
        dispatch(getUserAccounts({ userId: user.userId }));
      }
      // Reset form
      resetForm();
    }
  }, [successMessage, dispatch]);

  useEffect(() => {
    if (error) {
      toast.error(error);
      dispatch(clearError());
    }
  }, [error, dispatch]);

  const resetForm = () => {
    setBillerName('');
    setBillAmount('');
    setBillerReference('');
  };

  const handlePayBill = async (e) => {
    e.preventDefault();

    if (!selectedAccount || !billType || !billerName || !billAmount) {
      toast.error('Please fill all required fields');
      return;
    }

    const billPaymentData = {
      accountId: selectedAccount,
      billType,
      billerName,
      billAmount: parseFloat(billAmount),
      billerReferenceNumber: billerReference || null,
    };

    dispatch(payBill(billPaymentData));
  };

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  const selectedAccountData = accounts.find((acc) => acc.accountId === parseInt(selectedAccount));

  return (
    <MainLayout>
      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Typography variant="h4" gutterBottom sx={{ mb: 4, fontWeight: 'bold', color: '#1F3A93' }}>
          Pay Bills
        </Typography>

        <Grid container spacing={3}>
          {/* Payment Form */}
          <Grid item xs={12} md={6}>
            <Paper sx={{ p: 3, mb: 3 }}>
              <Typography variant="h6" gutterBottom>
                Pay Your Bills
              </Typography>

              {selectedAccountData && (
                <Card sx={{ mb: 3, backgroundColor: '#F5F6FA' }}>
                  <CardContent>
                    <Typography variant="body2" color="textSecondary">
                      Selected Account
                    </Typography>
                    <Typography variant="h6">
                      {selectedAccountData.accountNumber}
                    </Typography>
                    <Typography variant="body2" color="textSecondary">
                      Balance: {formatCurrency(selectedAccountData.balance)}
                    </Typography>
                  </CardContent>
                </Card>
              )}

              <Box component="form" onSubmit={handlePayBill} noValidate>
                <FormControl fullWidth margin="normal" required>
                  <InputLabel>Select Account</InputLabel>
                  <Select
                    value={selectedAccount}
                    onChange={(e) => setSelectedAccount(e.target.value)}
                    label="Select Account"
                  >
                    {accounts.map((account) => (
                      <MenuItem key={account.accountId} value={account.accountId}>
                        {account.accountNumber} - {formatCurrency(account.balance)}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>

                <FormControl fullWidth margin="normal" required>
                  <InputLabel>Bill Type</InputLabel>
                  <Select
                    value={billType}
                    onChange={(e) => setBillType(e.target.value)}
                    label="Bill Type"
                  >
                    {billTypes.map((type) => (
                      <MenuItem key={type} value={type}>
                        {type.replace('_', ' ')}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>

                <TextField
                  fullWidth
                  margin="normal"
                  label="Biller Name"
                  value={billerName}
                  onChange={(e) => setBillerName(e.target.value)}
                  required
                  placeholder="e.g., Power Company"
                />

                <TextField
                  fullWidth
                  margin="normal"
                  label="Bill Amount"
                  type="number"
                  value={billAmount}
                  onChange={(e) => setBillAmount(e.target.value)}
                  required
                  inputProps={{ step: '0.01', min: '0' }}
                  placeholder="Enter amount"
                  startAdornment="₹"
                />

                <TextField
                  fullWidth
                  margin="normal"
                  label="Biller Reference Number (Optional)"
                  value={billerReference}
                  onChange={(e) => setBillerReference(e.target.value)}
                  placeholder="e.g., Bill Reference"
                />

                <Button
                  type="submit"
                  fullWidth
                  variant="contained"
                  sx={{
                    mt: 3,
                    backgroundColor: '#1F3A93',
                    '&:hover': { backgroundColor: '#152757' },
                  }}
                  disabled={loading || !selectedAccount}
                >
                  {loading ? <CircularProgress size={24} /> : 'Pay Bill'}
                </Button>
              </Box>
            </Paper>
          </Grid>

          {/* Bills Summary */}
          <Grid item xs={12} md={6}>
            <Paper sx={{ p: 3 }}>
              <Typography variant="h6" gutterBottom>
                Quick Tips
              </Typography>
              <Box sx={{ mb: 2, p: 2, backgroundColor: '#EEF4FF', borderRadius: 1 }}>
                <Typography variant="body2">
                  ✓ Select your account from which you want to pay the bill
                </Typography>
              </Box>
              <Box sx={{ mb: 2, p: 2, backgroundColor: '#EEF4FF', borderRadius: 1 }}>
                <Typography variant="body2">
                  ✓ Choose the bill type (electricity, water, internet, etc.)
                </Typography>
              </Box>
              <Box sx={{ mb: 2, p: 2, backgroundColor: '#EEF4FF', borderRadius: 1 }}>
                <Typography variant="body2">
                  ✓ Enter the biller name and bill amount
                </Typography>
              </Box>
              <Box sx={{ mb: 2, p: 2, backgroundColor: '#EEF4FF', borderRadius: 1 }}>
                <Typography variant="body2">
                  ✓ Amount will be deducted from your selected account
                </Typography>
              </Box>
              <Box sx={{ p: 2, backgroundColor: '#FFF3CD', borderRadius: 1 }}>
                <Typography variant="body2" sx={{ fontWeight: 'bold' }}>
                  ⚠ Ensure you have sufficient balance before paying
                </Typography>
              </Box>
            </Paper>
          </Grid>
        </Grid>

        {/* Bills History */}
        <Paper sx={{ mt: 4 }}>
          <Box sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Bill Payment History
            </Typography>

            {loading ? (
              <Box sx={{ display: 'flex', justifyContent: 'center', py: 4 }}>
                <CircularProgress />
              </Box>
            ) : bills.length === 0 ? (
              <Alert severity="info">No bill payments found</Alert>
            ) : (
              <>
                <TableContainer>
                  <Table>
                    <TableHead>
                      <TableRow sx={{ backgroundColor: '#F5F6FA' }}>
                        <TableCell>Date</TableCell>
                        <TableCell>Bill Type</TableCell>
                        <TableCell>Biller</TableCell>
                        <TableCell align="right">Amount</TableCell>
                        <TableCell>Status</TableCell>
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      {bills.map((bill) => (
                        <TableRow key={bill.billPaymentId} hover>
                          <TableCell>{formatDateShort(bill.createdAt)}</TableCell>
                          <TableCell>{bill.billType.replace('_', ' ')}</TableCell>
                          <TableCell>{bill.billerName}</TableCell>
                          <TableCell align="right">
                            {formatCurrency(bill.billAmount)}
                          </TableCell>
                          <TableCell>
                            <Chip
                              label={bill.status}
                              size="small"
                              color={bill.status === 'COMPLETED' ? 'success' : 'default'}
                            />
                          </TableCell>
                        </TableRow>
                      ))}
                    </TableBody>
                  </Table>
                </TableContainer>

                <TablePagination
                  rowsPerPageOptions={[5, 10, 25]}
                  component="div"
                  count={pagination.totalElements}
                  rowsPerPage={rowsPerPage}
                  page={page}
                  onPageChange={handleChangePage}
                  onRowsPerPageChange={handleChangeRowsPerPage}
                />
              </>
            )}
          </Box>
        </Paper>
      </Container>
    </MainLayout>
  );
};

export default BillPaymentPage;
