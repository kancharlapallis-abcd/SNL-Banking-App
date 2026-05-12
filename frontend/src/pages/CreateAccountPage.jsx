import React from 'react';
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
import { createAccount, clearError, clearSuccessMessage } from '../store/accountSlice';
import { useAuth } from '../hooks/useCustomHooks';
import { useNavigate } from 'react-router-dom';
import MainLayout from '../layouts/MainLayout';
import AddIcon from '@mui/icons-material/Add';
import { ToastContainer, toast } from 'react-toastify';

const CreateAccountPage = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { user } = useAuth();
  const { loading, error, successMessage } = useSelector((state) => state.account);

  React.useEffect(() => {
    if (error) {
      toast.error(error);
      dispatch(clearError());
    }
    if (successMessage) {
      toast.success(successMessage);
      dispatch(clearSuccessMessage());
      setTimeout(() => {
        navigate('/accounts');
      }, 2000);
    }
  }, [error, successMessage, dispatch, navigate]);

  // FIX 1: Unified naming to micRCode
  const validationSchema = yup.object({
    accountType: yup.string().required('Account type is required'),
    ifscCode: yup
      .string()
      .matches(/^[A-Z]{4}0[A-Z0-9]{6}$/, 'Invalid IFSC code format (e.g. HDFC0001234)')
      .required('IFSC code is required'),
    micRCode: yup.string().nullable(), 
  });

  const handleSubmit = async (values, { setSubmitting }) => {
    console.log("Submitting Account Data:", values); // Debug log
    try {
      await dispatch(
        createAccount({
          userId: user?.userId || user?.id, // Handles different naming conventions
          accountType: values.accountType,
          ifscCode: values.ifscCode.toUpperCase(),
          micRCode: values.micRCode,
        })
      );
    } catch (err) {
      console.error("Submission error:", err);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <MainLayout>
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" sx={{ mb: 1, fontWeight: 'bold', color: '#1a237e' }}>
          Create New Account
        </Typography>
        <Typography variant="body1" color="textSecondary">
          Choose the type of account you want to open
        </Typography>
      </Box>

      <Grid container spacing={3}>
        <Grid item xs={12} md={6}>
          <Card elevation={3}>
            <CardContent sx={{ p: 4 }}>
              <Formik
                enableReinitialize // Important if user data loads late
                initialValues={{
                  accountType: '',
                  ifscCode: '',
                  micRCode: '', // FIX 2: Matched case
                }}
                validationSchema={validationSchema}
                onSubmit={handleSubmit}
              >
                {({ values, errors, touched, handleChange, handleBlur, isSubmitting }) => (
                  <Form>
                    <FormControl fullWidth sx={{ mb: 2 }} error={touched.accountType && Boolean(errors.accountType)}>
                      <InputLabel>Account Type</InputLabel>
                      <Select
                        name="accountType"
                        value={values.accountType}
                        onChange={handleChange}
                        onBlur={handleBlur}
                        label="Account Type"
                      >
                        <MenuItem value="SAVINGS">Savings Account</MenuItem>
                        <MenuItem value="CURRENT">Current Account</MenuItem>
                        <MenuItem value="SALARY">Salary Account</MenuItem>
                      </Select>
                      {touched.accountType && <Typography variant="caption" color="error" sx={{ml: 2}}>{errors.accountType}</Typography>}
                    </FormControl>

                    <TextField
                      fullWidth
                      id="ifscCode"
                      name="ifscCode"
                      label="IFSC Code"
                      placeholder="e.g., HDFC0001234"
                      value={values.ifscCode}
                      onChange={handleChange}
                      onBlur={handleBlur}
                      error={touched.ifscCode && Boolean(errors.ifscCode)}
                      helperText={touched.ifscCode && errors.ifscCode}
                      margin="normal"
                      inputProps={{ style: { textTransform: 'uppercase' } }}
                    />

                    <TextField
                      fullWidth
                      id="micRCode"
                      name="micRCode"
                      label="MICR Code (Optional)"
                      placeholder="e.g., 560002001"
                      value={values.micRCode} // FIX 3: Matched case
                      onChange={handleChange}
                      onBlur={handleBlur}
                      error={touched.micRCode && Boolean(errors.micRCode)}
                      helperText={touched.micRCode && errors.micRCode}
                      margin="normal"
                    />

                    <Button
                      fullWidth
                      variant="contained"
                      type="submit"
                      disabled={isSubmitting || loading}
                      startIcon={loading ? <CircularProgress size={20} color="inherit" /> : <AddIcon />}
                      sx={{ py: 1.5, mt: 3, backgroundColor: '#1a237e', '&:hover': { backgroundColor: '#0d47a1' } }}
                    >
                      {loading ? 'Processing...' : 'Create Account'}
                    </Button>
                  </Form>
                )}
              </Formik>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} md={6}>
          {/* Info Side Stays the Same */}
          <Card elevation={1}>
            <CardContent>
              <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>
                Account Types
              </Typography>
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                <Box sx={{ p: 2, backgroundColor: '#f0f7ff', borderRadius: 1, borderLeft: '4px solid #1a237e' }}>
                  <Typography variant="body2" sx={{ fontWeight: 600, mb: 0.5 }}>Savings Account</Typography>
                  <Typography variant="caption" color="textSecondary">Ideal for regular savings with lower transaction limits</Typography>
                </Box>
                <Box sx={{ p: 2, backgroundColor: '#f0f8f0', borderRadius: 1, borderLeft: '4px solid #2e7d32' }}>
                  <Typography variant="body2" sx={{ fontWeight: 600, mb: 0.5 }}>Current Account</Typography>
                  <Typography variant="caption" color="textSecondary">For businesses with unlimited transactions and high balance requirements</Typography>
                </Box>
                <Box sx={{ p: 2, backgroundColor: '#fff8f0', borderRadius: 1, borderLeft: '4px solid #ed6c02' }}>
                  <Typography variant="body2" sx={{ fontWeight: 600, mb: 0.5 }}>Salary Account</Typography>
                  <Typography variant="caption" color="textSecondary">Specially designed for salaried employees with zero balance option</Typography>
                </Box>
              </Box>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      <ToastContainer position="bottom-right" autoClose={3000} />
    </MainLayout>
  );
};

export default CreateAccountPage;