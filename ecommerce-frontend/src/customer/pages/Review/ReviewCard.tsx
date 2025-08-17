import { Delete } from '@mui/icons-material'
import { Avatar, Box, Grid2, IconButton, Rating } from '@mui/material'
import { red } from '@mui/material/colors'
import { deleteReview, Review } from '../../../State/customer/reviewSlice'
import dayjs from "dayjs";
import { useAppDispatch } from '../../../State/Store';
const ReviewCard = ({review}:{review:Review}) => {
  const formattedDate = dayjs(review.createdAt).format("MMMM D, YYYY");
  const dispatch = useAppDispatch();
  const handleDelete = () => {
    dispatch(deleteReview({reviewId: review.id || 0}));
  }

  return (
    <div className='flex justify-between'>
      <Grid2 container spacing={9}>
        <Grid2 size={{xs:1}}>
          <Box>
            <Avatar className='text-white' sx={{width:56, height:56, bgcolor:"#9155FD"}}>
                {review.user.firstName[0]}{review.user.lastName[0]}
            </Avatar>
          </Box>
        </Grid2>

        <Grid2 size={{xs:9}}>
          <div className='space-y-2'>
            <div>
              <p className='font-semibold text-lg'>{review.user.firstName} {review.user.lastName}</p>
              <p className='opacity-70'>{formattedDate}</p>
            </div>
          </div>
          <Rating 
          readOnly 
          value={review.rating}
          precision={.5}
          sx={{marginLeft:"-5px"}}
          />
          
          <p>{review.reviewText}</p>
            <div>
              <img className='w-24 h-24 object-cover' src={review.images && review.images[0]} alt="" />
            </div>

        </Grid2>

        

      </Grid2>
      <div>
      <IconButton onClick={handleDelete}>
          <Delete sx={{color:red[700]}}/>
        </IconButton>
      </div>
    </div>
  )
}

export default ReviewCard
