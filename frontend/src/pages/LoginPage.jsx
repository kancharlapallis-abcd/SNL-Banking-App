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
} from '@mui/material';
import { Visibility, VisibilityOff } from '@mui/icons-material';
import { Formik, Form } from 'formik';
import * as yup from 'yup';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { loginUser, clearError } from '../store/authSlice';

const LoginPage = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { loading, error } = useSelector((state) => state.auth);
  const [showPassword, setShowPassword] = React.useState(false);

  React.useEffect(() => {
    return () => {
      dispatch(clearError());
    };
  }, [dispatch]);

  const validationSchema = yup.object({
    email: yup.string('Enter your email').email('Enter a valid email').required('Email is required'),
    password: yup.string('Enter your password').required('Password is required'),
  });

  const handleSubmit = async (values, { setSubmitting }) => {
    try {
      const result = await dispatch(loginUser({ email: values.email, password: values.password }));
      if (result.payload) {
        navigate('/dashboard');
      }
    } finally {
      setSubmitting(false);
    }
  };

  const handleClickShowPassword = () => {
    setShowPassword(!showPassword);
  };

  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        background: 'linear-gradient(135deg, #1F3A93 0%, #3D5AF0 100%)',
      }}
    >
      <Container maxWidth="sm">
        <Card sx={{ borderRadius: 2, boxShadow: '0 8px 32px rgba(0, 0, 0, 0.2)' }}>
          <CardContent sx={{ p: 4 }}>
            <Box sx={{ mb: 3, textAlign: 'center' }}>
              <Typography variant="h3" sx={{ mb: 1, color: '#1F3A93' }}>
                🏦 SNL Bank
              </Typography>
              <Typography variant="h6" color="textSecondary">
                Welcome Back
              </Typography>
            </Box>

            {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

            <Formik
              initialValues={{ email: '', password: '' }}
              validationSchema={validationSchema}
              onSubmit={handleSubmit}
            >
              {({ values, errors, touched, handleChange, handleBlur, isSubmitting }) => (
                <Form>
                  <TextField
                    fullWidth
                    id="email"
                    name="email"
                    label="Email Address"
                    type="email"
                    placeholder="you@example.com"
                    value={values.email}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    error={touched.email && Boolean(errors.email)}
                    helperText={touched.email && errors.email}
                    margin="normal"
                    variant="outlined"
                  />

                  <TextField
                    fullWidth
                    id="password"
                    name="password"
                    label="Password"
                    type={showPassword ? 'text' : 'password'}
                    placeholder="Enter your password"
                    value={values.password}
                    onChange={handleChange}
                    onBlur={handleBlur}
                    error={touched.password && Boolean(errors.password)}
                    helperText={touched.password && errors.password}
                    margin="normal"
                    variant="outlined"
                    InputProps={{
                      endAdornment: (
                        <InputAdornment position="end">
                          <IconButton
                            onClick={handleClickShowPassword}
                            edge="end"
                          >
                            {showPassword ? <VisibilityOff /> : <Visibility />}
                          </IconButton>
                        </InputAdornment>
                      ),
                    }}
                  />

                  <Button
                    fullWidth
                    variant="contained"
                    type="submit"
                    disabled={isSubmitting || loading}
                    sx={{ mt: 3, mb: 2, py: 1.5 }}
                  >
                    {loading ? <CircularProgress size={24} /> : 'Login'}
                  </Button>

                  <Box sx={{ textAlign: 'center', mt: 2 }}>
                    <Typography variant="body2" color="textSecondary">
                      Don't have an account?{' '}
                      <Link href="/register" underline="none" sx={{ color: '#1F3A93', fontWeight: 600 }}>
                        Register Now
                      </Link>
                    </Typography>
                  </Box>

                  <Box sx={{ textAlign: 'center', mt: 1 }}>
                    <Typography variant="body2" color="textSecondary">
                      <Link href="/forgot-password" underline="none" sx={{ color: '#1F3A93' }}>
                        Forgot Password?
                      </Link>
                    </Typography>
                  </Box>
                </Form>
              )}
            </Formik>
          </CardContent>
        </Card>
      </Container>
    </Box>
  );
};

export default LoginPage;
