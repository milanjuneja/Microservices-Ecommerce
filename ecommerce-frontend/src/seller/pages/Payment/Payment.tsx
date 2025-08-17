import { Button, Card, Divider } from "@mui/material";
import TransactionTable from "../Transaction/TransactionTable";
import { useAppSelector } from "../../../State/Store";

const Payment = () => {
  const { transaction } = useAppSelector((store) => store);

  const totalTransactionAmount = transaction.transactions.reduce(
    (acc, item) => item.order.totalSellingPrice + acc,
    0
  );

  const lastTransaction = transaction.transactions.length;

  return (
    <div className="space-y-5">
      <Card className="rounded-md space-y-4 p-5">
        <h1 className="text-gray-600 font-medium">Total Earning</h1>
        <h1 className="font-bold text-xl pb-1">Rs {totalTransactionAmount}</h1>
        <Divider />
        <p className="text-gray-600 font-medium pt-1">
          Last Payment:{" "}
          <strong>
            {
              transaction.transactions[lastTransaction - 1].order
                .totalSellingPrice
            }
          </strong>
        </p>
      </Card>
      <div className="pt-20 space-y-3">
        <Button variant="contained">Transaction</Button>
        <TransactionTable />
      </div>
    </div>
  );
};

export default Payment;
