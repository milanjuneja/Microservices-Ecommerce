import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { Transaction } from "../../types/TransactionTypes";
import { api } from "../../config/api";

interface TransactionState {
  transactions: Transaction[];
  transaction: Transaction | null;
  loading: boolean;
  error: string | null;
}

const initialState: TransactionState = {
  transactions: [],
  transaction: null,
  loading: false,
  error: null,
};

export const fetchTransactionsBySeller = createAsyncThunk<
  Transaction[],
  string,
  { rejectValue: string }
>(
  "transactions/fetchTransactionsBySeller",
  async (jwt, { rejectWithValue }) => {
    try {
      const res = await api.get("/api/transactions/seller", {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      });
      console.log("seller transactions", res.data);
      return res.data;
    } catch (error) {
      console.log("error in fetchTransactionsBySeller", error);
      return rejectWithValue("can not fetch transactions");
    }
  }
);

const transactionSlice = createSlice({
  name: "transactions",
  initialState: initialState,
  reducers: {},
  extraReducers: (buider) => {
    buider
      .addCase(fetchTransactionsBySeller.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchTransactionsBySeller.fulfilled, (state, action) => {
        state.loading = false;
        state.transactions = action.payload;
      })
      .addCase(fetchTransactionsBySeller.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });
  },
});

export default transactionSlice.reducer;
