import React, { useEffect, useState } from "react";
import StartIcon from "@mui/icons-material/Star";
import { purple, teal } from "@mui/material/colors";
import { Button, Divider } from "@mui/material";
import {
  Add,
  AddShoppingCart,
  FavoriteBorder,
  LocalShipping,
  Remove,
  Shield,
  Wallet,
  WorkspacePremium,
} from "@mui/icons-material";
import SimilarProduct from "./SimilarProduct";
import ReviewCard from "../Review/ReviewCard";
import { useAppDispatch, useAppSelector } from "../../../State/Store";
import { useNavigate, useParams } from "react-router-dom";
import { fetchProductById } from "../../../State/customer/ProductSlice";
import { addProductToWishlist } from "../../../State/customer/wishlistSlice";
import { additemToCart } from "../../../State/customer/cartSlice";
import { getReviewsByProductId } from "../../../State/customer/reviewSlice";
import { showSnackbar } from "../../../State/SnackbarSlice";

const ProductDetail = () => {
  const [quantity, setQuantity] = useState(1);
  const dispatch = useAppDispatch();
  const {productId} = useParams();
  const {product} = useAppSelector(store=>store);
  const [activeImage, setActiveImage] = useState(0);
  const {review} = useAppSelector(store => store);
  const navigate = useNavigate();

  useEffect(()=>{
    dispatch(fetchProductById(Number(productId)));
    dispatch(getReviewsByProductId({ productId: Number(productId)}));
  },[productId])

  const handleActiveImage = (value: number) => {
    setActiveImage(value)
  }
  const handleAddToCart = () =>{
    const values =  {
      productId: Number(productId),
      size: 'XL',
      quantity: Number(quantity)
    }
    
    
    dispatch(additemToCart({
      jwt: localStorage.getItem('jwt') || "",
      request: values
    }))
    dispatch(showSnackbar({
      message: "Product added to cart successfully",
      severty: "success"
    }))
    
  }
  const handleWishlist = (e: any) => {
      e.stopPropagation();
      product.product?.id &&
        dispatch(
          addProductToWishlist({
            jwt: localStorage.getItem("jwt") || "",
            productId: product.product?.id,
          })
        );
        dispatch(showSnackbar({
          message: "Product added to wishlist successfully",
          severty: "success"
        }))
    };

    const handleReviewClick = (e: any) => {
      e.stopPropagation();
      navigate(`/reviews/${productId}`)
    }

  return (
    <div className="px-5 lg:px-20 pt-10">
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-10">
        <section className="flex flex-col lg:flex-row gap-5">
          <div className="w-full lg:w-[20%] flex flex-wrap lg:flex-col gap-3">
            {product.product?.images.map((item, index) => (
              <img
                onClick={() => handleActiveImage(index)}
                className="lg:w-full w-[50px] cursor-pointer rounded-md"
                src={item}
                alt=""
              />
            ))}
          </div>
          <div className="w-full lg:w=[85%]">
            <img
              className="w-full rounded-md"
              src={product.product?.images[activeImage]}
              alt=""
            />
          </div>
        </section>

        <section>
          <h1 className="font-bold text-lg text-primary-color">{product.product?.seller.businessDetails.businessName}</h1>
          <p className="text-gray-500 font-semibold">{product.product?.title}</p>
          <div className="flex justify-between items-center py-2 border w-[180px] px-3 mt-5">
            <div className="flex gap-1 items-center">
              <span>4</span>
              <StartIcon sx={{ color: teal[500], fontSize: "17px" }} />
            </div>
            <Divider orientation="vertical" flexItem />
            <span>2454 Ratings</span>
          </div>
          <div>
            <div className="price flex items-center gap-3 mt-5 text-2xl">
              <span className="font-sans text-gray-800">₹ {product.product?.sellingPrice}</span>
              <span className="line-through text-gray-400">₹ {product.product?.mrpPrice}</span>
              <span className="text-primary-color font-semibold">{product.product?.discountPercentage}%</span>
            </div>
            <p className="text-sm mt-3">
              Inclusive of all taxes. Free Shipping above Rs 1500
            </p>
          </div>
          <div className="mt-7 space-y-3">
            <div className="flex items-center gap-4">
              <Shield sx={{ color: teal[500] }} />
              <p>Authentic & Quality Assured</p>
            </div>
            <div className="flex items-center gap-4">
              <WorkspacePremium sx={{ color: teal[500] }} />
              <p>100% money back guarantee</p>
            </div>
            <div className="flex items-center gap-4">
              <LocalShipping sx={{ color: teal[500] }} />
              <p>Free shipping and return</p>
            </div>
            <div className="flex items-center gap-4">
              <Wallet sx={{ color: teal[500] }} />
              <p>Pay on Delivery available</p>
            </div>
          </div>
          <div className="mt-7 space-y-2">
            <h1>Quantity</h1>
            <div className="flex items-center gap-2 w-[140px] justify-between">
              <Button
                disabled={quantity == 1}
                onClick={() => setQuantity(quantity - 1)}
              >
                <Remove />
              </Button>
              <span>{quantity}</span>
              <Button onClick={() => setQuantity(quantity + 1)}>
                <Add />
              </Button>
            </div>
          </div>

          <div className="mt-12 flex items-center gap-5">
            <Button
              fullWidth
              startIcon={<AddShoppingCart />}
              sx={{ py: "1rem" }}
              variant="contained"
              color="primary"
              onClick={()=> handleAddToCart()}
            >
              Add To Cart
            </Button>

            <Button
              fullWidth
              startIcon={<FavoriteBorder />}
              onClick={(e) => handleWishlist(e)}
              sx={{ py: "1rem" }}
              variant="outlined"
              color="primary"
            >
              Wishlist
            </Button>
          </div>

          <div className="mt-5">
            <p>
              {product.product?.description}
            </p>
          </div>

          <div className="mt-12 space-y-5">
            <div>
              <h1 className="text-lg font-bold mb-4">Reviews & Ratings</h1>
              <Divider />
            </div>
            {review.reviews.length > 0 ? review.reviews.slice(0,2).map((review) => <ReviewCard review={review}/>) : 
            <div>
              <h1 className="text-lg font-bold">No Reviews</h1>
              <p>Be the first one to review this product after buying !!!!</p>
            </div>
            }
            <Divider />
            {review.reviews.length > 0 && <div className="mt-12 flex items-center gap-5">
            <Button
              fullWidth
              onClick={(e) => handleReviewClick(e)}
              sx={{ py: "1rem" }}
              variant="text"
              color="primary"
            >
              View All {review.reviews.length} Reviews
            </Button>
            </div>}
          </div>
        </section>
      </div>

      <div className="mt-20">
        <h1 className="text-lg font-bold">Similar Product</h1>
        <div className="pt-5"><SimilarProduct /></div>
        
      </div>

    </div>
  );
};

export default ProductDetail;
