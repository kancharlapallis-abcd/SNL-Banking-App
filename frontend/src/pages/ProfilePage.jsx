import React, { useEffect } from 'react';
import {
  Box,
  Card,
  CardContent,
  TextField,
  Button,
  Typography,
  Grid,
  Avatar,
  Divider,
  Alert,
} from '@mui/material';
import { Formik, Form } from 'formik';
import * as yup from 'yup';
import { useAuth } from '../hooks/useCustomHooks';
import MainLayout from '../layouts/MainLayout';
import { getInitials } from '../utils/formatters';
import { useDispatch, useSelector } from 'react-redux';
import { clearError, clearSuccessMessage } from '../store/authSlice';
import { ToastContainer, toast } from 'react-toastify';

const ProfilePage = () => {
  const dispatch = useDispatch();
  const { user } = useAuth();
  const { error, successMessage } = useSelector((state) => state.auth);

  useEffect(() => {
    if (error) {
      toast.error(error);
      dispatch(clearError());
    }
    if (successMessage) {
      toast.success(successMessage);
      dispatch(clearSuccessMessage());
    }
  }, [error, successMessage, dispatch]);

  const validationSchema = yup.object({
    firstName: yup.string('Enter your first name').required('First name is required'),
    lastName: yup.string('Enter your last name').required('Last name is required'),
    email: yup.string('Enter your email').email('Enter a valid email').required('Email is required'),
    mobileNumber: yup
      .string('Enter your mobile number')
      .matches(/^\d{10}$/, 'Mobile number must be 10 digits')
      .required('Mobile number is required'),
    city: yup.string('Enter your city').required('City is required'),
    pincode: yup
      .string('Enter your pincode')
      .matches(/^\d{6}$/, 'Pincode must be 6 digits')
      .required('Pincode is required'),
  });

  const handleSubmit = async (values, { setSubmitting }) => {
    try {
      // This would call updateUserProfile from userService
      // For now, just show success message
      toast.success('Profile updated successfully');
    } finally {
      setSubmitting(false);
    }
  };

  if (!user) {
    return (
      <MainLayout>
        <Typography>Loading...</Typography>
      </MainLayout>
    );
  }

  return (
    <MainLayout>
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" sx={{ mb: 1 }}>
          My Profile
        </Typography>
        <Typography variant="body1" color="textSecondary">
          Manage your account information
        </Typography>
      </Box>

      <Grid container spacing={3}>
        {/* Profile Summary */}
        <Grid item xs={12} md={4}>
          <Card>
            <CardContent sx={{ textAlign: 'center' }}>
              <Avatar
                sx={{
                  width: 100,
                  height: 100,
                  margin: '0 auto 20px',
                  bgcolor: '#FFA500',
                  fontSize: '2rem',
                }}
              >
                {getInitials(user.firstName, user.lastName)}
              </Avatar>
              <Typography variant="h6" sx={{ fontWeight: 600 }}>
                {user.firstName} {user.lastName}
              </Typography>
              <Typography variant="body2" color="textSecondary">
                {user.email}
              </Typography>
              <Divider sx={{ my: 2 }} />
              <Typography variant="body2" color="textSecondary" sx={{ mb: 1 }}>
                <strong>Mobile:</strong> {user.mobileNumber}
              </Typography>
              <Typography variant="body2" color="textSecondary" sx={{ mb: 1 }}>
                <strong>PAN:</strong> {user.panNumber}
              </Typography>
              <Typography variant="body2" color="textSecondary">
                <strong>Aadhar:</strong> {user.aadharNumber?.slice(-4)}****
              </Typography>
            </CardContent>
          </Card>
        </Grid>

        {/* Edit Profile */}
        <Grid item xs={12} md={8}>
          <Card>
            <CardContent sx={{ p: 4 }}>
              <Typography variant="h6" sx={{ fontWeight: 600, mb: 3 }}>
                Edit Profile Information
              </Typography>

              <Formik
                initialValues={{
                  firstName: user.firstName || '',
                  lastName: user.lastName || '',
                  email: user.email || '',
                  mobileNumber: user.mobileNumber || '',
                  address: user.address || '',
                  city: user.city || '',
                  state: user.state || '',
                  pincode: user.pincode || '',
                }}
                validationSchema={validationSchema}
                onSubmit={handleSubmit}
              >
                {({ values, errors, touched, handleChange, handleBlur, isSubmitting }) => (
                  <Form>
                    <Grid container spacing={2}>
                      <Grid item xs={12} sm={6}>
                        <TextField
                          fullWidth
                          id="firstName"
                          name="firstName"
                          label="First Name"
                          value={values.firstName}
                          onChange={handleChange}
                          onBlur={handleBlur}
                          error={touched.firstName && Boolean(errors.firstName)}
                          helperText={touched.firstName && errors.firstName}
                          variant="outlined"
                        />
                      </Grid>

                      <Grid item xs={12} sm={6}>
                        <TextField
                          fullWidth
                          id="lastName"
                          name="lastName"
                          label="Last Name"
                          value={values.lastName}
                          onChange={handleChange}
                          onBlur={handleBlur}
                          error={touched.lastName && Boolean(errors.lastName)}
                          helperText={touched.lastName && errors.lastName}
                          variant="outlined"
                        />
                      </Grid>

                      <Grid item xs={12} sm={6}>
                        <TextField
                          fullWidth
                          id="email"
                          name="email"
                          label="Email"
                          type="email"
                          value={values.email}
                          onChange={handleChange}
                          onBlur={handleBlur}
                          error={touched.email && Boolean(errors.email)}
                          helperText={touched.email && errors.email}
                          disabled
                          variant="outlined"
                        />
                      </Grid>

                      <Grid item xs={12} sm={6}>
                        <TextField
                          fullWidth
                          id="mobileNumber"
                          name="mobileNumber"
                          label="Mobile Number"
                          value={values.mobileNumber}
                          onChange={handleChange}
                          onBlur={handleBlur}
                          error={touched.mobileNumber && Boolean(errors.mobileNumber)}
                          helperText={touched.mobileNumber && errors.mobileNumber}
                          variant="outlined"
                        />
                      </Grid>

                      <Grid item xs={12}>
                        <TextField
                          fullWidth
                          id="address"
                          name="address"
                          label="Address"
                          value={values.address}
                          onChange={handleChange}
                          onBlur={handleBlur}
                          multiline
                          rows={2}
                          variant="outlined"
                        />
                      </Grid>

                      <Grid item xs={12} sm={6}>
                        <TextField
                          fullWidth
                          id="city"
                          name="city"
                          label="City"
                          value={values.city}
                          onChange={handleChange}
                          onBlur={handleBlur}
                          error={touched.city && Boolean(errors.city)}
                          helperText={touched.city && errors.city}
                          variant="outlined"
                        />
                      </Grid>

                      <Grid item xs={12} sm={6}>
                        <TextField
                          fullWidth
                          id="pincode"
                          name="pincode"
                          label="Pincode"
                          value={values.pincode}
                          onChange={handleChange}
                          onBlur={handleBlur}
                          error={touched.pincode && Boolean(errors.pincode)}
                          helperText={touched.pincode && errors.pincode}
                          variant="outlined"
                        />
                      </Grid>

                      <Grid item xs={12}>
                        <Button
                          fullWidth
                          variant="contained"
                          type="submit"
                          disabled={isSubmitting}
                          sx={{ py: 1.5 }}
                        >
                          Save Changes
                        </Button>
                      </Grid>
                    </Grid>
                  </Form>
                )}
              </Formik>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      <ToastContainer position="bottom-right" autoClose={3000} />
    </MainLayout>
  );
};

export default ProfilePage;
