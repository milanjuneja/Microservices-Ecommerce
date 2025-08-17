import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";
import { Order, OrderItem, OrderState } from "../../types/OrderTypes";
import { api } from "../../config/api";
import { Address } from "../../types/userTypes";

const initialState: OrderState = {
  orders: [],
  orderItem: null,
  currentOrder: null,
  paymentOrder: null,
  loading: false,
  error: null,
  orderCancelled: false,
};



export const fetchUserOrderHistory = createAsyncThunk<Order[], string>(
  "orders/fetchUserOrderHistory",
  async (jwt, { rejectWithValue }) => {
    try {
      const res = await api.get("/api/orders/user", {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      });
      console.log("order history fetched", res.data);
      return res.data;
    } catch (error) {
      console.log("Error in fetch user order history", error);
      return rejectWithValue("Unable to fetch order history");
    }
  }
);

export const fetchOrderById = createAsyncThunk<
  Order,
  { orderId: number; jwt: string }
>("orders/fetchOrderById", async ({ orderId, jwt }, { rejectWithValue }) => {
  try {
    const res = await api.get(`/api/orders/${orderId}`, {
      headers: {
        Authorization: `Bearer ${jwt}`,
      },
    });
    console.log("order fetched", res.data);
    return res.data;
  } catch (error) {
    console.log("Error in fetch user order", error);
    return rejectWithValue("Unable to fetch order");
  }
});

export const createOrder = createAsyncThunk<
  any,
  { address: Address; jwt: string; paymentGateway: string }
>(
  "orders/createOrder",
  async ({ address, jwt, paymentGateway }, { rejectWithValue }) => {
    try {
      const res = await api.post(`/api/orders`, address, {
        params: { paymentMethod: paymentGateway },
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      });
      console.log("order created ", res.data);
      if (res.data.payment_link_id)
        window.location.href = res.data.payment_link_id;
      return res.data;
    } catch (error) {
      console.log("Error in create Order", error);
      return rejectWithValue("Unable create order");
    }
  }
);

export const fetchOrderItemById = createAsyncThunk<
  OrderItem,
  { orderItemId: number; jwt: string }
>(
  "orders/fetchOrderItemById",
  async ({ orderItemId, jwt }, { rejectWithValue }) => {
    try {
      const res = await api.get(`/api/orders/item/${orderItemId}`, {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      });
      console.log("order item fetch ", res.data);
      return res.data;
    } catch (error) {
      console.log("Error in fetchOrderItemById", error);
      return rejectWithValue("Unable to fetch order item");
    }
  }
);

export const paymentSuccess = createAsyncThunk<
  any,
  { paymentId: string; jwt: string; paymentLinkId: string },
  { rejectValue: string }
>(
  "orders/paymentSuccess",
  async ({ paymentId, jwt, paymentLinkId }, { rejectWithValue }) => {
    try {
      const res = await api.get(`/api/payment/${paymentId}`, {
        params: { paymentLinkId },
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      });
      console.log("Payment Success ", res.data);
      return res.data;
    } catch (error) {
      console.log("Error in paymentSuccess", error);
      return rejectWithValue("Unable to get payment ");
    }
  }
);

export const cancelOrder = createAsyncThunk<Order, any>(
  "orders/cancelOrder",
  async ({ orderId, jwt }, { rejectWithValue }) => {
    try {
      const res = await api.put(`/api/orders/cancel/${orderId}`,null, {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      });
      console.log("cancel order ", res.data);
      return res.data;
    } catch (error) {
      console.log("Error in cancelOrder", error);
      return rejectWithValue("Unable cancel order ");
    }
  }
);

const orderSlice = createSlice({
  name: "orders",
  initialState: initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchUserOrderHistory.pending, (state) => {
        state.loading = true;
        state.error = null;
        state.orderCancelled = false;
      })

      .addCase(fetchUserOrderHistory.fulfilled, (state, action: PayloadAction<Order[]>) => {
        state.loading = false;
        state.orders = action.payload
      })
      .addCase(fetchUserOrderHistory.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })

      //  fetch order by ID
      .addCase(fetchOrderById.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchOrderById.fulfilled, (state, action: PayloadAction<Order>) => {
        state.loading = false;
        state.currentOrder = action.payload;
      })
      .addCase(fetchOrderById.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      // new order
      .addCase(createOrder.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(createOrder.fulfilled, (state, action: PayloadAction<any>) => {
        state.loading = false;
        state.paymentOrder = action.payload;
      })
      .addCase(createOrder.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      // fetch order item by id
      .addCase(fetchOrderItemById.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchOrderItemById.fulfilled, (state, action) => {
        state.loading = false;
        state.orderItem = action.payload;
      })
      .addCase(fetchOrderItemById.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      // payment success
      .addCase(paymentSuccess.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(paymentSuccess.fulfilled, (state, action) => {
        state.loading = false;
        console.log('Payment successfull', action.payload);
      })
      .addCase(paymentSuccess.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      // cancel order
      .addCase(cancelOrder.pending, (state) => {
        state.loading = true;
        state.error = null;
        state.orderCancelled = false;
      })
      .addCase(cancelOrder.fulfilled, (state, action) => {
        state.loading = false;
        state.orders = state.orders.map((order) => order.id === action.payload.id ? action.payload : order)
        state.orderCancelled = true;
        state.currentOrder = action.payload;
      })
      .addCase(cancelOrder.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
  },
});
export default orderSlice.reducer;
