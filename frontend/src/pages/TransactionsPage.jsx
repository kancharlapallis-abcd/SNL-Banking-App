import React, { useEffect, useState } from 'react';
import {
  Box,
  Card,
  CardContent,
  Typography,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TablePagination,
  CircularProgress,
  Chip,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Paper,
} from '@mui/material';
import { useDispatch, useSelector } from 'react-redux';
import { getAccountTransactions } from '../store/transactionSlice';
import { getUserAccounts } from '../store/accountSlice';
import { useAuth } from '../hooks/useCustomHooks';
import MainLayout from '../layouts/MainLayout';
import { formatCurrency, formatDate, getTransactionStatusColor } from '../utils/formatters';
import DownloadIcon from '@mui/icons-material/Download';

const TransactionsPage = () => {
  const dispatch = useDispatch();
  const { user } = useAuth();
  const { accounts } = useSelector((state) => state.account);
  const { transactions, loading } = useSelector((state) => state.transaction);
  const [selectedAccountId, setSelectedAccountId] = useState('');
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);

 useEffect(() => {
  const id = user?.userId || user?.id;
  if (id) {
    dispatch(getUserAccounts(id));
  }
}, [user, dispatch]);

//   useEffect(() => {
//     if (selectedAccountId) {
//       dispatch(getAccountTransactions({ accountId: selectedAccountId, page, size: rowsPerPage }));
//     }
//   }, [selectedAccountId, page, rowsPerPage, dispatch]);
useEffect(() => {
  if (accounts.length > 0 && !selectedAccountId) {
    setSelectedAccountId(accounts[0].accountId);
  }
}, [accounts, selectedAccountId]);

  const handleAccountChange = (event) => {
    setSelectedAccountId(event.target.value);
    setPage(0);
  };

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  return (
    <MainLayout>
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" sx={{ mb: 1 }}>
          Transaction History
        </Typography>
        <Typography variant="body1" color="textSecondary">
          View all your transactions
        </Typography>
      </Box>

      <Card sx={{ mb: 3 }}>
        <CardContent>
          <FormControl fullWidth sx={{ maxWidth: 300 }}>
            <InputLabel>Select Account</InputLabel>
            <Select
              value={selectedAccountId}
              onChange={handleAccountChange}
              label="Select Account"
            >
              <MenuItem value="">All Accounts</MenuItem>
              {accounts.map((account) => (
                <MenuItem key={account.accountId} value={account.accountId}>
                  {account.accountNumber} ({account.accountType})
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </CardContent>
      </Card>

      <Card>
        {loading ? (
          <Box sx={{ display: 'flex', justifyContent: 'center', py: 5 }}>
            <CircularProgress />
          </Box>
        ) : transactions.length > 0 ? (
          <>
            <TableContainer>
              <Table>
                <TableHead sx={{ backgroundColor: '#f5f5f5' }}>
                  <TableRow>
                    <TableCell sx={{ fontWeight: 600 }}>Date & Time</TableCell>
                    <TableCell sx={{ fontWeight: 600 }}>Type</TableCell>
                    <TableCell sx={{ fontWeight: 600 }} align="right">Amount</TableCell>
                    <TableCell sx={{ fontWeight: 600 }}>Status</TableCell>
                    <TableCell sx={{ fontWeight: 600 }}>Reference</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {transactions.map((transaction) => (
                    <TableRow key={transaction.transactionId} hover>
                      <TableCell>{formatDate(transaction.createdAt)}</TableCell>
                      <TableCell>
                        <Typography variant="body2" sx={{ fontWeight: 500 }}>
                          {transaction.transactionType}
                        </Typography>
                      </TableCell>
                      <TableCell align="right">
                        <Typography
                          variant="body2"
                          sx={{
                            fontWeight: 600,
                            color: transaction.transactionType === 'WITHDRAWAL' ? '#f44336' : '#4CAF50',
                          }}
                        >
                          {transaction.transactionType === 'WITHDRAWAL' ? '-' : '+'}{formatCurrency(transaction.amount)}
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Chip
                          label={transaction.transactionStatus}
                          size="small"
                          sx={{
                            backgroundColor: getTransactionStatusColor(transaction.transactionStatus),
                            color: 'white',
                          }}
                        />
                      </TableCell>
                      <TableCell>
                        <Typography variant="caption" sx={{ fontFamily: 'monospace' }}>
                          {transaction.referenceNumber}
                        </Typography>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
           <TablePagination
  rowsPerPageOptions={[5, 10, 25]}
  component="div"
  // If your slice doesn't have totalElements yet, use transactions.length
  count={transactions.length} 
  rowsPerPage={rowsPerPage}
  page={page}
  onPageChange={handleChangePage}
  onRowsPerPageChange={handleChangeRowsPerPage}
            />
          </>
        ) : (
          <CardContent sx={{ textAlign: 'center', py: 5 }}>
            <Typography color="textSecondary">No transactions found</Typography>
          </CardContent>
        )}
      </Card>
    </MainLayout>
  );
};

export default TransactionsPage;
