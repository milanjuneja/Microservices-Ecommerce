import { AddPhotoAlternate, Close } from "@mui/icons-material";
import {
  Button,
  CircularProgress,
  FormControl,
  FormHelperText,
  Grid,
  Grid2,
  IconButton,
  InputLabel,
  MenuItem,
  Select,
  TextField,
} from "@mui/material";
import { useFormik } from "formik";
import React, { useState } from "react";
import { uploadToCloudinary } from "../../../Util/UploadToCloudnary";
import { menLevelTwo } from "../../../data/category/level two/menLevelTwo";
import { womenLevelTwo } from "../../../data/category/level two/womenLevelTwo";
import { furnitureLevelTwo } from "../../../data/category/level two/furnitureLevelTwo";
import { electronicsLevelTwo } from "../../../data/category/level two/electronicsLevelTwo";
import { menLevelThree } from "../../../data/category/level three/menLevelThree";
import { womenLevelThree } from "../../../data/category/level three/womenLevelThree";
import { furnitureLevelThree } from "../../../data/category/level three/furnitureLevelThree";
import { electronicsLevelThree } from "../../../data/category/level three/electronicsLevelThree";
import { colors } from "../../../data/filter/color";
import { MainCategory } from "../../../data/category/MainCategory";
import store, { useAppDispatch, useAppSelector } from "../../../State/Store";
import { createProduct } from "../../../State/seller/sellerProductSlice";
import { showSnackbar } from "../../../State/SnackbarSlice";

const category2: {[key:string]:any[]} = {
  men: menLevelTwo,
  women: womenLevelTwo,
  kids:[],
  home_furniture: furnitureLevelTwo,
  beauty: [],
  electronics: electronicsLevelTwo
};

const sizes = [
  {
    "name": "XS",
    "value": "XS"
  },
  {
    "name": "S",
    "value": "S"
  },
  {
    "name": "M",
    "value": "M"
  },
  {
    "name": "L",
    "value": "L"
  },
  {
    "name": "XL",
    "value": "Xl"
  },
]
const category3: {[key:string]:any[]} = {
  men: menLevelThree,
  women: womenLevelThree,
  kids:[],
  home_furniture: furnitureLevelThree,
  beauty: [],
  electronics: electronicsLevelThree
};

const AddProduct = () => {
  const [uploadImage, setUploadImage] = useState(false);
  const [snackbarOpen, setOpenSnackbar] = useState(false);
  const {product} = useAppSelector(store => store);

  const dispatch = useAppDispatch();

  const formik = useFormik({
    initialValues: {
      title: "",
      description: "",
      mrpPrice: "",
      sellingPrice: "",
      quantity: "",
      color: "",
      images: [],
      category: "",
      category2: "",
      category3: "",
      sizes: "",
    },
    onSubmit: (values) => {
      console.log(values);
      dispatch(createProduct({request:values, jwt: localStorage.getItem('jwt')}));
      if(product.error){
        showSnackbar({message: product.error, severity:"error"});
        return;
      }
      showSnackbar({message: "Product Added Successfully", severity:"success"});
    },
  });

  const handleImageChange = async (event: any) => {
    const file = event.target.files[0];
    setUploadImage(true);
    const image = await uploadToCloudinary(file);
    formik.setFieldValue("images", [...formik.values.images, image]);
    setUploadImage(false);
  };

  const handleRemoveImage = (index: number) => {
    const updatedImages = [...formik.values.images];
    updatedImages.splice(index, 1);
    formik.setFieldValue("images", updatedImages);
  };
  const childCategory = (category: any, parentCategoryId: any) => {
    return category.fiter((child: any) => {
      return child.parentCategoryId == parentCategoryId;
    });
  };
  const handleCLoseSnackbar = () => {
    setOpenSnackbar(false);
  };

  return (
    <div>
      <form onSubmit={formik.handleSubmit} className="space-y-4 p-4">
        <Grid2 container spacing={2}>
          <Grid2 className="flex flex-wrap gap-5" size={{ xs: 12 }}>
            <input
              type="file"
              accept="image/*"
              id="fileInput"
              style={{ display: "none" }}
              onChange={handleImageChange}
            />

            <label htmlFor="fileInput" className="relative">
              <span className="w-24 h-24 cursor-pointer flex items-center justify-center p-3 border rounded-md border-gray-400">
                <AddPhotoAlternate className="text-gray-700" />
              </span>
              {uploadImage && (
                <div className="absolute left-0 right-0 top-0 bottom-0 w-24 h-24 flex justify-center items-center">
                  <CircularProgress />
                </div>
              )}
            </label>

            <div className="flex flex-wrap gap-2">
              {formik.values.images.map((image, index) => (
                <div className="relative">
                  <img
                    className="w-24 h-24 object-cover"
                    key={index}
                    src={image}
                    alt={`ProductImage ${index + 1}`}
                  />
                  <IconButton
                    onClick={() => handleRemoveImage(index)}
                    className=""
                    size="small"
                    color="error"
                    sx={{
                      position: "absolute",
                      top: 0,
                      right: 0,
                      outline: "none",
                    }}
                  >
                    <Close sx={{ fontSize: "1rem" }} />
                  </IconButton>
                </div>
              ))}
            </div>
          </Grid2>
          <Grid2 size={{ xs: 12 }}>
            <TextField
              id="title"
              fullWidth
              name="title"
              label="Title"
              value={formik.values.title}
              onChange={formik.handleChange}
              error={formik.touched.title && Boolean(formik.errors.title)}
              helperText={formik.touched.title && formik.errors.title}
              required
            />
          </Grid2>
          <Grid2 size={{ xs: 12 }}>
            <TextField
              fullWidth
              multiline
              id="description"
              rows={4}
              name="description"
              label="Description"
              value={formik.values.description}
              onChange={formik.handleChange}
              error={
                formik.touched.description && Boolean(formik.errors.description)
              }
              helperText={
                formik.touched.description && formik.errors.description
              }
              required
            />
          </Grid2>
          <Grid2 size={{ xs: 12, md: 4, lg: 3 }}>
            <TextField
              fullWidth
              id="mrpPrice"
              name="mrpPrice"
              label="MRP"
              type="number"
              value={formik.values.mrpPrice}
              onChange={formik.handleChange}
              error={formik.touched.mrpPrice && Boolean(formik.errors.mrpPrice)}
              helperText={formik.touched.mrpPrice && formik.errors.mrpPrice}
              required
            />
          </Grid2>
          <Grid2 size={{ xs: 12, md: 4, lg: 3 }}>
            <TextField
              fullWidth
              id="sellingPrice"
              name="sellingPrice"
              label="Selling Price"
              type="number"
              value={formik.values.sellingPrice}
              onChange={formik.handleChange}
              error={
                formik.touched.sellingPrice &&
                Boolean(formik.errors.sellingPrice)
              }
              helperText={
                formik.touched.sellingPrice && formik.errors.sellingPrice
              }
              required
            />
          </Grid2>
          <Grid2 size={{ xs: 12, md: 4, lg: 3 }}>
            <FormControl
              fullWidth
              error={formik.touched.color && Boolean(formik.errors.color)}
              required
            >
              <InputLabel id="color-label">Color</InputLabel>
              <Select
                labelId="color-label"
                id="color"
                name="color"
                label="Color"
                value={formik.values.color}
                onChange={formik.handleChange}
              >
                <MenuItem value="">
                  <em>None</em>
                </MenuItem>
                {colors.map((color: any) => (
                  <MenuItem value={color.name}>
                    <div className="flex gap-3">
                      <span
                        className={`h-5 w-5 rounded-full ${
                          color.name === "White" ? "border" : ""
                        }`}
                        style={{ backgroundColor: color.hex }}
                      ></span>
                      <p>{color.name}</p>
                    </div>
                  </MenuItem>
                ))}
              </Select>
              {formik.touched.color && formik.errors.color && (
                <FormHelperText>{formik.errors.color}</FormHelperText>
              )}
            </FormControl>
          </Grid2>

          <Grid2 size={{ xs: 12, md: 4, lg: 3 }}>
            <FormControl
              fullWidth
              error={formik.touched.sizes && Boolean(formik.errors.sizes)}
              required
            >
              <InputLabel id="sizes-label">Size</InputLabel>
              <Select
                labelId="sizes-label"
                id="sizes"
                name="sizes"
                label="Sizes"
                value={formik.values.sizes}
                onChange={formik.handleChange}
              >
                <MenuItem value="">
                  <em>None</em>
                </MenuItem>
                {sizes.map((sizes: any) => (
                  <MenuItem value={sizes.name}>
                    <div className="flex gap-3">
                      <p>{sizes.name}</p>
                    </div>
                  </MenuItem>
                ))}
              </Select>
              {formik.touched.sizes && formik.errors.sizes && (
                <FormHelperText>{formik.errors.sizes}</FormHelperText>
              )}
            </FormControl>
          </Grid2>

          <Grid2 size={{ xs: 12, md: 4, lg: 4 }}>
            <FormControl
              fullWidth
              error={formik.touched.category && Boolean(formik.errors.category)}
              required
            >
              <InputLabel id="category-label">Category</InputLabel>
              <Select
                labelId="category-label"
                id="category"
                name="category"
                label="Category"
                value={formik.values.category}
                onChange={formik.handleChange}
              >
                <MenuItem value="">
                  <em>None</em>
                </MenuItem>
                {MainCategory.map((item: any) => (
                  <MenuItem value={item.categoryId}>
                    {item.name}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </Grid2>

          <Grid2 size={{ xs: 12, md: 4, lg: 4 }}>
            <FormControl
              fullWidth
              error={formik.touched.category2 && Boolean(formik.errors.category2)}
              required
            >
              <InputLabel id="category2-label">Category 2</InputLabel>
              <Select
                labelId="category2-label"
                id="category2"
                name="category2"
                label="Category 2"
                value={formik.values.category2}
                onChange={formik.handleChange}
              >
                <MenuItem value="">
                  <em>None</em>
                </MenuItem>
                {category2["men"].map((item: any) => (
                  <MenuItem value={item.categoryId}>
                    {item.name}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </Grid2>

          <Grid2 size={{ xs: 12, md: 4, lg: 4 }}>
            <FormControl
              fullWidth
              error={formik.touched.category3 && Boolean(formik.errors.category3)}
              required
            >
              <InputLabel id="category3-label">Category 3</InputLabel>
              <Select
                labelId="category3-label"
                id="category3"
                name="category3"
                label="Category 2"
                value={formik.values.category3}
                onChange={formik.handleChange}
              >
                <MenuItem value="">
                  <em>None</em>
                </MenuItem>
                {category3["men"].map((item: any) => (
                  <MenuItem value={item.categoryId}>
                    {item.name}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </Grid2>

          <Grid2 size={{xs:12}}>
            <Button 
            sx={{p:"14px"}}
            color="primary"
            variant="contained"
            fullWidth
            type="submit">
              {false ? <CircularProgress size={"small"}/> : "Add Product"}
            </Button>
              

          </Grid2>
        </Grid2>
      </form>
    </div>
  );
};

export default AddProduct;
