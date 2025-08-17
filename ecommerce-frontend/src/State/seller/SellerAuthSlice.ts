import { createAsyncThunk } from "@reduxjs/toolkit";
import { api } from "../../config/api";

export const sellerLogin = createAsyncThunk<any, any>(
  "/sellers/sellerLogin",
  async (loginRequest, { rejectWithValue }) => {
    try {
      const res = await api.post("/sellers/login", loginRequest );
      console.log("login otp response", res.data);
      const jwt = res.data.jwt;
      localStorage.setItem("jwt", jwt);
    } catch (error) {
      console.log("error------", error);
    }
  }
);