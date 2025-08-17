export const uploadToCloudinary = async (pics:any) => {
  const cloud_name = "dmuod8waj"
  const upload_preset = "ecommerce"

  if(pics){
    const data = new FormData();
    data.append("file", pics);
    data.append("upload_preset", upload_preset);
    data.append("cloud_name", cloud_name);
    // const 
    const res = await fetch("https://api.cloudinary.com/v1_1/dmuod8waj/upload" , {
      method:"POST",
      body:data
    });

    const fileData = await res.json();
    return fileData.url;
  }
  throw new Error("Error: pics not found");
}