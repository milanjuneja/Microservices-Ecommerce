import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";
import { Wishlist, WishlistState } from "../../types/WishlistTypes";
import { api } from "../../config/api";

const initialState: WishlistState = {
  wishlist: null,
  loading: false,
  error: null,
};

export const addProductToWishlist = createAsyncThunk<
  Wishlist,
  { jwt: string; productId: number }
>(
  "wishlist/addProductToWishlist",
  async ({ jwt, productId }, { rejectWithValue }) => {
    try {
      const res = await api.post(
        `/api/wishlist/add-product/${productId}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${jwt}`,
          },
        }
      );
      console.log("Product added successfully", res.data);
      return res.data;
    } catch (error) {
      console.log("Error in addProductToWishlist", error);
      return rejectWithValue("Unable to add product to wishlist");
    }
  }
);

export const getWishlistByUserId = createAsyncThunk(
  "wishlist/getWishlistByUserId",
  async (jwt: string | null, { rejectWithValue }) => {
    try {
      const res = await api.get("/api/wishlist", {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      });
      console.log("wishlist fetched", res.data);
      return res.data;
    } catch (error) {
      console.log("Error in getWishlistByUserId", error);
      return rejectWithValue("Unable to fetch wishlist");
    }
  }
);

const wishlistSlice = createSlice({
  name: "wishlist",
  initialState: initialState,
  reducers: {
    resetWishlistState: (state) => {
      state.error = null;
      state.loading = false;
      state.wishlist = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(getWishlistByUserId.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(
        getWishlistByUserId.fulfilled,
        (state, action: PayloadAction<Wishlist>) => {
          state.loading = false;
          state.wishlist = action.payload;
        }
      )
      .addCase(
        getWishlistByUserId.rejected,
        (state, action: PayloadAction<any>) => {
          state.loading = false;
          state.error = action.payload;
        }
      )
      .addCase(addProductToWishlist.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(
        addProductToWishlist.fulfilled,
        (state, action: PayloadAction<Wishlist>) => {
          state.loading = false;
          state.wishlist = action.payload;
        }
      )
      .addCase(
        addProductToWishlist.rejected,
        (state, action: PayloadAction<any>) => {
          state.loading = false;
          state.error = action.payload;
        }
      );
  },
});

export const { resetWishlistState } = wishlistSlice.actions;
export default wishlistSlice.reducer;
