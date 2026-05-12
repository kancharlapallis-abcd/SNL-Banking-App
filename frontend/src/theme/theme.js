import { createTheme } from '@mui/material/styles';

const theme = createTheme({
  palette: {
    primary: {
      main: '#1F3A93', // HDFC Blue
      light: '#3D5AF0',
      dark: '#1a2f73',
    },
    secondary: {
      main: '#FFA500', // Orange accent
      light: '#FFB84D',
      dark: '#CC8400',
    },
    success: {
      main: '#4CAF50',
      light: '#66BB6A',
    },
    error: {
      main: '#F44336',
      light: '#EF5350',
    },
    warning: {
      main: '#FF9800',
      light: '#FFB74D',
    },
    info: {
      main: '#2196F3',
      light: '#64B5F6',
    },
    background: {
      default: '#F5F6FA',
      paper: '#FFFFFF',
    },
    text: {
      primary: '#1a1a1a',
      secondary: '#666666',
      disabled: '#999999',
    },
    divider: '#E0E0E0',
  },
  typography: {
    fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
    h1: {
      fontSize: '2.5rem',
      fontWeight: 700,
      color: '#1F3A93',
    },
    h2: {
      fontSize: '2rem',
      fontWeight: 700,
      color: '#1F3A93',
    },
    h3: {
      fontSize: '1.75rem',
      fontWeight: 600,
      color: '#1F3A93',
    },
    h4: {
      fontSize: '1.5rem',
      fontWeight: 600,
    },
    h5: {
      fontSize: '1.25rem',
      fontWeight: 600,
    },
    h6: {
      fontSize: '1rem',
      fontWeight: 600,
    },
    body1: {
      fontSize: '1rem',
      lineHeight: 1.6,
    },
    body2: {
      fontSize: '0.875rem',
      lineHeight: 1.5,
    },
    button: {
      textTransform: 'none',
      fontWeight: 600,
      fontSize: '1rem',
    },
  },
  shape: {
    borderRadius: 8,
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          textTransform: 'none',
          fontWeight: 600,
          padding: '10px 24px',
          borderRadius: '8px',
          boxShadow: 'none',
          '&:hover': {
            boxShadow: '0 4px 12px rgba(31, 58, 147, 0.2)',
          },
        },
        contained: {
          backgroundColor: '#1F3A93',
          '&:hover': {
            backgroundColor: '#1a2f73',
          },
        },
        outlined: {
          borderColor: '#1F3A93',
          color: '#1F3A93',
          '&:hover': {
            backgroundColor: 'rgba(31, 58, 147, 0.05)',
          },
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          boxShadow: '0 2px 8px rgba(0, 0, 0, 0.1)',
          borderRadius: '8px',
        },
      },
    },
    MuiTextField: {
      styleOverrides: {
        root: {
          '& .MuiOutlinedInput-root': {
            '&:hover fieldset': {
              borderColor: '#1F3A93',
            },
          },
        },
      },
    },
    MuiAppBar: {
      styleOverrides: {
        root: {
          backgroundColor: '#1F3A93',
          boxShadow: '0 2px 8px rgba(0, 0, 0, 0.1)',
        },
      },
    },
  },
});

export default theme;
