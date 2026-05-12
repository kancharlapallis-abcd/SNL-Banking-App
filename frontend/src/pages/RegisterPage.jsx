import React from 'react';
import {
  Box,
  Card,
  CardContent,
  TextField,
  Button,
  Typography,
  Container,
  Link,
  Alert,
  CircularProgress,
  InputAdornment,
  IconButton,
  Grid,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from '@mui/material';
import { Visibility, VisibilityOff } from '@mui/icons-material';
import { Formik, Form } from 'formik';
import * as yup from 'yup';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { registerUser, clearError } from '../store/authSlice';

const RegisterPage = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { loading, error, successMessage } = useSelector((state) => state.auth);
  const [showPassword, setShowPassword] = React.useState(false);

  React.useEffect(() => {
    return () => {
      dispatch(clearError());
    };
  }, [dispatch]);

  const validationSchema = yup.object({
    firstName: yup.string('Enter your first name').required('First name is required'),
    lastName: yup.string('Enter your last name').required('Last name is required'),
    email: yup.string('Enter your email').email('Enter a valid email').required('Email is required'),
    mobileNumber: yup
      .string('Enter your mobile number')
      .matches(/^\d{10}$/, 'Mobile number must be 10 digits')
      .required('Mobile number is required'),
    panNumber: yup
      .string('Enter your PAN')
      .matches(/^[A-Z]{5}[0-9]{4}[A-Z]{1}$/, 'Invalid PAN format')
      .required('PAN is required'),
    aadharNumber: yup
      .string('Enter your Aadhar')
      .matches(/^\d{12}$/, 'Aadhar must be 12 digits')
      .required('Aadhar is required'),
    password: yup
      .string('Enter your password')
      .min(8, 'Password should be of minimum 8 characters length')
      .required('Password is required'),
    confirmPassword: yup
      .string('Confirm your password')
      .oneOf([yup.ref('password'), null], 'Passwords must match')
      .required('Confirm password is required'),
    address: yup.string('Enter your address').required('Address is required'),
    city: yup.string('Enter your city').required('City is required'),
    state: yup.string('Select your state').required('State is required'),
    pincode: yup
      .string('Enter your pincode')
      .matches(/^\d{6}$/, 'Pincode must be 6 digits')
      .required('Pincode is required'),
  });

  const handleSubmit = async (values, { setSubmitting }) => {
    try {
      const { confirmPassword, ...registerData } = values;
      const result = await dispatch(registerUser(registerData));
      if (result.payload) {
        setTimeout(() => {
          navigate('/login');
        }, 2000);
      }
    } finally {
      setSubmitting(false);
    }
  };

  const handleClickShowPassword = () => {
    setShowPassword(!showPassword);
  };

  const indianStates = [
    'Andhra Pradesh', 'Arunachal Pradesh', 'Assam', 'Bihar', 'Chhattisgarh',
    'Goa', 'Gujarat', 'Haryana', 'Himachal Pradesh', 'Jharkhand',
    'Karnataka', 'Kerala', 'Madhya Pradesh', 'Maharashtra', 'Manipur',
    'Meghalaya', 'Mizoram', 'Nagaland', 'Odisha', 'Punjab',
    'Rajasthan', 'Sikkim', 'Tamil Nadu', 'Telangana', 'Tripura',
    'Uttar Pradesh', 'Uttarakhand', 'West Bengal',
  ];

  if (successMessage) {
    return (
      <Box sx={{ minHeight: '100vh', display: 'flex', alignItems: 'center',background: 'linear-gradient(135deg, #1F3A93 0%, #3D5AF0 100%)' }}>
        <Container maxWidth="sm">
          <Card>
            <CardContent sx={{ p: 4, textAlign: 'center' }}>
              <Typography variant="h4" sx={{ color: '#4CAF50', mb: 2 }}>
                ✓ Registration Successful!
              </Typography>
              <Typography variant="body1" color="textSecondary" sx={{ mb: 3 }}>
                Your account has been created successfully. Please log in to continue.
              </Typography>
              <Button variant="contained" fullWidth onClick={() => navigate('/login')}>
                Go to Login
              </Button>
            </CardContent>
          </Card>
        </Container>
      </Box>
    );
  }

  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        background: 'linear-gradient(135deg, #1F3A93 0%, #3D5AF0 100%)',
        py: 4,
      }}
    >
      <Container maxWidth="md">
        <Card sx={{ borderRadius: 2, boxShadow: '0 8px 32px rgba(0, 0, 0, 0.2)' }}>
          <CardContent sx={{ p: 4 }}>
            <Box sx={{ mb: 3, textAlign: 'center' }}>
              <Typography variant="h3" sx={{ mb: 1, color: '#1F3A93' }}>
                🏦 SNL Bank
              </Typography>
              <Typography variant="h6" color="textSecondary">
                Create Your Account
              </Typography>
            </Box>

            {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

            <Formik
              initialValues={{
                firstName: '',
                lastName: '',
                email: '',
                mobileNumber: '',
                panNumber: '',
                aadharNumber: '',
                password: '',
                confirmPassword: '',
                address: '',
                city: '',
                state: '',
                pincode: '',
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

                    <Grid item xs={12}>
                      <TextField
                        fullWidth
                        id="email"
                        name="email"
                        label="Email Address"
                        type="email"
                        value={values.email}
                        onChange={handleChange}
                        onBlur={handleBlur}
                        error={touched.email && Boolean(errors.email)}
                        helperText={touched.email && errors.email}
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

                    <Grid item xs={12} sm={6}>
                      <TextField
                        fullWidth
                        id="panNumber"
                        name="panNumber"
                        label="PAN Number"
                        value={values.panNumber}
                     //   onChange={(e) => handleChange({ ...e, target: { ...e.target, value: e.target.value.toUpperCase() } })}
                     onChange={(e) => {
  e.target.value = e.target.value.toUpperCase();
  handleChange(e);
}} 
                     onBlur={handleBlur}
                        error={touched.panNumber && Boolean(errors.panNumber)}
                        helperText={touched.panNumber && errors.panNumber}
                        variant="outlined"
                        placeholder="AAAAA0001A"
                      />
                    </Grid>

                    <Grid item xs={12}>
                      <TextField
                        fullWidth
                        id="aadharNumber"
                        name="aadharNumber"
                        label="Aadhar Number"
                        value={values.aadharNumber}
                        onChange={handleChange}
                        onBlur={handleBlur}
                        error={touched.aadharNumber && Boolean(errors.aadharNumber)}
                        helperText={touched.aadharNumber && errors.aadharNumber}
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
                        error={touched.address && Boolean(errors.address)}
                        helperText={touched.address && errors.address}
                        variant="outlined"
                        multiline
                        rows={2}
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
                      <FormControl fullWidth error={touched.state && Boolean(errors.state)}>
                        <InputLabel>State</InputLabel>
                        <Select
                          id="state"
                          name="state"
                          value={values.state}
                          onChange={handleChange}
                          onBlur={handleBlur}
                          label="State"
                        >
                          {indianStates.map((state) => (
                            <MenuItem key={state} value={state}>{state}</MenuItem>
                          ))}
                        </Select>
                      </FormControl>
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

                    <Grid item xs={12} sm={6}>
                      <TextField
                        fullWidth
                        id="password"
                        name="password"
                        label="Password"
                        type={showPassword ? 'text' : 'password'}
                        value={values.password}
                        onChange={handleChange}
                        onBlur={handleBlur}
                        error={touched.password && Boolean(errors.password)}
                        helperText={touched.password && errors.password}
                        variant="outlined"
                      />
                    </Grid>

                    <Grid item xs={12} sm={6}>
                      <TextField
                        fullWidth
                        id="confirmPassword"
                        name="confirmPassword"
                        label="Confirm Password"
                        type={showPassword ? 'text' : 'password'}
                        value={values.confirmPassword}
                        onChange={handleChange}
                        onBlur={handleBlur}
                        error={touched.confirmPassword && Boolean(errors.confirmPassword)}
                        helperText={touched.confirmPassword && errors.confirmPassword}
                        variant="outlined"
                        InputProps={{
                          endAdornment: (
                            <InputAdornment position="end">
                              <IconButton onClick={handleClickShowPassword} edge="end">
                                {showPassword ? <VisibilityOff /> : <Visibility />}
                              </IconButton>
                            </InputAdornment>
                          ),
                        }}
                      />
                    </Grid>

                    <Grid item xs={12}>
                      <Button
                        fullWidth
                        variant="contained"
                        type="submit"
                        disabled={isSubmitting || loading}
                        sx={{ py: 1.5 }}
                      >
                        {loading ? <CircularProgress size={24} /> : 'Create Account'}
                      </Button>
                    </Grid>

                    <Grid item xs={12}>
                      <Typography variant="body2" color="textSecondary" sx={{ textAlign: 'center' }}>
                        Already have an account?{' '}
                        <Link href="/login" underline="none" sx={{ color: '#1F3A93', fontWeight: 600 }}>
                          Login Here
                        </Link>
                      </Typography>
                    </Grid>
                  </Grid>
                </Form>
              )}
            </Formik>
          </CardContent>
        </Card>
      </Container>
    </Box>
  );
};

export default RegisterPage;
