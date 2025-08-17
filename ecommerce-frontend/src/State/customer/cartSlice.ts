import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";
import { Cart, CartItem } from "../../types/CartTypes";
import { api } from "../../config/api";
import { string } from "yup";
import { applyCoupon } from "./couponSlice";

interface CartState {
  cart: Cart | null;
  loading: boolean;
  error: string | null;
}

const initialState: CartState = {
  cart: null,
  loading: false,
  error: null,
};

export const fetchUserCart = createAsyncThunk<Cart, string>(
  "cart/fetchUserCart",
  async (jwt, { rejectWithValue }) => {
    try {
      const res = await api.get("/api/cart", {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      });
      console.log("user cart", res.data);
      return res.data;
    } catch (error) {
      console.log("error in fetch user cart", error);
      return rejectWithValue("Failed to fetch user cart");
    }
  }
);

interface AddItemRequest {
  productId: number | undefined;
  size: string;
  quantity: number;
}

export const additemToCart = createAsyncThunk<
  CartItem,
  {
    jwt: string | null;
    request: AddItemRequest;
  }
>("cart/additemToCart", async ({ jwt, request }, { rejectWithValue }) => {
  try {
    const res = await api.put("api/cart/add", request, {
      headers: {
        Authorization: `Bearer ${jwt}`,
      },
    });
    console.log("item added successfully", res.data);
    return res.data;
  } catch (error) {
    console.log("Error in add item to cart", error);
    return rejectWithValue("Failed to add item to cart");
  }
});

export const deleteCartItem = createAsyncThunk<
  any,
  { jwt: string; cartItemId: number }
>("cart/deleteCartItem", async ({ jwt, cartItemId }, { rejectWithValue }) => {
  try {
    const res = await api.delete(`api/cart/item/${cartItemId}`, {
      headers: {
        Authorization: `Bearer ${jwt}`,
      },
    });
    console.log("item deleted successfully", res.data);
    return res.data;
  } catch (error) {
    console.log("Error in delete cart item", error);
    return rejectWithValue("Failed to delete item to cart");
  }
});

export const updateCartItem = createAsyncThunk<
  any,
  { jwt: string | null; cartItemId: number; cartItem: any }
>(
  "cart/updateCartItem",
  async ({ jwt, cartItemId, cartItem }, { rejectWithValue }) => {
    try {
      const res = await api.put(`api/cart/item/${cartItemId}`, cartItem, {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      });
      console.log("updated successfully", res.data);
      return res.data;
    } catch (error) {
      console.log("Error in update cart item", error);
      return rejectWithValue("Failed to update item to cart");
    }
  }
);

const cartSlice = createSlice({
  name: "cart",
  initialState: initialState,
  reducers: {
    resetCartState: (state) => {
      state.cart = null;
      state.error = null;
      state.loading = false;
    },
  },
  extraReducers: (builder) => {
    builder.addCase(fetchUserCart.pending, (state) => {
      state.loading = true;
      state.error = null;
    });

    builder.addCase(
      fetchUserCart.fulfilled,
      (state, action: PayloadAction<Cart>) => {
        state.loading = false;
        state.cart = action.payload;
      }
    );
    builder.addCase(fetchUserCart.rejected, (state, action) => {
      state.loading = false;
      state.error = action.payload as string;
    });

    // add item to cart

    builder.addCase(additemToCart.pending, (state) => {
      state.loading = true;
      state.error = null;
    });
    builder.addCase(
      additemToCart.fulfilled,
      (state, action: PayloadAction<CartItem>) => {
        state.cart?.cartItems.push(action.payload);
        state.loading = false;
      }
    );
    builder.addCase(additemToCart.rejected, (state, action) => {
      state.loading = false;
      state.error = action.payload as string;
    });

    // delete item

    builder.addCase(deleteCartItem.pending, (state) => {
      state.loading = true;
      state.error = null;
    });

    builder.addCase(deleteCartItem.fulfilled, (state, action) => {
      if (state.cart) {
        state.cart.cartItems = state.cart.cartItems.filter(
          (item: CartItem) => item.id !== action.meta.arg.cartItemId
        );
        const mrpPrice = sumCartItemMrpPrice(state.cart?.cartItems || []);
        const sellingPrice = sumCartItemSellingPrice(
          state.cart?.cartItems || []
        );
        state.cart.totalSellingPrice = sellingPrice;
        state.cart.totalMrpPrice = mrpPrice;
      }
      state.loading = false;
    });
    // update item

    builder.addCase(updateCartItem.pending, (state) => {
      state.loading = true;
      state.error = null;
    });

    builder.addCase(updateCartItem.fulfilled, (state, action) => {
      if (state.cart) {
       const index = state.cart.cartItems.findIndex(item => item.id === action
        .meta.arg.cartItemId
       )
       if(index !== -1){
        state.cart.cartItems[index] = {
          ...state.cart.cartItems[index],
          ...action.payload,
        }
       }
       const mrpPrice = sumCartItemMrpPrice(state.cart?.cartItems || []);
        const sellingPrice = sumCartItemSellingPrice(
          state.cart?.cartItems || []
        );
        state.cart.totalSellingPrice = sellingPrice;
        state.cart.totalMrpPrice = mrpPrice;
      }
      state.loading = false;
    });

    builder.addCase(updateCartItem.rejected, (state, action) => {
      state.loading = false;
      state.error = action.payload as string;
    });
    builder.addCase(applyCoupon.fulfilled, (state, action) => {
      state.loading = false;
      state.cart = action.payload;
    })
  },
});
const sumCartItemMrpPrice = (cartItems: CartItem[]) => {
  return cartItems.reduce(
    (acc, item) => acc + item.mrpPrice * item.quantity,
    0
  );
};
const sumCartItemSellingPrice = (cartItems: CartItem[]) => {
  return cartItems.reduce(
    (acc, item) => acc + item.sellingPrice * item.quantity,
    0
  );
};
export default cartSlice.reducer;
export const { resetCartState } = cartSlice.actions;
