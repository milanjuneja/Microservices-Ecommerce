import { Divider } from "@mui/material";
import { useAppSelector } from "../../../State/Store";

const PricingCard = () => {
  const {cart} = useAppSelector(store=>store);
  const subTotal = cart.cart?.cartItems.reduce((acc, item) => acc + item.sellingPrice, 0);
  const shipping = 69;
  
  return (
    <>
      <div className="space-y-3 p-5">
        <div className="flex justify-between items-center">
          <span>Subtotal</span>
          <span>₹{subTotal}</span>
        </div>

        <div className="flex justify-between items-center">
          <span>Discount</span>
          <span>₹{cart.cart?.discount}</span>
        </div>

        <div className="flex justify-between items-center">
          <span>Shipping</span>
          <span>₹{shipping}</span>
        </div>

        <div className="flex justify-between items-center">
          <span>Platform fee</span>
          <span>free</span>
        </div>
        
      </div>
      <Divider />
        <div className="flex justify-between items-center p-5 text-primary-color">
          <span>Total</span>
          <span>₹{subTotal &&  subTotal + shipping}</span>
        </div>
    </>
  );
};

export default PricingCard;
