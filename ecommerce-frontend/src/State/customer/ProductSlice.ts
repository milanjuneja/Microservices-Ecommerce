import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { api } from "../../config/api";
import { Product } from "../../types/ProductTypes";

export const fetchProductById = createAsyncThunk(
  "products/fetchProductById",
  async (productId:number, { rejectWithValue }) => {
    try {
      const res = await api.get(`/products/${productId}`);
      console.log("product by Id", res.data);

      return res.data;
    } catch (error: any) {
      rejectWithValue(error.message);
    }
  }
);

export const searchProduct = createAsyncThunk<any, any>(
  "products/searchProduct",
  async (query:string, { rejectWithValue }) => {
    try {
      const res = await api.get(`/products/search`, {
        params: {
          query
        },
      });
      console.log("search Product", res.data);

      return res.data;
    } catch (error: any) {
      console.log(error);
      rejectWithValue(error.message);
    }
  }
);

export const fetchAllProducts = createAsyncThunk<any, any>(
  "products/fetchAllProducts",
  async (params, { rejectWithValue }) => {
    try {
      const res = await api.get(`/products`, {
        params: {
          ...params,
          pageNumber: params.pageNumber || 0,
        },
      });
      console.log("All Products Data", res.data);

      return res.data;
    } catch (error: any) {
      console.log(error);
      rejectWithValue(error.message);
    }
  }
);

interface ProductState{
  product: Product | null,
  products: Product[],
  totalPages: number,
  loading:boolean,
  error: string | null | undefined;
  searchProduct:Product[]
}
const initialState:ProductState = {
  product: null,
  products: [],
  totalPages: 1,
  loading: false,
  error:null,
  searchProduct:[]
}

const productSlice = createSlice({
  name: "products",
  initialState: initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder.addCase(fetchProductById.pending, (state) => {
          state.loading = true;
        }),
          builder.addCase(fetchProductById.fulfilled, (state, action) => {
            state.loading = false;
            state.product = action.payload;
          }),
          builder.addCase(fetchProductById.rejected, (state, action) => {
            state.loading = false;
            state.error = action.error.message;
          }),
          builder.addCase(searchProduct.pending, (state) => {
            state.loading = true;
          }),
          builder.addCase(searchProduct.fulfilled, (state, action) => {
            state.loading = false;
            state.searchProduct = action.payload;
          }),
          builder.addCase(searchProduct.rejected, (state, action) => {
            state.loading = false;
            state.error = action.error.message;
          })

          builder.addCase(fetchAllProducts.pending, (state) => {
            state.loading = true;
          }),
          builder.addCase(fetchAllProducts.fulfilled, (state, action) => {
            state.loading = false;
            state.products = action.payload.content;
          }),
          builder.addCase(fetchAllProducts.rejected, (state, action) => {
            state.loading = false;
            state.error = action.error.message;
          })
  }
});

export default productSlice.reducer;
