import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { HomeCategory } from "../../types/HomeCategoryTypes";
import { api } from "../../config/api";

export const updateHomeCategory = createAsyncThunk<
  HomeCategory,
  {
    id: number;
    data: HomeCategory;
  }
>(
  "homeCategory/updateHomeCategory",
  async ({ id, data }, { rejectWithValue }) => {
    try {
      const res = await api.patch(`admin/home-category/${id}`, data);
      console.log("category update", res.data);
      return res.data;
    } catch (error) {
      console.log("error in updateHomeCategory", error);
      return rejectWithValue("not able to update category");
    }
  }
);

export const fetchHomeCategories = createAsyncThunk<HomeCategory[]>(
  "homeCategory/fetchHomeCategories",
  async (_, { rejectWithValue }) => {
    try {
      const res = await api.get(`admin/home-category`);
      console.log("fetch category", res.data);
      return res.data;
    } catch (error) {
      console.log("error in fetchHomeCategories", error);
      return rejectWithValue("not able to fetch home category");
    }
  }
);

interface HomeCategoryState {
  categories: HomeCategory[];
  loading: boolean;
  error: string | null;
  categoryUpdate: boolean;
}

const initialState: HomeCategoryState = {
  categories: [],
  loading: false,
  error: null,
  categoryUpdate: false,
};

const homeCategorySlice = createSlice({
  name: "homeCategory",
  initialState: initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(updateHomeCategory.fulfilled, (state, action) => {
        state.loading = false;
        state.categoryUpdate = true;
        const index = state.categories.findIndex(
          (category) => category.id === action.payload.id
        );
        if (index !== -1) state.categories[index] = action.payload;
        else state.categories.push(action.payload);
      })
      .addCase(updateHomeCategory.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })

      .addCase(fetchHomeCategories.pending, (state) => {
        state.loading = true;
        state.error = null;
        state.categoryUpdate = false;
      })

      .addCase(fetchHomeCategories.fulfilled, (state, action) => {
        state.loading = false;
        state.categories = action.payload;
      })
      .addCase(fetchHomeCategories.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      });
  },
});

export default homeCategorySlice.reducer;
