import { useDispatch } from "react-redux";
import { Snackbar, Alert } from "@mui/material";
import { useAppSelector } from "../State/Store";
import { hideSnackbar } from "./../State/SnackbarSlice";

const GlobalSnackbar = () => {
  const dispatch = useDispatch();
  const { snackbar } = useAppSelector((store) => store);

  const validSeverity: "success" | "error" | "warning" | "info" = [
    "success",
    "error",
    "warning",
    "info",
  ].includes(snackbar.severity)
    ? (snackbar.severity as "success" | "error" | "warning" | "info")
    : "info";

  return (
    <Snackbar
      open={snackbar.open}
      autoHideDuration={3000}
      onClose={() => dispatch(hideSnackbar())}
      anchorOrigin={{ vertical: "top", horizontal: "right" }}
    >
      <Alert
        onClose={() => dispatch(hideSnackbar())}
        severity={validSeverity}
        variant="filled"
      >
        {snackbar.message}
      </Alert>
    </Snackbar>
  );
};

export default GlobalSnackbar;
