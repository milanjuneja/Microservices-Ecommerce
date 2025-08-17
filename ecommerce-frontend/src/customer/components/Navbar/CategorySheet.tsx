import { menLevelTwo } from "../../../data/category/level two/menLevelTwo";
import { womenLevelTwo } from "../../../data/category/level two/womenLevelTwo";
import { electronicsLevelTwo } from "../../../data/category/level two/electronicsLevelTwo";
import { furnitureLevelTwo } from "../../../data/category/level two/furnitureLevelTwo";
import { menLevelThree } from "../../../data/category/level three/menLevelThree";
import { womenLevelThree } from "../../../data/category/level three/womenLevelThree";
import { electronicsLevelThree } from "../../../data/category/level three/electronicsLevelThree";
import { furnitureLevelThree } from "../../../data/category/level three/furnitureLevelThree";
import { Box } from "@mui/material";
import { useNavigate } from "react-router-dom";

const categoryTwo: { [key: string]: any } = {
  men: menLevelTwo,
  women: womenLevelTwo,
  electronics: electronicsLevelTwo,
  home_furniture: furnitureLevelTwo,
};

const categoryThree: { [key: string]: any } = {
  men: menLevelThree,
  women: womenLevelThree,
  electronics: electronicsLevelThree,
  home_furniture: furnitureLevelThree,
};

const CategorySheet = ({ selectredCategory }: any) => {
  const navigate = useNavigate();

  const childCategory = (category: any, parentCategoryId: any) => {
    if (category === undefined) return;
    return category.filter(
      (child: any) => child.parentCategoryId === parentCategoryId
    );
  };

  return (
    <Box
      sx={{ zIndex: 2 }}
      className="bg-white shadow-lg lg:h-[500px] overflow-y-auto"
    >
      <div className="flex flex-wrap text-sm">
        {categoryTwo[selectredCategory]?.map((item: any, index: any) => (
          <div
            className={`p-8 lg:w-[20%] ${
              index % 2 === 0 ? "bg-slate-50" : "bg-white"
            }`}
            key={item.categoryId}
          >
            <p className="text-primary-color mb-5 font-semibold">{item.name}</p>
            <ul className="space-y-3">
              {childCategory &&
                childCategory(
                  categoryThree[selectredCategory],
                  item.categoryId
                ).map((child: any) => (
                  <li
                    onClick={() => navigate("/products/" + child.categoryId)}
                    className="hover:text-primary-color cursor-pointer"
                  >
                    {child.name}
                  </li>
                ))}
            </ul>
          </div>
        ))}
      </div>
    </Box>
  );
};

export default CategorySheet;
