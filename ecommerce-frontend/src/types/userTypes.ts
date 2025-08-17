export interface Address {
  id?:number,
  name: String;
  locality: String;
  address: String;
  city: String;
  state: String;
  pinCode: String;
  mobile: String;
}

export enum UserRole{
  ROLE_CUSTOME = 'ROLE_CUSTOMER',
  ROLE_ADMIN = 'ROLE_ADMIN',
  ROLE_SELLER = 'ROLE_SELLER'
}

export interface User{
  id?:number,
  password?: string,
  email: string,
  firstName:string,
  lastName: string,
  mobile?: string,
  role: UserRole,
  addresses: Address[]
}