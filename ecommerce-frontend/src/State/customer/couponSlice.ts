import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";
import { Cart } from "../../types/CartTypes";
import { api } from "../../config/api";
import { CouponState } from "../../types/CouponTypes";

export const applyCoupon = createAsyncThunk<
  Cart,
  {
    apply: string;
    code: string;
    orderValue: number;
    jwt: string;
  },
  { rejectValue: string }
>(
  "coupon/applyCoupon",
  async ({ apply, code, orderValue, jwt }, { rejectWithValue }) => {
    try {
      const res = await api.post("/api/coupons/apply", null, {
        params: { apply, code, orderValue },
        headers: { Authorization: `Bearer ${jwt}` },
      });
      console.log("apply coupon", res.data);
      return res.data;
    } catch (error) {
      console.log("Error in apply coupon", error);
      return rejectWithValue("Cannot apply coupon");
    }
  }
);


const initialState: CouponState = {
  coupons: [],
  cart: null,
  loading: false,
  error: null,
  couponCreated: false,
  couponApplied: false
}

const couponSlice = createSlice({
  name: "coupon",
  initialState: initialState,
  reducers : {},
  extraReducers: (builder) => {
    builder.addCase(applyCoupon.pending, (state)=> {
      state.loading = true;
      state.error = null;
      state.couponApplied = false;
    })

    builder.addCase(applyCoupon.fulfilled, (state, action)=> {
      state.loading = false;
      state.cart = action.payload;
      if(action.meta.arg.apply === 'true') state.couponApplied = true;
    })
    builder.addCase(applyCoupon.rejected, (state, action : PayloadAction<string | undefined>)=> {
      state.loading = false;
      state.error = action.payload || "Failed to apply coupon";
      state.couponApplied=false;
    })
  }
});

export default couponSlice.reducer;