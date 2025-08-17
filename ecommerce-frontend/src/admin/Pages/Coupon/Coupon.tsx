import { Delete } from "@mui/icons-material";
import {
  Button,
  FormControl,
  InputLabel,
  MenuItem,
  Paper,
  Select,
  styled,
  Table,
  TableBody,
  TableCell,
  tableCellClasses,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import React, { useEffect, useState } from "react";
import store, { useAppDispatch, useAppSelector } from "../../../State/Store";
import {
  deleteCoupon,
  fetchAllCoupons,
} from "../../../State/admin/adminCouponSlice";

const StyledTableCell = styled(TableCell)(({ theme }) => ({
  [`&.${tableCellClasses.head}`]: {
    backgroundColor: theme.palette.common.black,
    color: theme.palette.common.white,
  },
  [`&.${tableCellClasses.body}`]: {
    fontSize: 14,
  },
}));

const StyledTableRow = styled(TableRow)(({ theme }) => ({
  "&:nth-of-type(odd)": {
    backgroundColor: theme.palette.action.hover,
  },
  // hide last border
  "&:last-child td, &:last-child th": {
    border: 0,
  },
}));

const Coupon = () => {
  const dispatch = useAppDispatch();
  const [couponDeleted, setCouponDeleted] = useState(false);
  const { adminCoupons } = useAppSelector((store) => store);

  useEffect(() => {
    dispatch(fetchAllCoupons());
  }, [couponDeleted]);

  const handleDeleteCoupon = (e: number) => {
    dispatch(deleteCoupon(e));
    setCouponDeleted(true);
  };
  return (
    <>
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 700 }} aria-label="customized table">
          <TableHead>
            <TableRow>
              <StyledTableCell>Coupon Code</StyledTableCell>
              <StyledTableCell>Start Date</StyledTableCell>
              <StyledTableCell>End Date</StyledTableCell>
              <StyledTableCell>Minimun Order value</StyledTableCell>
              <StyledTableCell>Discount</StyledTableCell>
              <StyledTableCell>Delete</StyledTableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {adminCoupons.coupons.map((coupon) => (
              <StyledTableRow key={coupon.id}>
                <StyledTableCell component="th" scope="row">
                  {coupon.code}
                </StyledTableCell>
                <StyledTableCell>{coupon.validityStartDate}</StyledTableCell>
                <StyledTableCell>{coupon.validityEndDate}</StyledTableCell>
                <StyledTableCell>{coupon.minimumOrderValue}</StyledTableCell>
                <StyledTableCell>{coupon.discountPercentage}</StyledTableCell>
                <StyledTableCell>
                  <Button onClick={() => handleDeleteCoupon(coupon.id)}>
                    <Delete />
                  </Button>
                </StyledTableCell>
              </StyledTableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </>
  );
};

export default Coupon;
