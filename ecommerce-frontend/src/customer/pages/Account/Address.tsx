
import UserAddressCard from './UserAddressCard'
import store, {  useAppSelector } from '../../../State/Store'

const Address = () => {
  const {auth} = useAppSelector(store=>store);
  console.log("Auth User----------------", auth.user);
  return (
    <div className='space-y-3'>
      {auth.user && auth.user.addresses.map((address) => <UserAddressCard address={address}/>)}
    </div>
  )
}

export default Address
