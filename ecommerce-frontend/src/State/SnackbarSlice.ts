import { createSlice } from "@reduxjs/toolkit"

const initialState = {
  open: false,
  message: "",
  severity: "success"
}

const SnackbarSlice = createSlice({
  name: 'snackbar',
  initialState: initialState,
  reducers:{
    showSnackbar: (state, action) => {
      state.open = true;
      state.message = action.payload.message;
      state.severity = action.payload.severity;
    },
    hideSnackbar: (state) => {
      state.open = false;
      state.message = "";
    }
  },

})


export const { showSnackbar, hideSnackbar } = SnackbarSlice.actions;
export default SnackbarSlice.reducer;
