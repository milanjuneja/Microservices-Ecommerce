import React from 'react'
import { Product } from '../../../types/ProductTypes'
import { Close } from '@mui/icons-material'
import { useAppDispatch } from '../../../State/Store'
import { addProductToWishlist } from '../../../State/customer/wishlistSlice'
import { teal } from '@mui/material/colors'
import { showSnackbar } from '../../../State/SnackbarSlice'

const WishlistProductCard = ({item}:{item:Product}) => {
  const dispatch = useAppDispatch()
  const handleWishlist = () => {
      
      item.id &&
        dispatch(
          addProductToWishlist({
            jwt: localStorage.getItem("jwt") || "",
            productId: item.id,
          })
        );
        dispatch(showSnackbar({ message: "Product removed from wishlist", severity: "success" }));
    };
  return (
    <div className='w-60 relative'>
      <div className='w-full'>
        <img src={item.images[0]} className='object-top w-full' alt="" />
      </div>
      <div className='pt-3 space-y-1 '>
        <p>{item.title}</p>
        <div className="price flex items-center gap-3">
            <span className="font-sans text-gray-800">
              ₹ {item.sellingPrice}
            </span>
            <span className="thin-line-through text-gray-400">
              ₹ {item.mrpPrice}
            </span>
            <span className="text-primary-color font-semibold">
              {item.discountPercentage}%
            </span>
          </div>
          <div className='absolute top-1 right-1'>
            <button onClick={handleWishlist}>
              <Close className='cursor-pointer bg-white rounded-full p-1' sx={{color:teal[500], fontSize:"2rem"}}/>
            </button>
          </div>
      </div>
      
    </div>
  )
}

export default WishlistProductCard
