import React from 'react'

const SimilarProductCard = () => {
  return (
    <div>
        <div className="group relative px-4">
        <div
          className="card"
        >
          
            <img
              className="card-media object-top"
              src="https://veirdo.in/cdn/shop/files/Artboard_30.jpg?v=1727412083&width=533"
              alt=""
             
            />
          </div>
          <div className="details pt-3 space-y-1 group-hover-effect rounded-md">
            <div className="name">
              <h1>Nike</h1>
              <p>Blue shirt</p>
            </div>
            <div className="price flex items-center gap-3">
              <span className="font-sans text-gray-800">
              ₹ 400
              </span>
              <span className="thin-line-through text-gray-400">₹ 999</span>
              <span className="text-primary-color font-semibold">60%</span>
            </div>
        </div>
      </div>
    </div>
  )
}

export default SimilarProductCard
        