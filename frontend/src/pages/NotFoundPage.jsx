import React from 'react';
import { Box, Button, Typography, Container } from '@mui/material';
import { Link } from 'react-router-dom';

const NotFoundPage = () => {
  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        background: 'linear-gradient(135deg, #1F3A93 0%, #3D5AF0 100%)',
      }}
    >
      <Container maxWidth="sm" sx={{ textAlign: 'center', color: 'white' }}>
        <Typography variant="h1" sx={{ fontSize: '8rem', fontWeight: 700, mb: 2 }}>
          404
        </Typography>
        <Typography variant="h4" sx={{ mb: 2 }}>
          Page Not Found
        </Typography>
        <Typography variant="body1" sx={{ mb: 4, opacity: 0.9 }}>
          The page you're looking for doesn't exist or has been moved.
        </Typography>
        <Button variant="contained" component={Link} to="/dashboard" sx={{ backgroundColor: 'white', color: '#1F3A93' }}>
          Back to Dashboard
        </Button>
      </Container>
    </Box>
  );
};

export default NotFoundPage;
