import { Route, Routes } from 'react-router-dom'
import SellerTable from '../admin/Pages/Seller/SellerTable'
import Coupon from '../admin/Pages/Coupon/Coupon'
import AddNewCoupon from '../admin/Pages/Coupon/AddNewCoupon'
import GridTable from '../admin/Pages/HomePage/GridTable'
import ElectronicTable from '../admin/Pages/HomePage/ElectronicTable'
import ShopByCategory from '../admin/Pages/HomePage/ShopByCategory'
import Deal from '../admin/Pages/HomePage/Deal'
import GlobalSnackbar from '../component/GlobalSnacker'

const AdminRoutes = () => {
  return (
    <div>
      <GlobalSnackbar />
      <Routes>
        <Route path='/' element={<SellerTable />}/>
        <Route path='/coupon' element={<Coupon />}/>
        <Route path='/add-coupon' element={<AddNewCoupon />}/>
        <Route path='/home-grid' element={<GridTable />}/>
        <Route path='/electronics-category' element={<ElectronicTable />}/>
        <Route path='/shop-by-category' element={<ShopByCategory />}/>
        <Route path='/deals' element={<Deal />}/>
      </Routes>
    </div>
  )
}

export default AdminRoutes
