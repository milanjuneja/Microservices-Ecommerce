import { ThemeProvider } from "@mui/material";
import Navbar from "./customer/components/Navbar/Navbar";
import CustomTheme from "./Theme/CustomTheme";
import Home from "./customer/pages/Home/Home";
import Product from "./customer/pages/Product/Product";
import ProductDetail from "./customer/pages/ProductDetails/ProductDetail";
import Review from "./customer/pages/Review/Review";
import Cart from "./customer/pages/Cart/Cart";
import Checkout from "./customer/pages/Checkout/Checkout";
import Account from "./customer/pages/Account/Account";
import { Route, Routes, useNavigate } from "react-router-dom";
import BecomeSeller from "./customer/pages/Become Seller/BecomeSeller";
import SellerDashboard from "./seller/pages/SellerDashboard/SellerDashboard";
import AdminDashboard from "./admin/Pages/Dashboard/AdminDashboard";
import store, { useAppDispatch, useAppSelector } from "./State/Store";
import { useEffect } from "react";
import { getSellerByJwt } from "./State/seller/sellerSlice";
import Auth from "./customer/pages/Auth/Auth";
import { fetchUserProfile } from "./State/AuthSlice";
import PaymentSucces from "./customer/pages/PaymentSucces";
import Wishlist from "./customer/pages/Wishlist/Wishlist";
import { createHomeCategories } from "./State/customer/customerSlice";
import { homeCategories } from "./data/HomeCategories";
import SearchProducts from "./customer/pages/Search/SearchProducts";
import ReviewForm from "./customer/pages/Review/ReviewForm";
import GlobalSnackbar from "./component/GlobalSnacker";
import NotFound from "./component/NotFound";

function App() {
  const dispatch = useAppDispatch();
  const { seller, auth } = useAppSelector((store) => store);
  const navigate = useNavigate();
  useEffect(() => {
    dispatch(getSellerByJwt(localStorage.getItem("jwt") || ""));
    dispatch(createHomeCategories(homeCategories));
  }, []);

  useEffect(() => {
    if (seller.profile) navigate("/seller");
  }, [seller.profile]);

  useEffect(() => {
    dispatch(
      fetchUserProfile({ jwt: auth.jwt || localStorage.getItem("jwt") })
    );
  }, [auth.jwt]);

  return (
    <>
      <ThemeProvider theme={CustomTheme}>
        <div>
          <Navbar />
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Auth />} />
            <Route path="/products/:category" element={<Product />} />
            <Route path="/reviews/:productId" element={<Review />} />
            <Route path="/reviews/:productId/create" element={<ReviewForm />} />
            <Route
              path="/product-details/:category/:name/:productId"
              element={<ProductDetail />}
            />
            <Route path="/cart" element={<Cart />} />
            <Route path="/checkout" element={<Checkout />} />
            <Route path="/become-seller" element={<BecomeSeller />} />
            <Route path="/account/*" element={<Account />} />
            <Route path="/seller/*" element={<SellerDashboard />} />
            <Route path="/admin/*" element={<AdminDashboard />} />
            <Route
              path="/payment-success/:orderId"
              element={<PaymentSucces />}
            />
            <Route path="/wishlist" element={<Wishlist />} />

            <Route path="/search-products" element={<SearchProducts />} />
            <Route path="*" element={<NotFound />}/>
          </Routes>
        </div>
        <GlobalSnackbar />
      </ThemeProvider>
    </>
  );
}

export default App;
