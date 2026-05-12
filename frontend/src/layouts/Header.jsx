import React from 'react';
import { AppBar, Toolbar, Typography, Button, Box, Avatar, Menu, MenuItem } from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useCustomHooks';
import { getInitials } from '../utils/formatters';

const Header = () => {
  const navigate = useNavigate();
  const { user, logout } = useAuth();
  const [anchorEl, setAnchorEl] = React.useState(null);

  const handleMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const handleProfile = () => {
    navigate('/profile');
    handleMenuClose();
  };

  const handleSettings = () => {
    navigate('/settings');
    handleMenuClose();
  };

  return (
    <AppBar position="static">
      <Toolbar>
        <Link to="/" style={{ textDecoration: 'none', color: 'inherit', flexGrow: 1 }}>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
            <Typography variant="h6" component="div" sx={{ fontWeight: 700 }}>
              🏦 SNL Bank
            </Typography>
          </Box>
        </Link>

        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          <Button color="inherit" component={Link} to="/dashboard">
            Dashboard
          </Button>
          <Button color="inherit" component={Link} to="/accounts">
            Accounts
          </Button>
          <Button color="inherit" component={Link} to="/transfers">
            Transfers
          </Button>
          <Button color="inherit" component={Link} to="/bills">
            Bills
          </Button>
          <Button color="inherit" component={Link} to="/loans">
            Loans
          </Button>
          <Button color="inherit" component={Link} to="/support">
            Support
          </Button>

          {user && (
            <>
              <Button
                color="inherit"
                onClick={handleMenuOpen}
                sx={{ textTransform: 'none' }}
              >
                <Avatar sx={{ bgcolor: '#FFA500', width: 32, height: 32, marginRight: 1 }}>
                  {getInitials(user.firstName, user.lastName)}
                </Avatar>
                <Typography variant="body2">{user.firstName}</Typography>
              </Button>

              <Menu
                anchorEl={anchorEl}
                open={Boolean(anchorEl)}
                onClose={handleMenuClose}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
                transformOrigin={{ vertical: 'top', horizontal: 'right' }}
              >
                <MenuItem onClick={handleProfile}>My Profile</MenuItem>
                <MenuItem onClick={handleSettings}>Settings</MenuItem>
                <MenuItem onClick={handleLogout}>Logout</MenuItem>
              </Menu>
            </>
          )}
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Header;
