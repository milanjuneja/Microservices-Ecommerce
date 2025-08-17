import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { HomeCategory, HomeDate } from "../../types/HomeCategoryTypes";
import { api } from "../../config/api";

export const createHomeCategories = createAsyncThunk<HomeDate, HomeCategory[]>(
  "home/createHomeCategories",
  async (homeCategories, { rejectWithValue }) => {
    try {
      const res = await api.post("/admin/home/categories", homeCategories);
      console.log("Create home categories", res.data);
      return res.data;
    } catch (error) {
      console.log("Error in createHomeCategories", error);
      return rejectWithValue("Not able to create home category");
    }
  }
);

interface HomeState {
  homePageData: HomeDate | null;
  homeCategories: HomeCategory[];
  loading: boolean;
  error: string | null;
}
const initialState: HomeState = {
  homePageData: null,
  homeCategories: [],
  loading: false,
  error: null,
};
const homeSlice = createSlice({
  name: "home",
  initialState: initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(createHomeCategories.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(createHomeCategories.fulfilled, (state, action) => {
        state.loading = true;
        state.homePageData = action.payload;
      })
      .addCase(createHomeCategories.rejected, (state, action) => {
        state.loading = true;
        state.error = action.payload as string;
      });
  },
});

export default homeSlice.reducer;
