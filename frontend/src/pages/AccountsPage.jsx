import React, { useEffect, useState } from 'react';
import {
  Box,
  Grid,
  Card,
  CardContent,
  Typography,
  Button,
  CircularProgress,
  Paper,
  Chip,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from '@mui/material';
import { useDispatch, useSelector } from 'react-redux';
import { getUserAccounts, getAccountById, freezeAccount, unfreezeAccount } from '../store/accountSlice';
import { useAuth } from '../hooks/useCustomHooks';
import { formatCurrency, getAccountTypeColor } from '../utils/formatters';
import MainLayout from '../layouts/MainLayout';
import { Link } from 'react-router-dom';
import AddIcon from '@mui/icons-material/Add';

const AccountsPage = () => {
  const dispatch = useDispatch();
  const { user } = useAuth();
  const { accounts, loading } = useSelector((state) => state.account);
  const [openDialog, setOpenDialog] = useState(false);
  const [selectedAccountId, setSelectedAccountId] = useState(null);
  const [actionType, setActionType] = useState(null);

  useEffect(() => {
    if (user?.id) {
      dispatch(getUserAccounts(user.id));
    }
  }, [user, dispatch]);

  const handleOpenDialog = (accountId, action) => {
    setSelectedAccountId(accountId);
    setActionType(action);
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setSelectedAccountId(null);
    setActionType(null);
  };

  const handleConfirmAction = async () => {
    if (actionType === 'freeze') {
      dispatch(freezeAccount(selectedAccountId));
    } else if (actionType === 'unfreeze') {
      dispatch(unfreezeAccount(selectedAccountId));
    }
    handleCloseDialog();
  };

  return (
    <MainLayout>
      <Box sx={{ mb: 4, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Box>
          <Typography variant="h4" sx={{ mb: 1 }}>
            My Accounts
          </Typography>
          <Typography variant="body1" color="textSecondary">
            Manage your bank accounts
          </Typography>
        </Box>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          component={Link}
          to="/create-account"
        >
          Create Account
        </Button>
      </Box>

      {loading ? (
        <Box sx={{ display: 'flex', justifyContent: 'center', py: 5 }}>
          <CircularProgress />
        </Box>
      ) : accounts.length > 0 ? (
        <Grid container spacing={3}>
          {accounts.map((account) => (
            <Grid item xs={12} sm={6} md={4} key={account.id}>
              <Card
                sx={{
                  height: '100%',
                  display: 'flex',
                  flexDirection: 'column',
                  borderTop: `4px solid ${getAccountTypeColor(account.accountType)}`,
                  '&:hover': { boxShadow: 4 },
                }}
              >
                <CardContent sx={{ flexGrow: 1 }}>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start', mb: 2 }}>
                    <Box>
                      <Typography color="textSecondary" variant="body2" sx={{ mb: 0.5 }}>
                        {account.accountType} Account
                      </Typography>
                      <Typography variant="h6" sx={{ fontWeight: 700 }}>
                        {account.accountNumber?.slice(-4) && `****${account.accountNumber.slice(-4)}`}
                      </Typography>
                    </Box>
                    <Chip
                      label={account.status}
                      size="small"
                      color={account.status === 'ACTIVE' ? 'success' : 'error'}
                      variant="outlined"
                    />
                  </Box>

                  <Box sx={{ my: 3, p: 2, backgroundColor: '#f5f5f5', borderRadius: 1 }}>
                    <Typography color="textSecondary" variant="body2" sx={{ mb: 0.5 }}>
                      Balance
                    </Typography>
                    <Typography variant="h5" sx={{ fontWeight: 700, color: '#1F3A93' }}>
                      {formatCurrency(account.balance)}
                    </Typography>
                  </Box>

                  <Box sx={{ display: 'flex', gap: 1, mb: 2 }}>
                    <Typography variant="body2" color="textSecondary">
                      IFSC: {account.ifscCode}
                    </Typography>
                  </Box>
                </CardContent>

                <Box sx={{ p: 2, display: 'flex', gap: 1 }}>
                  <Button
                    fullWidth
                    size="small"
                    variant="outlined"
                    component={Link}
                    to={`/accounts/${account.id}`}
                  >
                    Details
                  </Button>
                  {account.status === 'ACTIVE' ? (
                    <Button
                      fullWidth
                      size="small"
                      variant="outlined"
                      color="error"
                      onClick={() => handleOpenDialog(account.id, 'freeze')}
                    >
                      Freeze
                    </Button>
                  ) : account.status === 'FROZEN' ? (
                    <Button
                      fullWidth
                      size="small"
                      variant="outlined"
                      color="success"
                      onClick={() => handleOpenDialog(account.id, 'unfreeze')}
                    >
                      Unfreeze
                    </Button>
                  ) : null}
                </Box>
              </Card>
            </Grid>
          ))}
        </Grid>
      ) : (
        <Card>
          <CardContent sx={{ textAlign: 'center', py: 5 }}>
            <Typography variant="h6" color="textSecondary" sx={{ mb: 2 }}>
              No accounts found
            </Typography>
            <Button
              variant="contained"
              component={Link}
              to="/create-account"
            >
              Create Your First Account
            </Button>
          </CardContent>
        </Card>
      )}

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

export default AccountsPage;
