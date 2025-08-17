import { Box, TextField, Typography } from "@mui/material";
import { useFormik } from "formik";
import { useNavigate, useSearchParams } from "react-router-dom";
import store, { useAppDispatch, useAppSelector } from "../../../State/Store";
import { searchProduct } from "../../../State/customer/ProductSlice";
import ProductCard from "../Product/ProductCard";
import { useEffect, useState } from "react";

const SearchProducts = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const dispatch = useAppDispatch();
  const { product } = useAppSelector((store) => store);
  const [isSubmitted, setIsSubmitted] = useState(false);
  const navigate = useNavigate();
  const formik = useFormik({
    initialValues: {
      search: searchParams.get("search") || "",
    },
    onSubmit: (values) => {
      dispatch(searchProduct(values.search));
      setIsSubmitted(true);
    },
  });

  const useDebounce = (text: any, delay: any) => {
    const [debounce, setDebounce] = useState(text);

    useEffect(() => {
      const timer = setTimeout(() => {
        setDebounce(text);
      }, delay);

      return () => {
        clearTimeout(timer);
      };
    }, [delay, text]);
    return debounce;
  };

  const debounceText = useDebounce(searchParams.get("search") || "", 1000);

  useEffect(() => {
    if (debounceText) dispatch(searchProduct(debounceText));
  }, [debounceText]);

  const updateFilterParams = (event: any) => {
    const { value, name } = event.target;
    formik.handleChange(event);
    const newParams = new URLSearchParams(searchParams);
    if (value) newParams.set(name, value);
    else newParams.delete(name);
    setSearchParams(newParams);
    setIsSubmitted(false);
  };
  return (
    <>
      <div className="mb-8 px-4">
        <Box
          className="bg-white shadow-md rounded-xl p-6 space-y-6 lg:w-1/2 w-full mx-auto"
          component={"form"}
          onSubmit={formik.handleSubmit}
        >
          <Typography
            sx={{ paddingTop: "10px" }}
            variant="h4"
            className="text-center font-semibold text-gray-800"
          >
            Search Products
          </Typography>
          <TextField
            fullWidth
            name="search"
            label="Search Products"
            value={formik.values.search}
            onChange={(e) => updateFilterParams(e)}
            error={formik.touched.search && Boolean(formik.errors.search)}
            helperText={formik.touched.search && formik.errors.search}
            required
          />
          {!isSubmitted && debounceText && (
            <div className="w-full border border-gray-300 bg-white rounded-md shadow-md max-h-56 overflow-auto">
              <ul className="p-3 space-y-2">
                {product.searchProduct.map((item) => (
                  <li
                    onClick={() => navigate(`/product-details/${item.category.categoryId}/${item.title}/${item.id}`)}
                    className="px-4 py-2 text-gray-700 hover:bg-blue-100 cursor-pointer rounded-md"
                  >
                    {item.title}
                  </li>
                ))}
              </ul>
            </div>
          )}
        </Box>
      </div>

      {isSubmitted && product.searchProduct.length > 0 ? (
        <div className="pt-5 px-4">
          <section className="grid sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
            {product.searchProduct.map((item) => (
              <ProductCard item={item} />
            ))}
          </section>
        </div>
      ) : (
        <div className="text-center py-10">
          <h1 className="text-gray-600 text-xl font-semibold">
            {isSubmitted && "No Product found"}
          </h1>
        </div>
      )}
    </>
  );
};

export default SearchProducts;
