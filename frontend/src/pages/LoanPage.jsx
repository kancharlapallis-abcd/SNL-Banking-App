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
  CircularProgress,
  Chip,
  Typography,
  Alert,
  Grid,
} from '@mui/material';
import { toast } from 'react-toastify';
import MainLayout from '../layouts/MainLayout';
import { submitLoanApplication, getUserLoans, clearError, clearSuccess } from '../store/loanSlice';
import { useAuth } from '../hooks/useCustomHooks';
import { formatCurrency } from '../utils/formatters';
import { API_ENDPOINTS } from '../constants/apiEndpoints';

const LoanPage = () => {
  const dispatch = useDispatch();
  const { user } = useAuth();
  const { loans, loading, error, successMessage, pagination } = useSelector((state) => state.loan);

  const [loanType, setLoanType] = useState('PERSONAL_LOAN');
  const [principalAmount, setPrincipalAmount] = useState('');
  const [interestRate, setInterestRate] = useState('');
  const [tenureMonths, setTenureMonths] = useState('');
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);

  const loanTypes = ['PERSONAL_LOAN', 'HOME_LOAN', 'CAR_LOAN', 'EDUCATION_LOAN'];

  // Fetch loans on mount and pagination change
  useEffect(() => {
    if (user?.userId) {
      dispatch(getUserLoans({ userId: user.userId, page, size: rowsPerPage }));
    }
  }, [dispatch, user?.userId, page, rowsPerPage]);

  // Handle Success
  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage);
      dispatch(clearSuccess());
      resetForm();
      if (user?.userId) {
        dispatch(getUserLoans({ userId: user.userId, page: 0, size: rowsPerPage }));
      }
    }
  }, [successMessage, dispatch, user?.userId, rowsPerPage]);

  // Handle Error
  useEffect(() => {
    if (error) {
      toast.error(error);
      dispatch(clearError());
    }
  }, [error, dispatch]);

  const resetForm = () => {
    setLoanType('PERSONAL_LOAN');
    setPrincipalAmount('');
    setInterestRate('');
    setTenureMonths('');
  };

  const handleSubmitLoan = async (e) => {
    if (e) e.preventDefault();
    console.log("Step 1: Function triggered");

    // Safety check for user session
    if (!user?.userId) {
      toast.error("User session not found. Please log in again.");
      return;
    }

    try {
      // Step 2: Build the payload
     const loanData = {
  // Try sending userId as a string if the backend expects a String
  userId: String(user.userId), 
  loanType: loanType, 
  principalAmount: Number(principalAmount),
  interestRate: Number(interestRate),
  tenureMonths: Number(tenureMonths)
};
      
      console.log("Step 3: Payload created:", loanData);

      // Step 4: Dispatch to Redux
      // If this line fails, check if 'submitLoanApplication' is imported correctly at the top
      await dispatch(submitLoanApplication(loanData)).unwrap();
      
      console.log("Step 5: Success!");

    } catch (err) {
      console.error("Step 6: Submission failed:", err);
      // Toast is already handled by your useEffect, but good to have here for debugging
    }
  };

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'PENDING': return 'warning';
      case 'ACTIVE': return 'success';
      case 'REJECTED': return 'error';
      default: return 'default';
    }
  };

  return (
    <MainLayout>
      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Typography variant="h4" gutterBottom sx={{ mb: 4, fontWeight: 'bold', color: '#1F3A93' }}>
          Loans
        </Typography>

        <Grid container spacing={3}>
          <Grid item xs={12} md={6}>
            <Paper sx={{ p: 3, mb: 3 }}>
              <Typography variant="h6" gutterBottom>Apply for a Loan</Typography>
              <Box component="form" onSubmit={handleSubmitLoan}>
                <FormControl fullWidth margin="normal">
                  <InputLabel>Loan Type</InputLabel>
                  <Select
                    value={loanType}
                    onChange={(e) => setLoanType(e.target.value)}
                    label="Loan Type"
                  >
                    {loanTypes.map((type) => (
                      <MenuItem key={type} value={type}>{type.replace('_', ' ')}</MenuItem>
                    ))}
                  </Select>
                </FormControl>

                <TextField
                  fullWidth
                  margin="normal"
                  label="Principal Amount"
                  type="number"
                  value={principalAmount}
                  onChange={(e) => setPrincipalAmount(e.target.value)}
                  required
                />

                <TextField
                  fullWidth
                  margin="normal"
                  label="Interest Rate (%)"
                  type="number"
                  value={interestRate}
                  onChange={(e) => setInterestRate(e.target.value)}
                  required
                />

                <TextField
                  fullWidth
                  margin="normal"
                  label="Tenure (Months)"
                  type="number"
                  value={tenureMonths}
                  onChange={(e) => setTenureMonths(e.target.value)}
                  required
                />

                <Button
                  type="submit"
                  fullWidth
                  variant="contained"
                  sx={{ mt: 3, backgroundColor: '#1F3A93', py: 1.2 }}
                  disabled={loading}
                >
                  {loading ? <CircularProgress size={24} color="inherit" /> : 'Apply for Loan'}
                </Button>
              </Box>
            </Paper>
          </Grid>

          <Grid item xs={12} md={6}>
            <Paper sx={{ p: 3 }}>
              <Typography variant="h6" gutterBottom>Loan Information</Typography>
              <Box sx={{ mb: 2, p: 2, bgcolor: '#f0f4ff', borderRadius: 1 }}>
                <Typography variant="body2">• <strong>Speed:</strong> 2-5 business days</Typography>
                <Typography variant="body2">• <strong>Eligibility:</strong> Credit {'>'} 650</Typography>
              </Box>
              <Alert severity="warning">EMI is calculated upon approval.</Alert>
            </Paper>
          </Grid>
        </Grid>

        <Paper sx={{ mt: 4, p: 2 }}>
          <Typography variant="h6" sx={{ p: 2 }}>Your Loan Applications</Typography>
          {loading && !loans.length ? (
            <Box sx={{ textAlign: 'center', py: 3 }}><CircularProgress /></Box>
          ) : loans.length === 0 ? (
            <Alert severity="info">No applications found.</Alert>
          ) : (
            <>
              <TableContainer>
                <Table>
                  <TableHead>
                    <TableRow sx={{ bgcolor: '#f5f5f5' }}>
                      <TableCell>Loan Number</TableCell>
                      <TableCell>Type</TableCell>
                      <TableCell align="right">Amount</TableCell>
                      <TableCell>Status</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
{loans.map((loan) => (
  <TableRow key={loan?.loanApplicationId || Math.random()}>
    {/* Add the '?' after loan on every line below */}
    <TableCell>{loan?.loanNumber || 'N/A'}</TableCell>
    <TableCell>{loan?.loanType?.replace('_', ' ') || 'N/A'}</TableCell>
    <TableCell align="right">
      {formatCurrency(loan?.principalAmount || 0)}
    </TableCell>
    <TableCell>
      <Chip 
        label={loan?.status || 'PENDING'} 
        color={getStatusColor(loan?.status)} 
        size="small" 
      />
    </TableCell>
  </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </TableContainer>
              <TablePagination
                component="div"
                count={pagination.totalElements}
                rowsPerPage={rowsPerPage}
                page={page}
                onPageChange={handleChangePage}
                onRowsPerPageChange={handleChangeRowsPerPage}
              />
            </>
          )}
        </Paper>
      </Container>
    </MainLayout>
  );
};

export default LoanPage;