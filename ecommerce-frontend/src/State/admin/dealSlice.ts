import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { DealsState } from "../../types/DealTypes";
import { api } from "../../config/api";

const initialState: DealsState = {
  dealCreated: false,
  deals: [],
  loading: false,
  error: null,
  dealUpdated: false,
};

export const createDeal = createAsyncThunk(
  "deals/createDeal",
  async (deal: any, { rejectWithValue }) => {
    try {
      const res = await api.post("/admin/deals", deal, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("jwt")}`,
        },
      });
      console.log("created deal", res.data);
      return res.data;
    } catch (error) {
      console.log("error in createDeal", error);
      return rejectWithValue("not able to create deal");
    }
  }
);

export const getAllDeals = createAsyncThunk(
  "deals/getAllDeals",
  async (_, { rejectWithValue }) => {
    try {
      const res = await api.get("/admin/deals", {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("jwt")}`,
        },
      });
      console.log("get all deals", res.data);
      return res.data;
    } catch (error) {
      console.log("error in getAllDeals", error);
      return rejectWithValue("not able to get deals");
    }
  }
);

export const deleteDeal = createAsyncThunk(
  "deals/deleteDeal",
  async (dealId: number, { rejectWithValue }) => {
    try {
      const res = await api.delete(`/admin/deals/${dealId}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("jwt")}`,
        },
      });
      console.log("delete deal", res.data);
      return res.data;
    } catch (error) {
      console.log("error in deleteDeal", error);
      return rejectWithValue("not able to delete deal");
    }
  }
);

const dealSlice = createSlice({
  name:'deal',
  initialState:initialState,
  reducers:{},
  extraReducers: (builder) => {
    builder
      .addCase(getAllDeals.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(getAllDeals.fulfilled, (state, action) => {
        state.loading = false;
        state.deals = action.payload;
        state.dealCreated = true;
      })
      .addCase(getAllDeals.rejected, (state, action) => {
        state.loading = true;
        state.error = action.payload as string;
      })
      .addCase(deleteDeal.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(deleteDeal.fulfilled, (state, action) => {
        state.loading = false;
        state.deals = state.deals.filter((item) => item.id !== action.payload.id);

      })
      .addCase(deleteDeal.rejected, (state, action) => {
        state.loading = true;
        state.error = action.payload as string;
      })
      .addCase(createDeal.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(createDeal.fulfilled, (state, action) => {
        state.loading = false;
        state.deals.push( action.payload );
        state.dealCreated = true;
      })
      .addCase(createDeal.rejected, (state, action) => {
        state.loading = true;
        state.error = action.payload as string;
      })
      
  }
}); 

export default dealSlice.reducer;