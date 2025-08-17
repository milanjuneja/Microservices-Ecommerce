import { DatePicker, LocalizationProvider } from "@mui/x-date-pickers";
import { Dayjs } from "dayjs";
import { useFormik } from "formik";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { Box, Button, Grid2, TextField } from "@mui/material";
import { useAppDispatch } from "../../../State/Store";
import { createCoupon } from "../../../State/admin/adminCouponSlice";
import { showSnackbar } from "../../../State/SnackbarSlice";

interface CouponFormValues {
  code: string;
  discountPercentage: number;
  validityStartDate: Dayjs | null;
  validityEndDate: Dayjs | null;
  minimumOrderValue: number;
}

const AddNewCoupon = () => {
  const dispatch = useAppDispatch();
  const formik = useFormik<CouponFormValues>({
    initialValues: {
      code: "",
      discountPercentage: 0,
      validityStartDate: null,
      validityEndDate: null,
      minimumOrderValue: 0,
    },
    onSubmit: (values) => {
      console.log(values);
      const formattedValues = {
        ...values,
        validityStartDate: values.validityStartDate
          ? values.validityStartDate.toISOString()
          : null,
        validityEndDate: values.validityEndDate
          ? values.validityEndDate.toISOString()
          : null,
      };
      console.log(formattedValues);
      dispatch(
        createCoupon({ jwt: localStorage.getItem("jwt") || "", coupon: values })
      );
      showSnackbar({message:"Coupon Created Successfully",severity:"success"})
    },
  });
  return (
    <div>
      <h1 className="text-2xl font-bold text-primary-color pb-5 text-center">
        Create New Coupon
      </h1>
      <LocalizationProvider dateAdapter={AdapterDayjs}>
        <Box component={"form"} onSubmit={formik.handleSubmit} sx={{ mt: 3 }}>
          <Grid2 container spacing={2}>
            <Grid2 size={{ xs: 12, sm: 6 }}>
              <TextField
                fullWidth
                name="code"
                label="Coupon Code"
                value={formik.values.code}
                onChange={formik.handleChange}
                error={formik.touched.code && Boolean(formik.errors.code)}
                helperText={formik.touched.code && formik.errors.code}
              />
            </Grid2>

            <Grid2 size={{ xs: 12, sm: 6 }}>
              <TextField
                fullWidth
                name="discountPercentage"
                label="Discount Percentage"
                value={formik.values.discountPercentage}
                onChange={formik.handleChange}
                error={
                  formik.touched.discountPercentage &&
                  Boolean(formik.errors.discountPercentage)
                }
                helperText={
                  formik.touched.discountPercentage &&
                  formik.errors.discountPercentage
                }
              />
            </Grid2>

            <Grid2 size={{ xs: 12, sm: 6 }}>
              <DatePicker
                name="validityStartDate"
                sx={{ width: "100%" }}
                label="Validity Start Date"
                value={formik.values.validityStartDate}
                onChange={(date) =>
                  formik.setFieldValue("validityStartDate", date)
                }
              />
            </Grid2>

            <Grid2 size={{ xs: 12, sm: 6 }}>
              <DatePicker
                onChange={(date) =>
                  formik.setFieldValue("validityEndDate", date)
                }
                name="validityEndDate"
                sx={{ width: "100%" }}
                label="Validity End Date"
                value={formik.values.validityEndDate}
              ></DatePicker>
            </Grid2>

            <Grid2 size={{ xs: 12 }}>
              <TextField
                fullWidth
                name="minimumOrderValue"
                // type="number"
                label="Minimum Order Value"
                value={formik.values.minimumOrderValue}
                onChange={formik.handleChange}
                error={
                  formik.touched.minimumOrderValue &&
                  Boolean(formik.errors.minimumOrderValue)
                }
                helperText={
                  formik.touched.minimumOrderValue &&
                  formik.errors.minimumOrderValue
                }
              />
            </Grid2>
            <Grid2 size={{ xs: 12 }}>
              <Button
                onClick={() => formik.handleSubmit()}
                variant="contained"
                fullWidth
                sx={{ py: ".8rem" }}
              >
                Create Coupon
              </Button>
            </Grid2>
          </Grid2>
        </Box>
      </LocalizationProvider>
    </div>
  );
};

export default AddNewCoupon;
