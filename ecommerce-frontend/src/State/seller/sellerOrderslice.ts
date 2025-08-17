import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";
import { Order, OrderStatus } from "../../types/OrderTypes";
import { api } from "../../config/api";

interface SellerOrderState {
  orders: Order[];
  loading: boolean;
  error: string | null;
}

const initialState: SellerOrderState = {
  orders: [],
  loading: false,
  error: null,
};

export const fetchSellerOrders = createAsyncThunk<Order[], string>(
  "sellerOrders/fetchSellerOrders",
  async (jwt, { rejectWithValue }) => {
    try {
      const res = await api.get("/api/seller/orders", {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      });

      console.log("Fetch seller orders", res.data);
      return res.data;
    } catch (error) {
      console.log("error in  fetchSellerOrders", error);
      return rejectWithValue("Not able to fetch order");
    }
  }
);

export const updateOrderStatus = createAsyncThunk<
  Order,
  {
    jwt: string;
    orderId: number;
    orderStatus: OrderStatus;
  }
>(
  "sellerOrders/updateOrderStatus",
  async ({ jwt, orderId, orderStatus }, { rejectWithValue }) => {
    try {
      const res = await api.patch(
        `/api/seller/orders/${orderId}/status/${orderStatus}`,
        null,
        {
          headers: {
            Authorization: `Bearer ${jwt}`,
          },
        }
      );

      console.log("update seller orders", res.data);
      return res.data;
    } catch (error) {
      console.log("error in  updateOrderStatus", error);
      return rejectWithValue("Not able to update order");
    }
  }
);

export const deleteOrder = createAsyncThunk<
  any,
  {
    jwt: string;
    orderId: number;
  }
>("sellerOrders/deleteOrder", async ({ jwt, orderId }, { rejectWithValue }) => {
  try {
    const res = await api.get(`/api/seller/orders/${orderId}/delete`, {
      headers: {
        Authorization: `Bearer ${jwt}`,
      },
    });

    console.log("delete", res.data);
    return res.data;
  } catch (error) {
    console.log("error in  deleteOrder", error);
    return rejectWithValue("Not able to delete order");
  }
});

const sellerOrderSlice = createSlice({
  name: "sellerOrders",
  initialState: initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchSellerOrders.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(
        fetchSellerOrders.fulfilled,
        (state, action: PayloadAction<Order[]>) => {
          state.loading = false;
          state.orders = action.payload;
        }
      )
      .addCase(fetchSellerOrders.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })

      .addCase(updateOrderStatus.pending, (state) => {
        state.loading = true;
        state.error = null;
      })

      .addCase(
        updateOrderStatus.fulfilled,
        (state, action: PayloadAction<Order>) => {
          state.loading = false;
          const index = state.orders.findIndex(
            (order) => order.id === action.payload.id
          );
          if (index !== -1) state.orders[index] = action.payload;
        }
      )
      .addCase(updateOrderStatus.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })

      .addCase(deleteOrder.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(deleteOrder.fulfilled, (state, action) => {
        state.loading = false;
        state.orders = state.orders.filter(
          (order) => order.id !== action.meta.arg.orderId
        );
      })
      .addCase(deleteOrder.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });
  },
});
export default sellerOrderSlice.reducer;
