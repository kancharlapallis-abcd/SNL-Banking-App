import React, { useEffect } from 'react';
import {
  Box,
  Grid,
  Card,
  CardContent,
  Typography,
  Button,
  CircularProgress,
  Paper,
  List,
  ListItem,
  ListItemText,
  ListItemAvatar,
  Avatar,
  Chip,
} from '@mui/material';
import { useDispatch, useSelector } from 'react-redux';
import { getUserAccounts, getAccountBalance } from '../store/accountSlice';
import { getAccountTransactions } from '../store/transactionSlice';
import { useAuth } from '../hooks/useCustomHooks';
import { formatCurrency, formatDateShort, getAccountTypeColor, getTransactionStatusColor } from '../utils/formatters';
import MainLayout from '../layouts/MainLayout';
import { Link } from 'react-router-dom';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import SendIcon from '@mui/icons-material/Send';
import HistoryIcon from '@mui/icons-material/History';

const DashboardPage = () => {
  const dispatch = useDispatch();
  const { user } = useAuth();
  const { accounts, loading: accountLoading } = useSelector((state) => state.account);
  const { transactions } = useSelector((state) => state.transaction);

 /// 1. Fetch accounts to get updated balances
useEffect(() => {
  const id = user?.userId || user?.id;
  if (id) {
    dispatch(getUserAccounts(id));
  }
}, [user, dispatch]);

// 2. Fetch Transactions for ALL accounts owned by the user
useEffect(() => {
  const id = user?.userId || user?.id;
  if (id) {
    // Make sure your transactionSlice has a 'getTransactionHistory' thunk 
    // that accepts a userId or accountId
    dispatch(getAccountTransactions({ userId: id, page: 0, size: 5 }));
  }
}, [user, dispatch, accounts.length]); // Added accounts.length to re-trigger after transfer

  const totalBalance = accounts.reduce((sum, account) => sum + (account.balance || 0), 0);

  return (
    <MainLayout>
      <Box sx={{ mb: 4 }}>
    
       <Typography variant="h4" sx={{ mb: 1, fontWeight: 'bold' }}>
  Welcome, {user?.firstName || 'User'}! 
</Typography>

        <Typography variant="body1" color="textSecondary">
          Here's your banking dashboard
        </Typography>
      </Box>

      {/* Total Balance Card */}
      <Grid container spacing={3} sx={{ mb: 3 }}>
        <Grid item xs={12} sm={6} md={4}>
          <Card sx={{ background: 'linear-gradient(135deg, #1F3A93 0%, #3D5AF0 100%)', color: 'white' }}>
            <CardContent>
              <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start' }}>
                <Box>
                  <Typography color="inherit" variant="body2" sx={{ opacity: 0.8 }}>
                    Total Balance
                  </Typography>
                  <Typography variant="h4" sx={{ mt: 1, fontWeight: 700 }}>
                    {formatCurrency(totalBalance)}
                  </Typography>
                </Box>
                <TrendingUpIcon sx={{ fontSize: 40, opacity: 0.7 }} />
              </Box>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} sm={6} md={4}>
          <Card>
            <CardContent>
              <Typography color="textSecondary" variant="body2" sx={{ mb: 1 }}>
                Active Accounts
              </Typography>
              <Typography variant="h4" sx={{ fontWeight: 700 }}>
                {accounts.length}
              </Typography>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} sm={6} md={4}>
          <Card>
            <CardContent>
              <Typography color="textSecondary" variant="body2" sx={{ mb: 1 }}>
                Recent Transactions
              </Typography>
              <Typography variant="h4" sx={{ fontWeight: 700 }}>
                {transactions.length}
              </Typography>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      <Grid container spacing={3}>
        {/* Accounts Section */}
        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
                <Typography variant="h6" sx={{ fontWeight: 600 }}>
                  Your Accounts
                </Typography>
                <Button
                  size="small"
                  variant="outlined"
                  component={Link}
                  to="/accounts"
                >
                  View All
                </Button>
              </Box>

              {accountLoading ? (
                <Box sx={{ display: 'flex', justifyContent: 'center', py: 3 }}>
                  <CircularProgress />
                </Box>
              ) : accounts.length > 0 ? (
                <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                  {accounts.slice(0, 3).map((account) => (
                    <Paper
                      key={account.accountId}
                      sx={{
                        p: 2,
                        borderLeft: `4px solid ${getAccountTypeColor(account.accountType)}`,
                        '&:hover': { boxShadow: 2 },
                        cursor: 'pointer',
                      }}
                      component={Link}
                      to={`/accounts/${account.accountId}`}
                    >
                      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                        <Box>
                          <Typography variant="body2" color="textSecondary">
                            {account.accountType} Account
                          </Typography>
                          <Typography variant="body2" sx={{ fontWeight: 600, mt: 0.5 }}>
                            {account.accountNumber}
                          </Typography>
                        </Box>
                        <Box sx={{ textAlign: 'right' }}>
                          <Typography variant="h6" sx={{ fontWeight: 700 }}>
                            {formatCurrency(account.balance)}
                          </Typography>
                          <Chip
                            label={account.status}
                            size="small"
                            variant="outlined"
                            sx={{ mt: 0.5 }}
                          />
                        </Box>
                      </Box>
                    </Paper>
                  ))}
                </Box>
              ) : (
                <Typography color="textSecondary">No accounts found</Typography>
              )}

              <Button
                fullWidth
                variant="contained"
                sx={{ mt: 2 }}
                component={Link}
                to="/create-account"
              >
                Create New Account
              </Button>
            </CardContent>
          </Card>
        </Grid>

        {/* Quick Actions */}
        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Typography variant="h6" sx={{ fontWeight: 600, mb: 2 }}>
                Quick Actions
              </Typography>
              <Box sx={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 2 }}>
                <Button
                  fullWidth
                  variant="outlined"
                  startIcon={<SendIcon />}
                  component={Link}
                  to="/transfers"
                >
                  Transfer Funds
                </Button>
                <Button
                  fullWidth 
                  variant="outlined"
                  startIcon={<HistoryIcon />}
                  component={Link}
                  to="/transactions"
                >
                  View History
                </Button>
              </Box>
            </CardContent>
          </Card>

          {/* Recent Transactions */}
          <Card sx={{ mt: 3 }}>
            <CardContent>
              <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
                <Typography variant="h6" sx={{ fontWeight: 600 }}>
                  Recent Transactions
                </Typography>
                <Button
                  size="small"
                  variant="outlined"
                  component={Link}
                  to="/transactions"
                >
                  View All
                </Button>
              </Box>

             {transactions.length > 0 ? (
  <List>
    {transactions.slice(0, 3).map((transaction) => (
      // Use a safe key: transactionId or id
      <ListItem key={transaction?.transactionId || transaction?.id || Math.random()} disableGutters sx={{ mb: 1 }}>
        <ListItemAvatar>
          <Avatar sx={{ bgcolor: getTransactionStatusColor(transaction?.status || transaction?.transactionStatus) }}>
            <SendIcon />
          </Avatar>
        </ListItemAvatar>
        <ListItemText
          primary={`${transaction?.transactionType || 'Transfer'}`}
secondary={`${formatDateShort(transaction?.createdAt)} • ${formatCurrency(transaction?.amount || 0)}`}
        />
        <Chip
          label={transaction?.transactionStatus || transaction?.status || 'COMPLETED'}
          size="small"
          sx={{
            backgroundColor: getTransactionStatusColor(transaction?.transactionStatus || transaction?.status),
            color: 'white',
          }}
        />
      </ListItem>
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
    </MainLayout>
  );
};

export default DashboardPage;
