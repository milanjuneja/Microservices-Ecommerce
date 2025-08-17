import {
  Avatar,
  Box,
  Button,
  IconButton,
  useMediaQuery,
  useTheme,
} from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import SearchIcon from "@mui/icons-material/Search";
import FavoriteBorderIcon from "@mui/icons-material/FavoriteBorder";
import AddShoppingCartIcon from "@mui/icons-material/AddShoppingCart";
import StorefrontIcon from "@mui/icons-material/Storefront";
import CategorySheet from "./CategorySheet";
import { MainCategory } from "../../../data/category/MainCategory";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import store, { useAppDispatch, useAppSelector } from "../../../State/Store";
function Navbar() {
  const theme = useTheme();
  const navigate = useNavigate();
  const isLarge = useMediaQuery(theme.breakpoints.up("lg"));
  const [selectredCategory, setSelectedCategory] = useState("men");
  const [showCategorySheet, setShowCategorySheet] = useState(false);
  const {auth} = useAppSelector(store=>store);
  const {cart} = useAppSelector(store=>store);

  return (
    <>
      <Box className="sticky top-0 left-0 right-0 bg-white" sx={{ zIndex: 2 }}>
        <div className="flex items-center justify-between px-5 lg:px-20 h-[70px] border-b">
          <div className="flex items-center gap-9">
            <div className="flex items-center gap-2">
              {!isLarge && (
                <IconButton>
                  <MenuIcon />
                </IconButton>
              )}
              <h1
                onClick={() => navigate("/")}
                className="logo cursor-pointer text-lg font-bold md:text-2xl text-[#00927c]"
              >
                Ecommerce Website
              </h1>
            </div>
            <ul className="flex items-center font-medium text-gray-800">
              {MainCategory.map((item) => (
                <li
                  onMouseLeave={() => {
                    setShowCategorySheet(false);
                  }}
                  onMouseEnter={() => {
                    setShowCategorySheet(true);
                    setSelectedCategory(item.categoryId);
                  }}
                  key={item.name}
                  className="mainCategory hover:text-primary-color cursor-pointer hover:border-b-2 h-[70px] p-5 border-primary-color"
                >
                  {item.name}
                </li>
              ))}
            </ul>
          </div>
          <div className="flex gap-1 lg:gap-6 items-center">
            <IconButton onClick={()=> navigate("/search-products")}>
              <SearchIcon />
            </IconButton>
            {auth.user ? (
              <>
                <Button
                  onClick={() => navigate("/account/orders")}
                  className="flex items-center gap-2"
                >
                  <Avatar
                    sx={{ width: 29, height: 29 }}
                    src="https://png.pngtree.com/png-vector/20230831/ourmid/pngtree-man-avatar-image-for-profile-png-image_9197908.png"
                  />
                  <h1 className="font-semibold hidden lg:block">{auth.user?.firstName + ' ' + auth.user?.lastName}</h1>
                </Button>
              </>
            ) : (
              <Button onClick={()=> navigate('/login')} variant="contained">Login</Button>
            )}
            <IconButton onClick={()=>navigate("/wishlist")}>
              <FavoriteBorderIcon sx={{ fontSize: 29 }} />
            </IconButton>
            <IconButton onClick={() => navigate("/cart")}>
              <AddShoppingCartIcon 
                className="text-gray-700"
                sx={{ fontSize: 29 }}
              />{cart.cart?.cartItems.length}
            </IconButton>

            {isLarge && (
              <Button
                startIcon={<StorefrontIcon />}
                variant="outlined"
                className="hidden lg:block"
                onClick={() => navigate("/become-seller")}
              >
                Become seller
              </Button>
            )}
          </div>
        </div>

        {showCategorySheet && (
          <div
            onMouseLeave={() => setShowCategorySheet(false)}
            onMouseEnter={() => setShowCategorySheet(true)}
            className="categorySheet abolute top-[4.41rem] left-20 right-20 border"
          >
            <CategorySheet selectredCategory={selectredCategory} />
          </div>
        )}
      </Box>
    </>
  );
}

export default Navbar;
