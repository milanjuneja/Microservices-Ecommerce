import {
  Box,
  Button,
  FormControlLabel,
  Modal,
  Radio,
  RadioGroup,
  Typography,
} from "@mui/material";
import AddressCard from "./AddressCard";
import React, { useState } from "react";
import AddressForm from "./AddressForm";
import PricingCard from "../Cart/PricingCard";
import store, { useAppDispatch, useAppSelector } from "../../../State/Store";
import { createOrder } from "../../../State/customer/orderSlice";

const paymentGatewayList = [
  {
    value: "RAZORPAY",
    image:
      "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEzHTRfO-BS6NfMWXr8zbb3d-pcOCPNgJX9A&s",
    label: "",
  },
  {
    value: "STRIPE",
    image:
      "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRek2EqBo5YIE0TPMVMlIFA594WZZeuqYdAQQ&s",
    label: "",
  },
];

const Checkout = () => {
  const { auth } = useAppSelector((store) => store);
  const [open, setOpen] = React.useState(false);
  const dispatch = useAppDispatch();

  // 🟢 Store selected address ID
  const [selectedAddressId, setSelectedAddressId] = useState<number | null>(null);
  const [paymentGateway, setPaymentGateway] = useState("RAZORPAY");

  // 🟢 Handle Checkout
  const handleCheckout = () => {
    if (!selectedAddressId) {
      alert("Please select an address before checkout.");
      return;
    }

    const selectedAddress = auth.user?.addresses.find(
      (item) => item.id === selectedAddressId
    );

    if (selectedAddress) {
      dispatch(
        createOrder({
          address: selectedAddress,
          jwt: localStorage.getItem("jwt") || "",
          paymentGateway,
        })
      );
      console.log("Order Created:", { selectedAddress, paymentGateway });
    }
  };

  return (
    <>
      <div className="pt-10 px-5 sm:px-10 md:px-44 lg:px-60 min-h-screen">
        <div className="space-y-5 lg:space-y-0 lg:grid grid-cols-3 lg:gap-9">
          <div className="col-span-2 space-y-5">
            <div className="flex justify-between items-center">
              <h1 className="font-semibold">Select Address</h1>
              <Button onClick={() => setOpen(true)}>Add new Address</Button>
            </div>
            <div className="text-xs font-medium space-y-5">
              <p>Saved Addresses</p>
              <div className="space-y-3">
                {auth.user?.addresses.map((item) => (
                  <AddressCard
                    key={item.id}
                    item={item}
                    selectedAddressId={selectedAddressId}
                    setSelectedAddressId={setSelectedAddressId}
                  />
                ))}
              </div>
            </div>
          </div>

          <div>
            <div className="space-y-3 border p-5 rounded-md">
              <h1 className="text-primary-color font-medium pb-2 text-center">
                Choose Payment Gateway
              </h1>
              <RadioGroup
                row
                className="flex justify-between pr-0"
                onChange={(e) => setPaymentGateway(e.target.value)}
                value={paymentGateway}
              >
                {paymentGatewayList.map((item) => (
                  <FormControlLabel
                    key={item.value}
                    className="border w-[45%] pr-2 rounded-md flex justify-center"
                    value={item.value}
                    control={<Radio />}
                    label={
                      <img
                        className="object-cover w-14"
                        src={item.image}
                        alt={item.label}
                      />
                    }
                  />
                ))}
              </RadioGroup>
            </div>

            <div className="border rounded-md">
              <PricingCard />
              <div className="p-5">
                <Button
                  type="button"
                  fullWidth
                  sx={{ py: "11px" }}
                  variant="contained"
                  onClick={handleCheckout} // 🟢 Call handleCheckout
                >
                  Checkout
                </Button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <Modal open={open} onClose={() => setOpen(false)}>
        <Box
          sx={{ width: 500, p: 4, bgcolor: "background.paper", boxShadow: 24 }}
        >
          <AddressForm paymentGateway={paymentGateway} />
        </Box>
      </Modal>
    </>
  );
};

export default Checkout;
