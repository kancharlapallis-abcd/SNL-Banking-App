import { configureStore } from '@reduxjs/toolkit';
import authReducer from './authSlice';
import accountReducer from './accountSlice';
import transactionReducer from './transactionSlice';
import billReducer from './billSlice';
import loanReducer from './loanSlice';
import supportReducer from './supportSlice';

const store = configureStore({
  reducer: {
    auth: authReducer,
    account: accountReducer,
    transaction: transactionReducer,
    bill: billReducer,
    loan: loanReducer,
    support: supportReducer,
  },
});

export default store;
