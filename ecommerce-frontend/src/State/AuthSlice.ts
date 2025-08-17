import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { api } from "../config/api";
import { User } from "../types/userTypes";

export const sendLoginSignupOtp = createAsyncThunk(
  "/auth/sendLoginSignupOtp",
  async ({ email }: { email: string }, { rejectWithValue }) => {
    try {
      const res = await api.post("/auth/send/login-signup-otp", { email });
      console.log("login otp response", res);
    } catch (error) {
      console.log("error------", error);
    }
  }
);

export const signin = createAsyncThunk<any, any>(
  "/auth/signin",
  async (loginRequest, { rejectWithValue }) => {
    try {
      const res = await api.post("/auth/signing", loginRequest);
      console.log("login otp response", res.data);
      localStorage.setItem("jwt", res.data.jwt);
      return res.data.jwt;
    } catch (error) {
      console.log("error------", error);
    }
  }
);

export const signup = createAsyncThunk<any, any>(
  "/auth/signup",
  async (signupRequest, { rejectWithValue }) => {
    try {
      const res = await api.post("/auth/signup", signupRequest);
      console.log("login otp response", res.data);
      localStorage.setItem("jwt", res.data.jwt);
      return res.data.jwt;
    } catch (error) {
      console.log("error------", error);
    }
  }
);

export const fetchUserProfile = createAsyncThunk<any, any>(
  "/auth/fetchUserProfile",
  async ({jwt}, { rejectWithValue }) => {
    try {
      const res = await api.get("/users/profile", {
        headers: {
          Authorization: `Bearer ${jwt}`
        }
      });
      console.log("user profile", res.data);
      return res.data;
    } catch (error) {
      console.log("error------", error);
    }
  }
);


export const logout = createAsyncThunk<any, any>(
  "/auth/logout",
  async (navigate, { rejectWithValue }) => {
    try {
      localStorage.clear();
      navigate("/");
    } catch (error) {}
  }
);

interface AuthSate {
  jwt: any;
  otpSent: boolean;
  isLoggedIn: boolean;
  user: User | null;
  loading:boolean
}

const initialState: AuthSate = {
  jwt: null,
  otpSent: false,
  isLoggedIn: false,
  user: null,
  loading:false
};

const authSlice = createSlice({
  name: "auth",
  initialState: initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder.addCase(sendLoginSignupOtp.pending, (state) => {
      state.loading = true;
    })
    builder.addCase(sendLoginSignupOtp.fulfilled, (state) => {
      state.otpSent = true;
      state.loading = false;
    })
    builder.addCase(sendLoginSignupOtp.rejected, (state) => {
      state.loading = false;
    })
    builder.addCase(signin.fulfilled, (state, action) => {
      state.jwt = action.payload;
      state.isLoggedIn = true;
    });
    builder.addCase(signup.fulfilled, (state, action) => {
      state.jwt = action.payload;
      state.isLoggedIn = true;
    });
    builder.addCase(fetchUserProfile.fulfilled, (state, action) => {
      state.user = action.payload;
      state.isLoggedIn = true
    });
    builder.addCase(logout.fulfilled, (state) =>{
      state.isLoggedIn = false;
      state.jwt = null;
      state.user = null;
    })
  },
});

export default authSlice.reducer;