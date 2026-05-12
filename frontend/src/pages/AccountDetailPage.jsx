import React, { useEffect } from 'react';
import {
  Box,
  Card,
  CardContent,
  Typography,
  Grid,
  CircularProgress,
  Button,
  Chip,
  Divider,
  List,
  ListItem,
  ListItemText,
  Paper,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from '@mui/material';
import { useDispatch, useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';
import { getAccountById, freezeAccount, unfreezeAccount } from '../store/accountSlice';
import { getAccountTransactions } from '../store/transactionSlice';
import MainLayout from '../layouts/MainLayout';
import { formatCurrency, formatDate, getAccountTypeColor, getTransactionStatusColor } from '../utils/formatters';
import SendIcon from '@mui/icons-material/Send';
import { useState } from 'react';

const AccountDetailPage = () => {
  const { accountId } = useParams();
  const dispatch = useDispatch();
  const { selectedAccount, loading } = useSelector((state) => state.account);
  const { transactions } = useSelector((state) => state.transaction);
  const [openDialog, setOpenDialog] = useState(false);
  const [actionType, setActionType] = useState(null);

  useEffect(() => {
    if (accountId) {
      dispatch(getAccountById(accountId));
      dispatch(getAccountTransactions({ accountId, size: 10 }));
    }
  }, [accountId, dispatch]);

  const handleOpenDialog = (action) => {
    setActionType(action);
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setActionType(null);
  };

  const handleConfirmAction = () => {
    if (actionType === 'freeze') {
      dispatch(freezeAccount(accountId));
    } else if (actionType === 'unfreeze') {
      dispatch(unfreezeAccount(accountId));
    }
    handleCloseDialog();
  };

  if (loading || !selectedAccount) {
    return (
      <MainLayout>
        <Box sx={{ display: 'flex', justifyContent: 'center', py: 5 }}>
          <CircularProgress />
        </Box>
      </MainLayout>
    );
  }

  return (
    <MainLayout>
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" sx={{ mb: 1 }}>
          Account Details
        </Typography>
      </Box>

      <Grid container spacing={3}>
        {/* Account Summary */}
        <Grid item xs={12} md={4}>
          <Card>
            <CardContent>
              <Box sx={{ mb: 2, pb: 2, borderBottom: '1px solid #e0e0e0' }}>
                <Typography color="textSecondary" variant="body2" sx={{ mb: 1 }}>
                  Account Type
                </Typography>
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                  <Box
                    sx={{
                      width: 12,
                      height: 12,
                      borderRadius: '50%',
                      backgroundColor: getAccountTypeColor(selectedAccount.accountType),
                    }}
                  />
                  <Typography variant="h6" sx={{ fontWeight: 600 }}>
                    {selectedAccount.accountType}
                  </Typography>
                </Box>
              </Box>

              <Box sx={{ mb: 2 }}>
                <Typography color="textSecondary" variant="body2" sx={{ mb: 0.5 }}>
                  Account Number
                </Typography>
                <Typography variant="body1" sx={{ fontFamily: 'monospace', fontWeight: 600 }}>
                  {selectedAccount.accountNumber}
                </Typography>
              </Box>

              <Box sx={{ mb: 2 }}>
                <Typography color="textSecondary" variant="body2" sx={{ mb: 0.5 }}>
                  Status
                </Typography>
                <Chip
                  label={selectedAccount.status}
                  color={selectedAccount.status === 'ACTIVE' ? 'success' : 'error'}
                  variant="outlined"
                />
              </Box>

              <Box sx={{ my: 3, p: 2, backgroundColor: '#f5f5f5', borderRadius: 1 }}>
                <Typography color="textSecondary" variant="body2" sx={{ mb: 0.5 }}>
                  Current Balance
                </Typography>
                <Typography variant="h5" sx={{ fontWeight: 700, color: '#1F3A93' }}>
                  {formatCurrency(selectedAccount.balance)}
                </Typography>
              </Box>

              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                {selectedAccount.status === 'ACTIVE' ? (
                  <Button
                    fullWidth
                    variant="outlined"
                    color="error"
                    onClick={() => handleOpenDialog('freeze')}
                  >
                    Freeze Account
                  </Button>
                ) : selectedAccount.status === 'FROZEN' ? (
                  <Button
                    fullWidth
                    variant="outlined"
                    color="success"
                    onClick={() => handleOpenDialog('unfreeze')}
                  >
                    Unfreeze Account
                  </Button>
                ) : null}
              </Box>
            </CardContent>
          </Card>
        </Grid>

        {/* Account Information */}
        <Grid item xs={12} md={8}>
          <Card sx={{ mb: 3 }}>
            <CardContent>
              <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>
                Account Information
              </Typography>

              <Grid container spacing={2}>
                <Grid item xs={12} sm={6}>
                  <Paper sx={{ p: 2, backgroundColor: '#f9f9f9' }}>
                    <Typography color="textSecondary" variant="body2" sx={{ mb: 0.5 }}>
                      IFSC Code
                    </Typography>
                    <Typography variant="body1" sx={{ fontWeight: 600 }}>
                      {selectedAccount.ifscCode}
                    </Typography>
                  </Paper>
                </Grid>

                <Grid item xs={12} sm={6}>
                  <Paper sx={{ p: 2, backgroundColor: '#f9f9f9' }}>
                    <Typography color="textSecondary" variant="body2" sx={{ mb: 0.5 }}>
                      MICR Code
                    </Typography>
                    <Typography variant="body1" sx={{ fontWeight: 600 }}>
                      {selectedAccount.micRCode || 'N/A'}
                    </Typography>
                  </Paper>
                </Grid>

                <Grid item xs={12} sm={6}>
                  <Paper sx={{ p: 2, backgroundColor: '#f9f9f9' }}>
                    <Typography color="textSecondary" variant="body2" sx={{ mb: 0.5 }}>
                      Account Created
                    </Typography>
                    <Typography variant="body1" sx={{ fontWeight: 600 }}>
                      {formatDate(selectedAccount.createdAt)}
                    </Typography>
                  </Paper>
                </Grid>

                <Grid item xs={12} sm={6}>
                  <Paper sx={{ p: 2, backgroundColor: '#f9f9f9' }}>
                    <Typography color="textSecondary" variant="body2" sx={{ mb: 0.5 }}>
                      Last Updated
                    </Typography>
                    <Typography variant="body1" sx={{ fontWeight: 600 }}>
                      {formatDate(selectedAccount.updatedAt)}
                    </Typography>
                  </Paper>
                </Grid>
              </Grid>
            </CardContent>
          </Card>

          {/* Recent Transactions */}
          <Card>
            <CardContent>
              <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
                <Typography variant="h6" sx={{ fontWeight: 600 }}>
                  Recent Transactions
                </Typography>
              </Box>

              {transactions.length > 0 ? (
                <List>
                  {transactions.slice(0, 5).map((transaction, index) => (
                    <Box key={transaction.id}>
                      <ListItem disablePadding sx={{ py: 1.5 }}>
                        <Box sx={{ display: 'flex', width: '100%', alignItems: 'center', justifyContent: 'space-between' }}>
                          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                            <Box
                              sx={{
                                width: 40,
                                height: 40,
                                borderRadius: '50%',
                                backgroundColor: getTransactionStatusColor(transaction.transactionStatus),
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                color: 'white',
                              }}
                            >
                              <SendIcon sx={{ fontSize: 20 }} />
                            </Box>
                            <Box>
                              <Typography variant="body2" sx={{ fontWeight: 600 }}>
                                {transaction.transactionType}
                              </Typography>
                              <Typography variant="caption" color="textSecondary">
                                {formatDate(transaction.createdAt)}
                              </Typography>
                            </Box>
                          </Box>
                          <Box sx={{ textAlign: 'right' }}>
                            <Typography variant="body2" sx={{ fontWeight: 700 }}>
                              {formatCurrency(transaction.amount)}
                            </Typography>
                            <Chip
                              label={transaction.transactionStatus}
                              size="small"
                              sx={{
                                backgroundColor: getTransactionStatusColor(transaction.transactionStatus),
                                color: 'white',
                                mt: 0.5,
                              }}
                            />
                          </Box>
                        </Box>
                      </ListItem>
                      {index < transactions.length - 1 && <Divider />}
                    </Box>
                  ))}
                </List>
              ) : (
                <Typography color="textSecondary" variant="body2">
                  No transactions yet
                </Typography>
              )}
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* Confirmation Dialog */}
      <Dialog open={openDialog} onClose={handleCloseDialog}>
        <DialogTitle>
          {actionType === 'freeze' ? 'Freeze Account' : 'Unfreeze Account'}
        </DialogTitle>
        <DialogContent>
          <Typography sx={{ mt: 2 }}>
            Are you sure you want to {actionType} this account?
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button
            onClick={handleConfirmAction}
            variant="contained"
            color={actionType === 'freeze' ? 'error' : 'success'}
          >
            Confirm
          </Button>
        </DialogActions>
      </Dialog>
    </MainLayout>
  );
};

export default AccountDetailPage;
