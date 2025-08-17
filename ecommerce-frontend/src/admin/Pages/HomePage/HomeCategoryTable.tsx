import * as React from "react";
import { styled } from "@mui/material/styles";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell, { tableCellClasses } from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import { Box, Button, Modal } from "@mui/material";
import { Edit } from "@mui/icons-material";
import { HomeCategory } from "../../../types/HomeCategoryTypes";
import UpdateCategoryForm from "./UpdateCategoryForm";

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

export default function HomeCategoryTable({ data }: { data: HomeCategory[] }) {
  const [open, setOpen] = React.useState(false);
  const [categoryId, setCategoryId] = React.useState(0);
  return (
    <>
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 700 }} aria-label="customized table">
          <TableHead>
            <TableRow>
              <StyledTableCell>No</StyledTableCell>
              <StyledTableCell>Id</StyledTableCell>
              <StyledTableCell>Image</StyledTableCell>
              <StyledTableCell align="right">Category</StyledTableCell>
              <StyledTableCell align="right">Update</StyledTableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {data.map((category, index) => (
              <StyledTableRow key={category.id}>
                <StyledTableCell component="th" scope="row">
                  {index + 1}
                </StyledTableCell>
                <StyledTableCell>{category.id}</StyledTableCell>
                <StyledTableCell>
                  <img
                    className="w-20 rounded-md"
                    src={category.image}
                    alt=""
                  />
                </StyledTableCell>
                <StyledTableCell align="right">
                  {category.categoryId}
                </StyledTableCell>
                <StyledTableCell align="right">
                  <Button onClick={() => {
                    setOpen(true)
                    setCategoryId(Number(category.id))
                  }}>
                    <Edit />
                  </Button>
                </StyledTableCell>
              </StyledTableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      <Modal
        style={{
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
        }}
        open={open}
        onClose={() => setOpen(false)}
      >
        <Box
          sx={{ width: 500, p: 4, bgcolor: "background.paper", boxShadow: 24 }}
        >
          <UpdateCategoryForm category={categoryId} />
        </Box>
      </Modal>
    </>
  );
}
