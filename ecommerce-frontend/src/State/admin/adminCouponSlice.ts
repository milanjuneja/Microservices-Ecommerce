import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { Coupon, CouponState } from "../../types/CouponTypes";
import { api } from "../../config/api";

export const createCoupon = createAsyncThunk<
  Coupon,
  { coupon: any; jwt: string },
  { rejectValue: string }
>("coupon/createCoupon", async ({ coupon, jwt }, { rejectWithValue }) => {
  try {
    const res = await api.post("/api/coupons/create", coupon, {
      headers: {
        Authorization: `Bearer ${jwt}`,
      },
    });
    console.log("create coupon", res.data);
    return res.data;
  } catch (error) {
    console.log("Error in createCoupon", error);
    return rejectWithValue("cannot create coupon");
  }
});

export const fetchAllCoupons = createAsyncThunk(
  "coupon/fetchAllCoupons",
  async (_, { rejectWithValue }) => {
    try {
      const res = await api.get("/api/coupons/all", {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("jwt")}`,
        },
      });
      console.log("fetched coupons", res.data);
      return res.data;
    } catch (error) {
      console.log("Error in fetchAllCoupons", error);
      return rejectWithValue("cannot fetch all coupon");
    }
  }
);

export const deleteCoupon = createAsyncThunk(
  "coupon/deleteCoupon",
  async (couponId: number, { rejectWithValue }) => {
    try {
      const res = await api.delete(`/api/coupons/delete/${couponId}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("jwt")}`,
        },
      });
      console.log("coupon deleted", res.data);
      return res.data;
    } catch (error) {
      console.log("Error in deleteCoupon", error);
      return rejectWithValue("cannot delete coupon");
    }
  }
);

const initialState: CouponState = {
  coupons: [],
  cart: null,
  loading: false,
  error: null,
  couponCreated: false,
  couponApplied: false,
};

const adminCouponSlice = createSlice({
  name: "adminCoupon",
  initialState: initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchAllCoupons.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchAllCoupons.fulfilled, (state, action) => {
        state.loading = false;
        state.coupons = action.payload;
      })
      .addCase(fetchAllCoupons.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      .addCase(createCoupon.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(createCoupon.fulfilled, (state, action) => {
        state.loading = false;
        state.coupons.push(action.payload);
      })
      .addCase(createCoupon.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      .addCase(deleteCoupon.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(deleteCoupon.fulfilled, (state, action) => {
        state.loading = false;
        state.coupons = state.coupons.filter(
          (item) => item.id !== action.payload.id
        );
      })
      .addCase(deleteCoupon.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });
  },
});

export default adminCouponSlice.reducer;
