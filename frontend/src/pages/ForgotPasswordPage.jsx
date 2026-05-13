import React, { useState } from 'react';
import { Container, Paper, Typography, TextField, Button, Box } from '@mui/material';
import { Link } from 'react-router-dom';

const ForgotPasswordPage = () => {
  const [email, setEmail] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    // For now, just a placeholder alert
    alert(`Reset link sent to: ${email}`);
  };

  return (
    <Box sx={{ 
      minHeight: '100vh', 
      display: 'flex', 
      alignItems: 'center', 
      background: 'linear-gradient(135deg, #3D5AF0 0%, #1F3A93 100%)' 
    }}>
      <Container maxWidth="sm">
        <Paper sx={{ p: 4, textAlign: 'center', borderRadius: 2 }}>
          <Typography variant="h5" gutterBottom fontWeight="bold">
            Reset Password
          </Typography>
          <Typography variant="body2" color="textSecondary" sx={{ mb: 3 }}>
            Enter your email address and we'll send you a link to reset your password.
          </Typography>
          
          <form onSubmit={handleSubmit}>
            <TextField
              fullWidth
              label="Email Address"
              variant="outlined"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              sx={{ mb: 3 }}
            />
            <Button 
              fullWidth 
              variant="contained" 
              type="submit" 
              sx={{ py: 1.5, mb: 2, backgroundColor: '#1F3A93' }}
            >
              Send Reset Link
            </Button>
          </form>

          <Link to="/login" style={{ textDecoration: 'none', color: '#1F3A93', fontSize: '0.875rem' }}>
            Back to Login
          </Link>
        </Paper>
      </Container>
    </Box>
  );
};

export default ForgotPasswordPage;