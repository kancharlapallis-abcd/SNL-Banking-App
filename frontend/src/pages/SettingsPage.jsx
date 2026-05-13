import React from 'react';
import { Container, Typography, Paper, Box, Divider } from '@mui/material';
import MainLayout from '../layouts/MainLayout';

const SettingsPage = () => {
  return (
    <MainLayout>
      <Container maxWidth="md" sx={{ py: 4 }}>
        <Typography variant="h4" sx={{ mb: 4, fontWeight: 'bold' }}>
          Settings
        </Typography>
        <Paper sx={{ p: 4 }}>
          <Box sx={{ mb: 3 }}>
            <Typography variant="h6">Security Settings</Typography>
            <Typography variant="body2" color="textSecondary">
              Manage your password and two-factor authentication.
            </Typography>
          </Box>
          <Divider sx={{ mb: 3 }} />
          <Box>
            <Typography variant="h6">Preferences</Typography>
            <Typography variant="body2" color="textSecondary">
              Configure notifications and display settings.
            </Typography>
          </Box>
        </Paper>
      </Container>
    </MainLayout>
  );
};

export default SettingsPage;