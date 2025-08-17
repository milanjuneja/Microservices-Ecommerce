export const MainCategory = [
  {
    name: "Men",
    categoryId: "men",
    level: 1,
    levelTwoCategory: [
      {
        "name" : "Topwear",
        "categoryId": "men_topwear",
        "parentCategoryId": "men",
        "level": 2,
      },
      {
        "name" : "Bottomwear",
        "categoryId": "men_bottomwear",
        "parentCategoryId": "men",
        "level": 2,
      }
    ]
  },
  {
    name: "Women",
    categoryId : "women",
    level: 1,
    levelTwoCategory: [
      {
        "name": "Indian & fusion wear",
        "categoryId":"women_indian_fusion_wear",
        "parentCategoryId":"women",
        "level": 2,
      },
      {

      }
    ]
  },
  {
    name: "Home & Furniture",
    categoryId: "home_furniture",
    level:1
  },
  {
    name: "Electronics",
    categoryId: "electronics",
    level:1
  }
]