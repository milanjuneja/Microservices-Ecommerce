import { Box, Button, Grid, Grid2, TextField } from "@mui/material";
import { useFormik } from "formik";
import * as Yup from "yup";
import { useAppDispatch } from "../../../State/Store";
import { createOrder } from "../../../State/customer/orderSlice";

const AddressFormSchema = Yup.object().shape({
  name: Yup.string()
    .required("Name is required")
    .min(2, "Name must be at least 2 characters")
    .max(50, "Name must be less than 50 characters"),
  mobile: Yup.string()
    .required("Mobile number is required")
    .matches(/^[0-9]{10}$/, "Mobile number must be exactly 10 digits"),
  pinCode: Yup.string()
    .required("Pin code is required")
    .matches(/^[1-9][0-9]{5}$/, "Pin code must be a valid 6-digit number"),
  address: Yup.string()
    .required("Address is required")
    .min(10, "Address must be at least 10 characters"),
  city: Yup.string()
    .required("City is required")
    .min(2, "City name must be at least 2 characters"),
  state: Yup.string()
    .required("State is required")
    .min(2, "State name must be at least 2 characters"),
  locality: Yup.string()
    .required("Locality is required")
    .min(2, "Locality must be at least 2 characters"),
});

const AddressForm = ({paymentGateway}:any) => {
  const dispatch = useAppDispatch();
  const formik = useFormik({
    initialValues: {
      name: "",
      mobile: "",
      pinCode: "",
      address: "",
      city: "",
      state: "",
      locality: "",
    },
    validationSchema: AddressFormSchema,
    onSubmit: (values) => {
      console.log(values);
      dispatch(createOrder({address:values, 
        jwt:localStorage.getItem('jwt') || "",
         paymentGateway}))
    },
  });
  return (
    <div>
      <Box sx={{ max: "auto" }}>
        <p className="text-xl font-bold text-center pb-5">Contact Details</p>

        <form onSubmit={formik.handleSubmit}>
          <Grid2 container spacing={4}>
            <Grid2 size={{ xs: 12 }}>
              <TextField
                fullWidth
                name="name"
                label="name"
                value={formik.values.name}
                onChange={formik.handleChange}
                error={formik.touched.name && Boolean(formik.errors.name)}
                helperText={formik.touched.name && formik.errors.name}
              />
            </Grid2>

            <Grid2 size={{ xs: 6 }}>
              <TextField
                fullWidth
                name="mobile"
                label="Mobile"
                value={formik.values.mobile}
                onChange={formik.handleChange}
                error={formik.touched.mobile && Boolean(formik.errors.mobile)}
                helperText={formik.touched.mobile && formik.errors.mobile}
              />
            </Grid2>

            <Grid2 size={{ xs: 6 }}>
              <TextField
                fullWidth
                name="pinCode"
                label="Pin Code"
                value={formik.values.pinCode}
                onChange={formik.handleChange}
                error={formik.touched.pinCode && Boolean(formik.errors.pinCode)}
                helperText={formik.touched.pinCode && formik.errors.pinCode}
              />
            </Grid2>

            <Grid2 size={{ xs: 12 }}>
              <TextField
                fullWidth
                name="address"
                label="Address"
                value={formik.values.address}
                onChange={formik.handleChange}
                error={formik.touched.address && Boolean(formik.errors.address)}
                helperText={formik.touched.address && formik.errors.address}
              />
            </Grid2>

            <Grid2 size={{ xs: 12 }}>
              <TextField
                fullWidth
                name="locality"
                label="Locality"
                value={formik.values.locality}
                onChange={formik.handleChange}
                error={formik.touched.locality && Boolean(formik.errors.locality)}
                helperText={formik.touched.locality && formik.errors.locality}
              />
            </Grid2>

            <Grid2 size={{ xs: 6 }}>
              <TextField
                fullWidth
                name="city"
                label="City"
                value={formik.values.city}
                onChange={formik.handleChange}
                error={formik.touched.city && Boolean(formik.errors.city)}
                helperText={formik.touched.city && formik.errors.city}
              />
            </Grid2>

            <Grid2 size={{ xs: 6 }}>
              <TextField
                fullWidth
                name="state"
                label="State"
                value={formik.values.state}
                onChange={formik.handleChange}
                error={formik.touched.state && Boolean(formik.errors.state)}
                helperText={formik.touched.state && formik.errors.state}
              />
            </Grid2>

            <Grid2 size={{xs:12}}>
            <Button fullWidth type="submit" variant="contained" sx={{py:"14px"}}>Add Address</Button>
          </Grid2>
          </Grid2>
          
        </form>
      </Box>
    </div>
  );
};

export default AddressForm;
