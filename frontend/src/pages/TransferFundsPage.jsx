import React, { useEffect } from 'react';
import {
  Box,
  Card,
  CardContent,
  TextField,
  Button,
  Typography,
  Grid,
  CircularProgress,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from '@mui/material';
import { Formik, Form } from 'formik';
import * as yup from 'yup';
import { useDispatch, useSelector } from 'react-redux';
import { transferFunds } from '../store/transactionSlice';
import { getUserAccounts } from '../store/accountSlice';
import { useAuth } from '../hooks/useCustomHooks';
import MainLayout from '../layouts/MainLayout';
import SendIcon from '@mui/icons-material/Send';
import { formatCurrency } from '../utils/formatters';
import { ToastContainer, toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';

const TransferFundsPage = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { user } = useAuth();
  const { accounts } = useSelector((state) => state.account);
  const { loading } = useSelector((state) => state.transaction);

  useEffect(() => {
    if (user?.userId) {
      dispatch(getUserAccounts(user.userId));
    }
  }, [user, dispatch]);

  const validationSchema = yup.object({
    fromAccountId: yup
      .number()
      .typeError('Select a valid source account')
      .required('From account is required'),
    toAccountNumber: yup
      .string()
      .trim()
      .required('Recipient account number is required'),
    amount: yup
      .number()
      .typeError('Amount must be a number')
      .positive('Amount must be greater than zero')
      .required('Amount is required'),
    description: yup
      .string()
      .trim()
      .min(3, 'Description must be at least 3 characters')
      .required('Description is required for the transaction'),
  });

 // src/pages/TransferFundsPage.js

// Inside TransferFundsPage.js -> handleSubmit
// Inside TransferFundsPage.js -> handleSubmit
// src/pages/TransferFundsPage.js

const handleSubmit = async (values, { setSubmitting, resetForm }) => {
  try {
    const payload = {
      fromAccountId: Number(values.fromAccountId),
      toAccountNumber: values.toAccountNumber.trim(),
      amount: Number(values.amount),
      description: values.description.trim(),
      referenceNumber: `TXN${Date.now()}`
    };

    const result = await dispatch(transferFunds(payload)).unwrap();
    
    toast.success(result.message || "Transfer Successful!");
    
    // 1. Clear the form
    resetForm();

    // 2. Wait 1 second so the user sees the success toast, then move
    setTimeout(() => {
      navigate('/dashboard');
    }, 1000);

  } catch (err) {
    toast.error(err || "Transfer failed");
  } finally {
    setSubmitting(false);
  }
};

  return (
    <MainLayout>
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" sx={{ mb: 1, fontWeight: 700 }}>
          Transfer Funds
        </Typography>
        <Typography variant="body1" color="textSecondary">
          Securely send money to any SNL Bank account
        </Typography>
      </Box>

      <Grid container spacing={3}>
        <Grid item xs={12} md={8}>
          <Card elevation={3}>
            <CardContent sx={{ p: 4 }}>
              <Formik
                initialValues={{
                  fromAccountId: '',
                  toAccountNumber: '',
                  amount: '',
                  description: '',
                }}
                validationSchema={validationSchema}
                onSubmit={handleSubmit}
              >
                {({ values, errors, touched, handleChange, handleBlur, isSubmitting }) => (
                  <Form>
                    <FormControl 
                      fullWidth 
                      sx={{ mb: 3 }} 
                      error={touched.fromAccountId && Boolean(errors.fromAccountId)}
                    >
                      <InputLabel>From Account</InputLabel>
                      <Select
                        name="fromAccountId"
                        value={values.fromAccountId}
                        onChange={handleChange}
                        onBlur={handleBlur}
                        label="From Account"
                      >
                        {accounts && accounts.length > 0 ? (
                          accounts.map((account) => (
                            <MenuItem key={account.accountId} value={account.accountId}>
                              {account.accountNumber} ({account.accountType}) - {formatCurrency(account.balance)}
                            </MenuItem>
                          ))
                        ) : (
                          <MenuItem disabled value="">No accounts available</MenuItem>
                        )}
                      </Select>
                      {touched.fromAccountId && errors.fromAccountId && (
                        <Typography variant="caption" color="error" sx={{ mt: 0.5, ml: 1.5 }}>
                          {errors.fromAccountId}
                        </Typography>
                      )}
                    </FormControl>

                    <TextField
                      fullWidth
                      id="toAccountNumber"
                      name="toAccountNumber"
                      label="Recipient Account Number"
                      value={values.toAccountNumber}
                      onChange={handleChange}
                      onBlur={handleBlur}
                      error={touched.toAccountNumber && Boolean(errors.toAccountNumber)}
                      helperText={touched.toAccountNumber && errors.toAccountNumber}
                      margin="normal"
                    />

                    <TextField
                      fullWidth
                      id="amount"
                      name="amount"
                      label="Amount (₹)"
                      type="number"
                      value={values.amount}
                      onChange={handleChange}
                      onBlur={handleBlur}
                      error={touched.amount && Boolean(errors.amount)}
                      helperText={touched.amount && errors.amount}
                      margin="normal"
                    />

                    <TextField
                      fullWidth
                      id="description"
                      name="description"
                      label="Transaction Description"
                      multiline
                      rows={2}
                      value={values.description}
                      onChange={handleChange}
                      onBlur={handleBlur}
                      error={touched.description && Boolean(errors.description)}
                      helperText={touched.description && errors.description}
                      margin="normal"
                    />

                    <Button
                      fullWidth
                      variant="contained"
                      type="submit"
                      disabled={isSubmitting || loading}
                      startIcon={loading ? <CircularProgress size={20} /> : <SendIcon />}
                      sx={{ py: 1.5, mt: 4, borderRadius: 2 }}
                    >
                      {loading ? 'Processing...' : 'Confirm Transfer'}
                    </Button>
                  </Form>
                )}
              </Formik>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} md={4}>
          <Card variant="outlined" sx={{ bgcolor: '#f8f9fa' }}>
            <CardContent>
              <Typography variant="h6" gutterBottom sx={{ fontWeight: 600 }}>
                Transaction Info
              </Typography>
              <Box sx={{ mt: 2 }}>
                <Typography variant="body2" sx={{ mb: 1 }}>• Fee: <strong>₹10.00</strong></Typography>
                <Typography variant="body2" sx={{ mb: 1 }}>• Speed: <strong>Instant</strong></Typography>
                <Typography variant="body2" sx={{ mb: 1 }}>• Limit: <strong>Up to ₹1,00,000</strong></Typography>
              </Box>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
      <ToastContainer position="bottom-right" />
    </MainLayout>
  );
};

export default TransferFundsPage;