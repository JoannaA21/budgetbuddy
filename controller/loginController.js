const jwt = require('jsonwebtoken');
const { Sequelize } = require('sequelize');
const Profile = require('../models/users');
require('dotenv').config();
const SECRET_KEY = process.env.SECRET_KEY; // Replace with your actual secret key for JWT

const login = async (req, res, next) => {
  try {
    const {
      email,
      password
    } = req.body;

    if (!email) {
      return res.status(400).json({
        message: 'Email is required.'
      });
    }


    // Find the user by checking both username and email
    const user = await Profile.findOne({
      where: Sequelize.or({
          email: email
        } // Check if the username matches the email field
      ),
    });

    if (!user) {
      return res.status(401).json({
        message: 'Invalid email.'
      });
    }

    const isPasswordValid = await user.verifyPassword(password);
    if (!isPasswordValid) {
      return res.status(401).json({
        message: 'Invalid email or password'
      });
    }

    // Generate and send the JWT token on successful login
    const token = jwt.sign({
      userId: user.id,
      userOrEmail: username,
      fname: user.fname,
      lname: user.lname
    }, SECRET_KEY, {
      expiresIn: '1h'
    });
    const details = {
      details: {
        id: user.id,
        fname: user.fname,
        lname: user.lname,
        email: user.email,
      },
      token: token
    };
    res.json({
      details
    });
  } catch (err) {
    console.error('Error logging in:', err);
    res.status(500).json({
      message: 'An error occurred while logging in.'
    });
  }
};

const verify = async (req, res) => {
  console.log(req.headers);
  console.log(req.headers.authorization);
  const jwtToken = req.headers.authorization; // Extract the JWT token from the headers

  try {
    const result = await jwt.verify(jwtToken, SECRET_KEY);
    const currentTime = Math.floor(Date.now() / 1000);
    const expiration = new Date(result.exp * 1000);

    if (currentTime < expiration) {
      result.message = 'User is still logged in';
      console.log(result);
      res.json(result);
    } else {
      result.message = 'User session has expired';
      console.log(result);
      res.json(result);
    }
  } catch (error) {
    console.error('Error:', error.message);
    res.json({
      message: error.message
    });
  }

};

const logout = async (req, res) => {
  try {
    // Assuming you are using JWT for authentication
    // Clear the token from the client-side (e.g., local storage)
    res.clearCookie('token'); // Clear the token cookie

    // Send a response indicating successful logout
    res.json({
      message: 'Logout successful'
    });
  } catch (error) {
    // Handle any errors that might occur during logout
    res.status(500).json({
      error: 'An error occurred during logout'
    });
  }
};


module.exports = {
  login,
  logout,
  verify
};