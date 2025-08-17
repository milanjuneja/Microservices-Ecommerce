import React, { useState } from 'react'
import LoginForm from './LoginForm';
import RegisterForm from './RegisterForm';
import { Button } from '@mui/material';

const Auth = () => {
  const [isLogin, setIsLogin] = useState(true);
  return (
    <div className='flex justify-center h-[90vh] items-center'>
      <div className='max-w-md h-[85vh] rounded-md shadow-lg'>
        <img className='w-[100%] h-[25%]' src="https://thumbs.dreamstime.com/z/login-banner-18483762.jpg" alt="" />
        
        <div className='mt-8 px-10'>
        {isLogin ? <LoginForm /> : <RegisterForm />}
        <div className='flex items-center gap-1 justify-center mt-5'>
          <p>{isLogin && "Don't"} Have Account</p>
          <Button size='small' onClick={()=> setIsLogin(!isLogin)}>
            {isLogin? "Create Account":"Login"}
          </Button>
        </div>
        </div>

      </div>
      
    </div>
  )
}

export default Auth
