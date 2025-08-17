import * as React from "react";
import { styled } from "@mui/material/styles";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell, { tableCellClasses } from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import store, { useAppDispatch, useAppSelector } from "../../../State/Store";
import {
  fetchSellerOrders,
  updateOrderStatus,
} from "../../../State/seller/sellerOrderslice";
import { Button, Menu, MenuItem } from "@mui/material";
import { Label } from "@mui/icons-material";

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

const orderStatusColor = {
  PENDING: { color: "#FFA500", label: "PENDING" },
  PLACED: { color: "#0000FF", label: "PLACED" },
  CONFIRMED: { color: "#008000", label: "CONFIRMED" },

  SHIPPED: { color: "#1E90FF", label: "SHIPPED" },
  DELIVERED: { color: "#32CD32", label: "DELIVERED" },
  CANCELLED: { color: "#FF0000", label: "CANCELLED" },
};

const orderStatus = [
  { color: "#FFA500", label: "PENDING" },
  { color: "#0000FF", label: "PLACED" },
  { color: "#008000", label: "CONFIRMED" },

  { color: "#1E90FF", label: "SHIPPED" },
  { color: "#32CD32", label: "DELIVERED" },
  { color: "#FF0000", label: "CANCELLED" },
];

export default function OrderTable() {
  const dispatch = useAppDispatch();

  const { sellerOrder } = useAppSelector((store) => store);

  React.useEffect(() => {
    dispatch(fetchSellerOrders(localStorage.getItem("jwt") || ""));
  }, []);
  const [anchorEl, setAnchorEl] = React.useState<null | any>({});
  const open = Boolean(anchorEl);
  
  const handleClick = (event: any, orderId: number) => {
    setAnchorEl((prev: any) => ({ ...prev, [orderId]: event.currentTarget }));
  };
  const handleClose = (orderId: number) => () =>  {
    setAnchorEl((prev: any) => ({ ...prev, [orderId]: null }));
  };
  const handleUpdateOrderStatus = (orderId: number, orderStatus: any) => {
    dispatch(
      updateOrderStatus({
        jwt: localStorage.getItem("jwt") || "",
        orderId,
        orderStatus,
      })
    );
    handleClose(orderId);
  };
  return (
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 700 }} aria-label="customized table">
        <TableHead>
          <TableRow>
            <StyledTableCell>Order Id</StyledTableCell>
            <StyledTableCell>Products</StyledTableCell>
            <StyledTableCell align="right">Shipping Address</StyledTableCell>
            <StyledTableCell align="right">Order Status</StyledTableCell>
            <StyledTableCell align="right">Update</StyledTableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {sellerOrder.orders.map((order) => (
            <StyledTableRow key={order.id}>
              <StyledTableCell component="th" scope="row">
                {order.id}
              </StyledTableCell>
              <StyledTableCell>
                <div className="flex gap-1 flex-wrap">
                  {order.orderItems.map((orderItem) => (
                    <div className="flex gap-5">
                      <img
                        className="w-20 rounded-md"
                        src={orderItem.product.images[0]}
                        alt=""
                      />
                      <div className="flex flex-col justify-between py-2">
                        <h1>Title: {orderItem.product.title}</h1>
                        <h1>Selling Price: {orderItem.product.sellingPrice}</h1>
                        <h1>Color: {orderItem.product.color}</h1>
                      </div>
                    </div>
                  ))}
                </div>
              </StyledTableCell>
              <StyledTableCell align="right">
                <div className="flex flex-col gap-y-2">
                  <h1>{order.shipmentAddress.name}</h1>
                  <h1>
                    {order.shipmentAddress.address},{" "}
                    {order.shipmentAddress.city}
                  </h1>
                  <h1>
                    {order.shipmentAddress.state} -{" "}
                    {order.shipmentAddress.pinCode}
                  </h1>
                  <h1>
                    <strong>Mobile: </strong> {order.shipmentAddress.name}
                  </h1>
                </div>
              </StyledTableCell>
              <StyledTableCell align="right">
                <span className="px-5 py-2 border text-primary-color border-primary-color rounded-full">
                  {order.orderStatus}
                </span>
              </StyledTableCell>
              <StyledTableCell align="right">
                <Button
                  onClick={(e) => handleClick(e, Number(order.id))}
                >
                  Status
                </Button>
                <Menu
                  id={`status-menu ${order.id}`}
                  anchorEl={anchorEl[order.id]}
                  open={Boolean(anchorEl[order.id])}
                  onClose={handleClose(order.id)}
                  MenuListProps={{
                    "aria-labelledby": `status-menu ${order.id}`,
                  }}
                >
                  {orderStatus.map((item) => (
                    <MenuItem
                      key={item.label}
                      onClick={() =>
                        handleUpdateOrderStatus(order.id, item.label)
                      }
                    >
                      {item.label}
                    </MenuItem>
                  ))}
                </Menu>
              </StyledTableCell>
            </StyledTableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}
