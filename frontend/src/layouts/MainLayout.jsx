import React from 'react';
import { Box, Container } from '@mui/material';
import Header from './Header';

const MainLayout = ({ children }) => {
  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      <Header />
      <Box sx={{ flex: 1, backgroundColor: '#F5F6FA', py: 3 }}>
        <Container maxWidth="lg">{children}</Container>
      </Box>
    </Box>
  );
};

export default MainLayout;
