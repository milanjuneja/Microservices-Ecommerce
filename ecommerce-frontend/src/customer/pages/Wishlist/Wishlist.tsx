import React, { useEffect } from "react";
import WishlistProductCard from "./WishlistProductCard";
import store, { useAppDispatch, useAppSelector } from "../../../State/Store";
import { getWishlistByUserId } from "../../../State/customer/wishlistSlice";

const Wishlist = () => {
  const dispatch = useAppDispatch();
  const { wishlist } = useAppSelector((store) => store);
  useEffect(() => {
    dispatch(getWishlistByUserId(localStorage.getItem("jwt") || ""));
  }, []);
  return (
    <div className="h-[85vh] p-5 lg:p-20">
      <section>
        {wishlist.wishlist && wishlist.wishlist?.products.length > 0 ? (
          <h1>
            <strong>My Wishlist</strong> Items:{" "}
            {wishlist.wishlist?.products.length}
          </h1>
        ) : (
          <div className="text-center">
            <h1 className="font-semibold">Hey it feels so light!!</h1>
            <h3 className="text-x text-slate-600">There is nothing in your wishlist, lets add some items</h3>
          </div>
        )}
        <div className="pt-10 flex flex-wrap gap-5">
          {wishlist.wishlist?.products.map((item) => (
            <WishlistProductCard item={item} />
          ))}
        </div>
      </section>
    </div>
  );
};

export default Wishlist;
