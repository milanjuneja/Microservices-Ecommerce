import {
  Button,
  Divider,
  FormControl,
  FormControlLabel,
  FormLabel,
  Radio,
  RadioGroup,
} from "@mui/material";
import { teal } from "@mui/material/colors";
import { colors } from "../../../data/filter/color";
import { useState } from "react";
import { price } from "../../../data/filter/price";
import { discount } from "../../../data/filter/discount";
import { useSearchParams } from "react-router-dom";
import { brands } from "../../../data/filter/brand";

const FilterSection = () => {
  const [expandColor, setExpandColor] = useState(false);
  const [searchParams, setSearchParams] = useSearchParams();
  const [expandBrand, setExpandBrand] = useState(false);

  const handleBrandToggle = () => {
    setExpandBrand(!expandBrand);
  }

  const handleColorToggle = () => {
    setExpandColor(!expandColor);
  };
  const updateFilterParams = (event: any) => {
    const { value, name } = event.target;
    if (value) searchParams.set(name, value);
    else searchParams.delete(name);
    setSearchParams(searchParams);
  };

  const clearAllFilters = () => {
    console.log("clear", searchParams);

    searchParams.forEach((value, key) => {
      searchParams.delete(key);
    });
    setSearchParams(searchParams);
  };

  return (
    <div className="-z-50 space-y-5 bg-white">
      <div className="flex items-center justify-between h-[40px] px-9 lg:border-r">
        <p className="text-lg font-semibold">Filters</p>
        <Button
          onClick={clearAllFilters}
          size="small"
          className="text-teal-600 cursor-pointer font-semibold"
        >
          Clear all
        </Button>
      </div>
      <Divider />
      <div className="px-9 space-y-6">
        <section>
          <FormControl>
            <FormLabel
              sx={{
                fontSize: "16px",
                fontWeight: "bold",
                color: teal[500],
                pb: "14px",
              }}
              className="text-2xl font-semibold"
              id="color"
            >
              Color
            </FormLabel>
            <RadioGroup
              onChange={updateFilterParams}
              aria-labelledby="color"
              defaultValue=""
              name="color"
            >
              {colors.slice(0, expandColor ? colors.length : 5).map((color) => (
                <FormControlLabel
                  value={color.name}
                  control={<Radio />}
                  label={
                    <div className="flex items-center gap-3">
                      <p>{color.name}</p>
                      <p
                        style={{ backgroundColor: color.hex }}
                        className={`h-5 w-5 rounded-full ${
                          color.name === "white" ? "border" : ""
                        }`}
                      ></p>
                    </div>
                  }
                />
              ))}
            </RadioGroup>
          </FormControl>
          <div>
            <button
              onClick={handleColorToggle}
              className="text-primary-color hover:text-teal-900 flex items-center cursor-pointer"
            >
              {expandColor ? "Hide" : `+${colors.length - 5} more`}
            </button>
          </div>
        </section>

        <section>
          <FormControl>
            <FormLabel
              sx={{
                fontSize: "16px",
                fontWeight: "bold",
                color: teal[500],
                pb: "14px",
              }}
              className="text-2xl font-semibold"
              id="brand"
            >
              Brand
            </FormLabel>
            <RadioGroup
              onChange={updateFilterParams}
              aria-labelledby="brand"
              defaultValue=""
              name="brand"
            >
              {brands.slice(0, expandBrand ? brands.length : 5).map((brand) => (
                <FormControlLabel
                  value={brand.name}
                  control={<Radio />}
                  label={
                    <div className="flex items-center gap-3">
                      <p>{brand.name}</p>
                    </div>
                  }
                />
              ))}
            </RadioGroup>
          </FormControl>
          <div>
            <button
              onClick={handleBrandToggle}
              className="text-primary-color hover:text-teal-900 flex items-center cursor-pointer"
            >
              {expandBrand ? "Hide" : `+${brands.length - 5} more`}
            </button>
          </div>
        </section>

        <section>
          <FormControl>
            <FormLabel
              sx={{
                fontSize: "16px",
                fontWeight: "bold",
                color: teal[600],
                pb: "14px",
              }}
              className="text-2xl font-semibold"
              id="price"
            >
              Price
            </FormLabel>
            <RadioGroup
              onChange={updateFilterParams}
              aria-labelledby="price"
              defaultValue=""
              name="price"
            >
              {price.map((item) => (
                <FormControlLabel
                  key={item.name}
                  value={item.value}
                  control={<Radio size="small" />}
                  label={item.name}
                />
              ))}
            </RadioGroup>
          </FormControl>
        </section>

        <section>
          <FormControl>
            <FormLabel
              sx={{
                fontSize: "16px",
                fontWeight: "bold",
                color: teal[600],
                pb: "14px",
              }}
              className="text-2xl font-semibold"
              id="brand"
            >
              Discount
            </FormLabel>
            <RadioGroup
              onChange={updateFilterParams}
              aria-labelledby="brand"
              defaultValue=""
              name="discount"
            >
              {discount.map((item) => (
                <FormControlLabel
                  key={item.name}
                  value={item.value}
                  control={<Radio size="small" />}
                  label={item.name}
                />
              ))}
            </RadioGroup>
          </FormControl>
        </section>
      </div>
    </div>
  );
};

export default FilterSection;
