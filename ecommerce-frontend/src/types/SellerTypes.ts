export interface Seller {
  id?: number;

  sellerName: String;

  mobile: String;

  email: String;

  password: String;

  bankDetails: BankDetails;

  pickUpAddress: PickupAddress;

  businessDetails: BusinessDetails;
  GSTIN: String;

  role?: String;

  accountStatus?: String;
}

export interface PickupAddress {
  name: String;
  locality: String;
  address: String;
  city: String;
  state: String;
  pinCode: String;
  mobile: String;
}

export interface BankDetails {
  accountNumber: String;
  accountHolderName: String;
  ifscCode: String;
}

export interface BusinessDetails {
  businessName: String;
}
export interface SellerReport {
  id?: number;

  seller: Seller;
  totalEarnings: number;
  totalSales: number;
  totalRefunds: number;
  totalTax: number;
  netEarnings: number;
  totalOrders: number;
  canceledOrders: number;
  totalTransactions: number;
}
