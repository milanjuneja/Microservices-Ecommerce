import { useEffect } from "react";
import store, { useAppDispatch, useAppSelector } from "../../../State/Store";
import ReviewCard from "./ReviewCard";
import { Divider } from "@mui/material";
import { getReviewsByProductId } from "../../../State/customer/reviewSlice";
import { useParams } from "react-router-dom";
import { fetchProductById } from "../../../State/customer/ProductSlice";

const Review = () => {
  const dispatch = useAppDispatch();
  const { productId } = useParams();
  const {review} = useAppSelector(store => store);
  const {product} = useAppSelector(store => store);

  useEffect(() => {
    dispatch(getReviewsByProductId({ productId: Number(productId)}));
    dispatch(fetchProductById(Number(productId)));
  }, [productId]);

  return (
    <>
      <div className="p-5 lg:px-20 flex flex-col lg:flex-row gap-20">
        <section className="w-full md:w-1/2 lg:w-[30%] space-y-2">
          <img
            src={product.product?.images[0]}
            alt=""
          />

          <div>
            <div>
              <p className="font-bold text-xl">{product.product?.seller.businessDetails.businessName}</p>
              <p className="text-lg text-gray-600">{product.product?.title}</p>
            </div>
            <div>
              <div className="price flex items-center gap-3 mt-5 text-2xl">
                <span className="font-sans text-gray-800">₹ {product.product?.sellingPrice}</span>
                <span className="line-through text-gray-400">₹ {product.product?.mrpPrice}</span>
                <span className="text-primary-color font-semibold">{product.product?.discountPercentage}%</span>
              </div>
            </div>
          </div>
        </section>

        <section className="space-y-5 w-full">
          {review.reviews.map((review) => (
            <div className="space-y-3">
              <ReviewCard review={review}/>
              <Divider />
            </div>
          ))}
        </section>
      </div>
    </>
  );
};

export default Review;
