import { Button, TextField } from "@mui/material";
import { useFormik } from "formik";
import { useAppDispatch } from "../../../State/Store";
import { sendLoginSignupOtp, signin } from "../../../State/AuthSlice";
import { sellerLogin } from "../../../State/seller/SellerAuthSlice";
import { showSnackbar } from "../../../State/SnackbarSlice";
import { useNavigate } from "react-router-dom";

const SellerLoginForm = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const formik = useFormik({
    initialValues: {
      email: "",
      otp: "",
    },
    onSubmit: (values) => {
      console.log(values);
      dispatch(sellerLogin(values));
      dispatch(showSnackbar({ message: "Seller Logged in successfully", severity: "success" }));
      navigate("/seller/orders");
    },
  });

  const handleSendOtp = () => {
    dispatch(sendLoginSignupOtp({ email: formik.values.email }));
    dispatch(showSnackbar({ message: "Otp sent to your email", severity: "success" }));
    
  };

  return (
    <div>
      <h1 className="text-center font-bold text-xl text-primary-color pb-5">
        Login As seller
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

        {true && (
          <div className="space-y-2">
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

        <Button
          onClick={handleSendOtp}
          fullWidth
          variant="contained"
          sx={{ py: "11px" }}
        >
          Send otp
        </Button>
        <Button
        type="submit"
          onClick={() => formik.handleSubmit()}
          fullWidth
          variant="contained"
          sx={{ py: "11px" }}
        >
          Login
        </Button>
      </div>
    </div>
  );
};

export default SellerLoginForm;
