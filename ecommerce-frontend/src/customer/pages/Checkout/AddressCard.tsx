import { Radio } from "@mui/material";
import { Address } from "../../../types/userTypes";

const AddressCard = ({
  item,
  selectedAddressId,
  setSelectedAddressId,
}: {
  item: Address;
  selectedAddressId: number | null;
  setSelectedAddressId: (id: number) => void;
}) => {
  return (
    <div className={`p-5 border rounded-md flex ${selectedAddressId === item.id ? "border-blue-500" : "border-gray-300"}`}>
      <div>
        <Radio
          checked={selectedAddressId === item.id}
          onChange={() => setSelectedAddressId(Number(item.id))}
          value={item.id}
          name="radio-button"
        />
      </div>
      <div className="space-y-3 pt-3">
        <h1>{item.name}</h1>
        <p className="w-[320px]">{item.address}, {item.city}, {item.state}, {item.pinCode}</p>
        <p><strong>Mobile:</strong> {item.mobile}</p>
      </div>
    </div>
  );
};

export default AddressCard;
