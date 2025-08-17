import ElectricCategory from "./ElectricCategory/ElectricCategory";
import CategoryGrid from "./CategoryGrid/CategoryGrid";
import Deal from "./Deal/Deal";
import ShopByCategory from "./ShopByCategory/ShopByCategory";
import { Button } from "@mui/material";
import { Storefront } from "@mui/icons-material";

const Home = () => {
  return (
    <div className="space-y-5 lg:space-y-10 relative pb-20">
      <ElectricCategory />
      <CategoryGrid />

      <div className="pt-20">
        <h1
          className="text-lg lg:text-4xl font-bold text-primary-color pb-5 lg:pb-10
        text-center"
        >
          TODAY'S DEALS
        </h1>
        <Deal />
      </div>

      <section className="py-20">
        <h1
          className="text-lg lg:text-4xl font-bold text-primary-color pb-5 lg:pb-10
        text-center"
        >
          SHOP BY CATEGORY
        </h1>
        <ShopByCategory />
      </section>

      <section className=" lg:px-20 relative h-[200px] lg:h-[450px] object-cover">
        <img
          className="w-full h-full"
          src="https://cdn.create.vista.com/api/media/small/429497366/stock-photo-smiling-woman-red-dress-heels-sitting-sleigh-grey-background-banner"
          alt=""
        />
        <div className="absolute top-1/2 left-4 lg:left-[15rem] transform -translate-y-1/2 font-semibold lg:text-4xl space-y-3">
          <h1>Sell your products</h1>
          <p className="text-lg md:text-2xl ml-3">
            With{" "}
            <span className="logo text-primary-color">Ecommerce Website</span>
          </p>
          <div className="pt-4 flex justify-center mr-6">
            <Button
              startIcon={<Storefront />}
              variant="contained"
              className="bg-primary-color text-white"
            >
              Become Seller
            </Button>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Home;
