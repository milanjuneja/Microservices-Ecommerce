import { useAppDispatch, useAppSelector } from "../../../State/Store";
import { useFormik } from "formik";
import {
  Button,
  CircularProgress,
  TextField,
} from "@mui/material";
import { sendLoginSignupOtp, signin } from "../../../State/AuthSlice";
import GlobalSnackbar from "../../../component/GlobalSnacker";
import { showSnackbar } from "../../../State/SnackbarSlice";
import { useNavigate } from "react-router-dom";

const LoginForm = () => {
  const dispatch = useAppDispatch();
  const { auth } = useAppSelector((store) => store);
  const navigate = useNavigate();

  const formik = useFormik({
    initialValues: {
      email: "",
      otp: "",
    },
    onSubmit: (values) => {
      dispatch(signin(values));
      dispatch(showSnackbar({ message: "Logged in successfully", severity: "success" }));
      navigate('/')
    },
  });
  const handleSendOtp = () => {
    dispatch(sendLoginSignupOtp({ email: formik.values.email }));
    dispatch(showSnackbar({ message: "OTP sent successfully", severity: "success" }));
  };
  return (
    <div>
      <h1 className="text-center font-bold text-xl text-primary-color pb-8">
        Login
      </h1>
      <div className="space-y-5">
        <TextField
          fullWidth
          name="email"
          label="Email"
          value={formik.values.email}
          onChange={formik.handleChange}
          error={formik.touched.email && Boolean(formik.errors.email)}
          helperText={formik.touched.email && formik.errors.email}
        />

        {auth.otpSent && (
          <div className="space-y-5">
            <p className="font-medium text-sm opacity-50">
              Enter OTP sent to your Email
            </p>
            <TextField
              fullWidth
              name="otp"
              label="Otp"
              value={formik.values.otp}
              onChange={formik.handleChange}
              error={formik.touched.otp && Boolean(formik.errors.otp)}
              helperText={formik.touched.otp && formik.errors.otp}
            />
          </div>
        )}
        {auth.otpSent ? (
          <Button
            onClick={() => formik.handleSubmit()}
            fullWidth
            variant="contained"
            sx={{ py: "11px" }}
          >
            Login
          </Button>
        ) : (
          <Button
            onClick={handleSendOtp}
            fullWidth
            variant="contained"
            sx={{ py: "11px" }}
          >
            {auth.loading ? <CircularProgress /> : "Send otp"}
          </Button>
        )}


        
      </div>
    </div>
  );
};

export default LoginForm;
