import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { api } from "../../config/api";
import { Seller } from "../../types/SellerTypes";

export const getSellerByJwt = createAsyncThunk(
  "/sellers/getSellerByJwt",
  async (jwt: string, { rejectWithValue }) => {
    try {
      const res = await api.get("/sellers/profile", {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      });
      console.log("fetch seller profile", res.data);
      return res.data;
    } catch (error) {
      console.log("error getsellerByJWT------", error);
      return rejectWithValue("not able to fetch seller profile");
    }
  }
);

export const getAllSellers = createAsyncThunk(
  "/sellers/getAllSellers",
  async (accountStatus: string, { rejectWithValue }) => {
    try {
      const res = await api.get("/api/sellers", {
        params: {
          status: accountStatus,
        },
        headers: {
          Authorization: `Bearer ${localStorage.getItem("jwt")}`,
        },
      });
      console.log("get All Sellers profile", res.data);
      return res.data;
    } catch (error) {
      console.log("error in getAllSellers------", error);
      return rejectWithValue("not able to fetch all sellers profile");
    }
  }
);

export const updateSellerStatus = createAsyncThunk<
  Seller,
  {
    status: string;
    sellerId: number;
  },
  { rejectValue: string }
>(
  "/sellers/updateSellerStatus",
  async ({ status, sellerId }, { rejectWithValue }) => {
    try {
      const res = await api.patch(`/api/sellers/${sellerId}/status/${status}`, null, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("jwt")}`,
        },
      });
      console.log("update seller status", res.data);
      return res.data;
    } catch (error) {
      console.log("error in updateSellerStatus------", error);
      return rejectWithValue("not able to update seller status");
    }
  }
);

interface SellerState {
  sellers: Seller[];
  selectedSeller: any;
  profile: any;
  report: any;
  loading: boolean;
  error: any;
}

const initialState: SellerState = {
  sellers: [],
  selectedSeller: null,
  profile: null,
  report: null,
  loading: false,
  error: null,
};

const sellerSlice = createSlice({
  name: "sellers",
  initialState: initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(getSellerByJwt.pending, (state) => {
        state.loading = true;
      })
      .addCase(getSellerByJwt.fulfilled, (state, action) => {
        state.loading = false;
        state.profile = action.payload;
      })
      .addCase(getSellerByJwt.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      })
      .addCase(getAllSellers.pending, (state) => {
        state.loading = true;
      })
      .addCase(getAllSellers.fulfilled, (state, action) => {
        state.loading = false;
        state.sellers = action.payload;
      })
      .addCase(getAllSellers.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload;
      });
  },
});

export default sellerSlice.reducer;
