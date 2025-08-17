import { useFormik } from "formik";
import { useEffect, useState } from "react";
import { useAppDispatch, useAppSelector } from "../../../State/Store";
import { useParams } from "react-router-dom";
import { fetchProductById } from "../../../State/customer/ProductSlice";
import {
  Button,
  CircularProgress,
  Divider,
  IconButton,
  Rating,
  Stack,
  TextField,
} from "@mui/material";
import { AddPhotoAlternate, Close } from "@mui/icons-material";
import { createReview } from "../../../State/customer/reviewSlice";

const ReviewForm = () => {
  const dispatch = useAppDispatch();
  const { productId } = useParams();
  const { product } = useAppSelector((store) => store);
  const [uploadImage, setUploadImage] = useState(false);

  const formik = useFormik({
    initialValues: {
      reviewTitle: "",
      description: "",
      images: [],
      rating: 0,
    },
    onSubmit: (values) => {
      console.log(values);
      dispatch(
        createReview({
          jwt: localStorage.getItem("jwt"),
          productId: Number(productId),
          request: {
            reviewText: values.description,
            rating: Number(values.rating),
            productImages: values.images,
          },
        })
      );
    },
  });

  const handleRemoveImage = (index: number) => {
    const updatedImages = [...formik.values.images];
    updatedImages.splice(index, 1);
    formik.setFieldValue("images", updatedImages);
  };

  const handleImageChange = async (event: any) => {
    const file = event.target.files[0];
    setUploadImage(true);

    setTimeout(() => {
      formik.setFieldValue("images", [
        ...formik.values.images,
        URL.createObjectURL(file),
      ]);
      setUploadImage(false);
    }, 1000);
  };

  useEffect(() => {
    dispatch(fetchProductById(Number(productId)));
  }, [productId, dispatch]);

  const changeRatingStar = (e: any) => {
    formik.setFieldValue("rating", e.target.value);
  };

  return (
    <div className="px-5 lg:px-60 min-h-screen mt-10">
      <div className="bg-white shadow-lg rounded-lg p-6">
        <h2 className="text-3xl font-semibold text-gray-800 mb-5">
          Create Review
        </h2>

        <div className="flex items-center gap-5 mb-5">
          <img
            className="w-16 h-20 rounded-md object-cover"
            src={product.product?.images[0]}
            alt=""
          />
          <div>
            <h3 className="text-lg font-semibold">{product.product?.title}</h3>
            <p className="text-gray-600">{product.product?.description}</p>
          </div>
        </div>
        <Divider className="my-5" />

        <div className="mb-5">
          <h3 className="text-xl font-semibold text-gray-800">
            Overall Rating
          </h3>
          <Stack spacing={1} className="mt-2">
            <Rating
              onChange={(e) => changeRatingStar(e)}
              name="size-large"
              defaultValue={0}
              size="large"
            />
          </Stack>
        </div>
        <Divider className="my-5" />

        <form onSubmit={formik.handleSubmit} className="space-y-5">
          <div>
            <h3 className="text-xl font-semibold text-gray-800">
              Add a Headline
            </h3>
            <TextField
              fullWidth
              name="reviewTitle"
              label="Title"
              className="mt-2"
              value={formik.values.reviewTitle}
              onChange={formik.handleChange}
              error={
                formik.touched.reviewTitle && Boolean(formik.errors.reviewTitle)
              }
              helperText={
                formik.touched.reviewTitle && formik.errors.reviewTitle
              }
            />
          </div>
          <Divider className="my-5" />

          <div>
            <h3 className="text-xl font-semibold text-gray-800">
              Upload Images
            </h3>
            <div className="flex flex-wrap items-center gap-4 mt-3">
              <input
                type="file"
                accept="image/*"
                id="fileInput"
                hidden
                onChange={handleImageChange}
              />
              <label htmlFor="fileInput" className="relative">
                <span className="w-24 h-24 flex items-center justify-center border border-gray-400 rounded-lg cursor-pointer">
                  <AddPhotoAlternate className="text-gray-700" />
                </span>
                {uploadImage && (
                  <div className="absolute inset-0 flex items-center justify-center bg-white bg-opacity-75 rounded-lg">
                    <CircularProgress />
                  </div>
                )}
              </label>

              {formik.values.images.length > 0 && (
                <div className="flex flex-wrap gap-2">
                  {formik.values.images.map((image, index) => (
                    <div className="relative" key={index}>
                      <img
                        className="w-24 h-24 object-cover rounded-lg"
                        src={image}
                        alt={`Product ${index + 1}`}
                      />
                      <IconButton
                        onClick={() => handleRemoveImage(index)}
                        size="small"
                        color="error"
                        className="absolute top-0 right-0"
                      >
                        <Close sx={{ fontSize: "1rem" }} />
                      </IconButton>
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>
          <Divider className="my-5" />

          <div>
            <h3 className="text-xl font-semibold text-gray-800">
              Add a Written Review
            </h3>
            <TextField
              fullWidth
              name="description"
              label="Description"
              className="mt-2"
              multiline
              rows={4}
              value={formik.values.description}
              onChange={formik.handleChange}
              error={
                formik.touched.description && Boolean(formik.errors.description)
              }
              helperText={
                formik.touched.description && formik.errors.description
              }
            />
          </div>
          <Divider className="my-5" />

          <Button
            onClick={() => formik.handleSubmit}
            type="submit"
            fullWidth
            variant="contained"
            className="py-3 bg-blue-600 hover:bg-blue-700 text-white font-semibold"
          >
            Submit
          </Button>
        </form>
      </div>
    </div>
  );
};

export default ReviewForm;
