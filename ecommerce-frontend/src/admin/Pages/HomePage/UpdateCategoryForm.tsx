import {
  Box,
  Button,
  CircularProgress,
  FormControl,
  Grid2,
  InputLabel,
  MenuItem,
  Select,
  TextField,
} from "@mui/material";
import { useFormik } from "formik";
import { menLevelTwo } from "../../../data/category/level two/menLevelTwo";
import { womenLevelTwo } from "../../../data/category/level two/womenLevelTwo";
import { furnitureLevelTwo } from "../../../data/category/level two/furnitureLevelTwo";
import { electronicsLevelTwo } from "../../../data/category/level two/electronicsLevelTwo";
import { menLevelThree } from "../../../data/category/level three/menLevelThree";
import { womenLevelThree } from "../../../data/category/level three/womenLevelThree";
import { furnitureLevelThree } from "../../../data/category/level three/furnitureLevelThree";
import { electronicsLevelThree } from "../../../data/category/level three/electronicsLevelThree";
import { MainCategory } from "../../../data/category/MainCategory";
import { useEffect, useState } from "react";
import { useAppDispatch } from "../../../State/Store";
import { updateHomeCategory } from "../../../State/admin/homeCategorySlice";

const UpdateCategoryForm = ({category}:{category:number}) => {
  const dispatch = useAppDispatch();
  const formik = useFormik({
    initialValues: {
      imageUrl: "",
      category: "",
      category2: "",
      category3: "",
    },
    onSubmit: (values) => {
      const updatedValues = {
        name:values.category,
        image:values.imageUrl,
        categoryId:values.category2,
        section:'GRID'
      }
      dispatch(updateHomeCategory({id:category,
        data:updatedValues
      }))
    },
  });

  const category2: { [key: string]: any[] } = {
    men: menLevelTwo,
    women: womenLevelTwo,
    kids: [],
    home_furniture: furnitureLevelTwo,
    beauty: [],
    electronics: electronicsLevelTwo,
  };

  const category3: { [key: string]: any[] } = {
    men: menLevelThree,
    women: womenLevelThree,
    kids: [],
    home_furniture: furnitureLevelThree,
    beauty: [],
    electronics: electronicsLevelThree,
  };
  const [selectedCategory1, setCategory1] = useState("");
  const [selectedCategory2, setCategory2] = useState("");
  const [selectedCategory3, setCategory3] = useState("");


  useEffect(() => {
    formik.setFieldValue("category2", "");
    formik.setFieldValue("category3", "");
    setCategory2("");
  }, [formik.values.category]);
  useEffect(() => {
    formik.setFieldValue("category3", "");
  }, [formik.values.category2]);
  useEffect(() => {
    formik.setFieldValue("category3", selectedCategory3);
  }, [selectedCategory3]);
  return (
    <div>
      <Box sx={{ max: "auto" }}>
        <p className="text-xl font-bold text-center pb-5">Update Category</p>
        <form onSubmit={formik.handleSubmit}>
          <Grid2 container spacing={4}>
            <Grid2 size={{ xs: 12 }}>
              <TextField
                fullWidth
                name="imageUrl"
                label="Image URL"
                value={formik.values.imageUrl}
                onChange={formik.handleChange}
                error={
                  formik.touched.imageUrl && Boolean(formik.errors.imageUrl)
                }
                helperText={formik.touched.imageUrl && formik.errors.imageUrl}
              />
            </Grid2>
            <Grid2 size={{ xs: 12 }}>
              <FormControl
                fullWidth
                error={
                  formik.touched.category && Boolean(formik.errors.category)
                }
                required
              >
                <InputLabel id="category-label">Category</InputLabel>
                <Select
                  labelId="category-label"
                  id="category"
                  name="category"
                  label="Category"
                  value={formik.values.category}
                  onChange={(e) => {
                    formik.handleChange(e);
                    setCategory1(e.target.value);
                  }}
                >
                  <MenuItem value="">
                    <em>None</em>
                  </MenuItem>
                  {MainCategory.map((item: any) => (
                    <MenuItem key={item.categoryId} value={item.categoryId}>{item.name}</MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid2>

            <Grid2 size={{ xs: 12 }}>
              <FormControl
                fullWidth
                disabled={!formik.values.category}
                error={
                  formik.touched.category2 && Boolean(formik.errors.category2)
                }
                required
              >
                <InputLabel id="category2-label">Category 2</InputLabel>
                <Select
                  labelId="category2-label"
                  id="category2"
                  name="category2"
                  label="Category 2"
                  value={formik.values.category2}
                  onChange={(e) => {
                    formik.handleChange(e);
                    setCategory2(e.target.value);
                  }}
                >
                  <MenuItem value="">
                    <em>None</em>
                  </MenuItem>
                  {category2[selectedCategory1]?.map((item: any) => (
                    <MenuItem key={item.categoryId} value={item.categoryId}>{item.name}</MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid2>

            <Grid2 size={{ xs: 12 }}>
              <FormControl
                fullWidth
                disabled={!formik.values.category2}
                error={
                  formik.touched.category3 && Boolean(formik.errors.category3)
                }
                required
              >
                <InputLabel id="category3-label">Category 3</InputLabel>
                <Select
                  labelId="category3-label"
                  id="category3"
                  name="category3"
                  label="Category 3"
                  value={formik.values.category3}
                  onChange={(e) => {
                    formik.handleChange(e);
                    setCategory3(e.target.value);
                  }}
                >
                  <MenuItem value="">
                    <em>None</em>
                  </MenuItem>
                  {category3[selectedCategory1]?.map((item: any) => (
                    <MenuItem key={item.categoryId} value={item.categoryId}>{item.name}</MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid2>

            <Grid2 size={{ xs: 12 }}>
              <Button
                sx={{ p: "14px" }}
                color="primary"
                variant="contained"
                fullWidth
                type="submit"
              >
                {false ? <CircularProgress size={"small"} /> : "Submit"}
              </Button>
            </Grid2>
          </Grid2>
        </form>
      </Box>
    </div>
  );
};

export default UpdateCategoryForm;
