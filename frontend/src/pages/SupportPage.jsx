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
  Textarea,
} from '@mui/material';
import { toast } from 'react-toastify';
import MainLayout from '../layouts/MainLayout';
import { submitSupportTicket, getUserSupportTickets, clearError, clearSuccess } from '../store/supportSlice';
import { useAuth } from '../hooks/useCustomHooks';
import { formatDateShort } from '../utils/formatters';

const SupportPage = () => {
  const dispatch = useDispatch();
  const { user } = useAuth();
  const { tickets, loading, error, successMessage, pagination } = useSelector((state) => state.support);

  const [category, setCategory] = useState('GENERAL');
  const [subject, setSubject] = useState('');
  const [description, setDescription] = useState('');
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);

  const ticketCategories = [
    'GENERAL',
    'TECHNICAL_ISSUE',
    'ACCOUNT_PROBLEM',
    'TRANSACTION_ISSUE',
    'CARD_RELATED',
    'LOAN_INQUIRY',
    'COMPLAINT',
    'FEEDBACK',
  ];

  useEffect(() => {
    // Fetch user support tickets
    if (user?.userId) {
      dispatch(getUserSupportTickets({ userId: user.userId, page, size: rowsPerPage }));
    }
  }, [dispatch, user, page, rowsPerPage]);

  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage);
      dispatch(clearSuccess());
      // Refresh tickets
      if (user?.userId) {
        dispatch(getUserSupportTickets({ userId: user.userId, page: 0, size: rowsPerPage }));
      }
      // Reset form
      resetForm();
    }
  }, [successMessage, dispatch, user]);

  useEffect(() => {
    if (error) {
      toast.error(error);
      dispatch(clearError());
    }
  }, [error, dispatch]);

  const resetForm = () => {
    setCategory('GENERAL');
    setSubject('');
    setDescription('');
  };

  const handleSubmitTicket = async (e) => {
    e.preventDefault();

    if (!subject || !description) {
      toast.error('Please fill all required fields');
      return;
    }

    const ticketData = {
      userId: user.userId,
      category,
      subject,
      description,
    };

    dispatch(submitSupportTicket(ticketData));
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
      case 'OPEN':
        return 'warning';
      case 'IN_PROGRESS':
        return 'info';
      case 'RESOLVED':
        return 'success';
      case 'CLOSED':
        return 'default';
      default:
        return 'default';
    }
  };

  return (
    <MainLayout>
      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Typography variant="h4" gutterBottom sx={{ mb: 4, fontWeight: 'bold', color: '#1F3A93' }}>
          Support & Help
        </Typography>

        <Grid container spacing={3}>
          {/* Support Ticket Form */}
          <Grid item xs={12} md={6}>
            <Paper sx={{ p: 3, mb: 3 }}>
              <Typography variant="h6" gutterBottom>
                Submit a Support Ticket
              </Typography>

              <Box component="form" onSubmit={handleSubmitTicket} noValidate>
                <FormControl fullWidth margin="normal" required>
                  <InputLabel>Category</InputLabel>
                  <Select
                    value={category}
                    onChange={(e) => setCategory(e.target.value)}
                    label="Category"
                  >
                    {ticketCategories.map((cat) => (
                      <MenuItem key={cat} value={cat}>
                        {cat.replace('_', ' ')}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>

                <TextField
                  fullWidth
                  margin="normal"
                  label="Subject"
                  value={subject}
                  onChange={(e) => setSubject(e.target.value)}
                  required
                  placeholder="Brief subject of your issue"
                />

                <TextField
                  fullWidth
                  margin="normal"
                  label="Description"
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                  required
                  multiline
                  rows={6}
                  placeholder="Please describe your issue in detail"
                />

                <Button
                  type="submit"
                  fullWidth
                  variant="contained"
                  sx={{
                    mt: 3,
                    backgroundColor: '#1F3A93',
                    '&:hover': { backgroundColor: '#152757' },
                  }}
                  disabled={loading}
                >
                  {loading ? <CircularProgress size={24} /> : 'Submit Ticket'}
                </Button>
              </Box>
            </Paper>
          </Grid>

          {/* Support Information */}
          <Grid item xs={12} md={6}>
            <Paper sx={{ p: 3 }}>
              <Typography variant="h6" gutterBottom>
                How We Can Help
              </Typography>
              <Box sx={{ mb: 2, p: 2, backgroundColor: '#EEF4FF', borderRadius: 1 }}>
                <Typography variant="body2">
                  <strong>Response Time:</strong> 24-48 hours
                </Typography>
              </Box>
              <Box sx={{ mb: 2, p: 2, backgroundColor: '#EEF4FF', borderRadius: 1 }}>
                <Typography variant="body2">
                  <strong>Available Categories:</strong> Technical, Account, Transaction, Loans, etc.
                </Typography>
              </Box>
              <Box sx={{ mb: 2, p: 2, backgroundColor: '#EEF4FF', borderRadius: 1 }}>
                <Typography variant="body2">
                  <strong>Support Hours:</strong> Monday - Sunday, 8 AM to 10 PM
                </Typography>
              </Box>
              <Box sx={{ mb: 2, p: 2, backgroundColor: '#EEF4FF', borderRadius: 1 }}>
                <Typography variant="body2">
                  <strong>Ticket Status:</strong> OPEN → IN_PROGRESS → RESOLVED
                </Typography>
              </Box>
              <Box sx={{ p: 2, backgroundColor: '#FFF3CD', borderRadius: 1 }}>
                <Typography variant="body2" sx={{ fontWeight: 'bold' }}>
                  ⚠ Please provide detailed information to help us resolve your issue faster
                </Typography>
              </Box>
            </Paper>
          </Grid>
        </Grid>

        {/* Support Tickets History */}
        <Paper sx={{ mt: 4 }}>
          <Box sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Your Support Tickets
            </Typography>

            {loading ? (
              <Box sx={{ display: 'flex', justifyContent: 'center', py: 4 }}>
                <CircularProgress />
              </Box>
            ) : tickets.length === 0 ? (
              <Alert severity="info">No support tickets found</Alert>
            ) : (
              <>
                <TableContainer>
                  <Table>
                    <TableHead>
                      <TableRow sx={{ backgroundColor: '#F5F6FA' }}>
                        <TableCell>Date</TableCell>
                        <TableCell>Category</TableCell>
                        <TableCell>Subject</TableCell>
                        <TableCell>Status</TableCell>
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      {tickets.map((ticket) => (
                        <TableRow key={ticket.ticketId} hover>
                          <TableCell>{formatDateShort(ticket.createdAt)}</TableCell>
                          <TableCell>{ticket.category.replace('_', ' ')}</TableCell>
                          <TableCell>{ticket.subject}</TableCell>
                          <TableCell>
                            <Chip
                              label={ticket.status}
                              size="small"
                              color={getStatusColor(ticket.status)}
                            />
                          </TableCell>
                        </TableRow>
                      ))}
                    </TableBody>
                  </Table>
                </TableContainer>

                <TablePagination
                  rowsPerPageOptions={[5, 10, 25]}
                  component="div"
                  count={pagination.totalElements}
                  rowsPerPage={rowsPerPage}
                  page={page}
                  onPageChange={handleChangePage}
                  onRowsPerPageChange={handleChangeRowsPerPage}
                />
              </>
            )}
          </Box>
        </Paper>
      </Container>
    </MainLayout>
  );
};

export default SupportPage;
