import {
  Button,
  FormControl,
  InputLabel,
  Menu,
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
import { getAllSellers, updateSellerStatus } from "../../../State/seller/sellerSlice";
import { showSnackbar } from "../../../State/SnackbarSlice";

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

const accountStat = [
  {
    status: "PENDING_VERIFICATION",
    title: "Pending Verification",
    description: "Account is pending for verification",
  },
  {
    status: "ACTIVE",
    title: "Active",
    description: "Account is pending for verification",
  },
  {
    status: "SUSPENDED",
    title: "Suspended",
    description: "Account is pending for verification",
  },
  {
    status: "DEACTIVATED",
    title: "Deactivated",
    description: "Account is pending for verification",
  },
  {
    status: "BANNED",
    title: "Banned",
    description: "Account is pending for verification",
  },
  {
    status: "CLOSED",
    title: "Closed",
    description: "Account is pending for verification",
  },
];

const SellerTable = () => {
  const [accountStatus, setAccountStatus] = useState("PENDING_VERIFICATION");
  const dispatch = useAppDispatch();
  const { seller } = useAppSelector((store) => store);
  useEffect(() => {
    dispatch(getAllSellers(accountStatus));
  }, [accountStatus]);
  const handleChange = (event: any) => {
    setAccountStatus(event.target.value);
  };
  const [anchorEl, setAnchorEl] = React.useState<null | any>({});

  const handleClick = (event: any, sellerId: number) => {
    setAnchorEl((prev: any) => ({ ...prev, [sellerId]: event.currentTarget }));
  };
  const handleClose = (sellerId: number) => () =>  {
    setAnchorEl((prev: any) => ({ ...prev, [sellerId]: null }));
  };
  const handleUpdateSellerStatus = (sellerId: number, sellerStatus: any) => {
      dispatch(updateSellerStatus({ status: sellerStatus, sellerId }));
      handleClose(sellerId);
      showSnackbar({ message: "Seller status updated successfully", severity: "success" });
    };
  return (
    <>
      <div className="text-center">
        <h1 className="text-3xl text-primary-color rounded-md mb-4">
          Seller Details
        </h1>
      </div>
      <div className="pb-5 w-60">
        <FormControl fullWidth>
          <InputLabel id="demo-simple-select-label">Account Status</InputLabel>
          <Select
            labelId="demo-simple-select-label"
            id="demo-simple-select"
            value={accountStatus}
            label="Account Status"
            onChange={handleChange}
          >
            {accountStat.map((item) => (
              <MenuItem value={item.status}>{item.title}</MenuItem>
            ))}
          </Select>
        </FormControl>
      </div>
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 700 }} aria-label="customized table">
          <TableHead>
            <TableRow>
              <StyledTableCell>Seller Name</StyledTableCell>
              <StyledTableCell>Email</StyledTableCell>
              <StyledTableCell align="right">Mobile</StyledTableCell>
              <StyledTableCell align="right">GSTIN</StyledTableCell>
              <StyledTableCell align="right">Business Name</StyledTableCell>
              <StyledTableCell align="right">Account Status</StyledTableCell>
              <StyledTableCell align="right">Change Status</StyledTableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {seller.sellers.map((seller) => (
              <StyledTableRow key={seller.id}>
                <StyledTableCell component="th" scope="row">
                  {seller.sellerName}
                </StyledTableCell>
                <StyledTableCell>{seller.email}</StyledTableCell>
                <StyledTableCell align="right">{seller.mobile}</StyledTableCell>
                <StyledTableCell align="right">{seller.GSTIN}</StyledTableCell>
                <StyledTableCell align="right">
                  {seller.businessDetails.businessName}
                </StyledTableCell>
                <StyledTableCell align="right">
                  {seller.accountStatus}
                </StyledTableCell>
                <StyledTableCell align="right">
                   <Button onClick={(e) => handleClick(e, Number(seller.id))}>
                    Status
                  </Button>
                  <Menu
                    id={`status-menu ${seller.id}`}
                    anchorEl={anchorEl[seller.id || 0]}
                    open={Boolean(anchorEl[seller.id || 0])}
                    onClose={handleClose(seller.id || 0)}
                    MenuListProps={{
                      "aria-labelledby": `status-menu ${seller.id}`,
                    }}
                  >
                    {accountStat.map((item) => (
                      <MenuItem
                        key={item.status}
                        onClick={() =>
                          handleUpdateSellerStatus(seller.id || 0, item.status)
                        }
                      >
                        {item.title}
                      </MenuItem>
                    ))}
                  </Menu> 
                </StyledTableCell>
              </StyledTableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </>
  );
};

export default SellerTable;
