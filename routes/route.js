const express = require('express');
const router = express.Router();
const expenseController = require('../controller/expenseController');
const goalController = require('../controller/goalController');
const userController = require('../controller/usersController');
const incomeController = require('../controller/incomeController');
const { login, logout } = require('../controller/loginController');



// Route for user login
router.post('/login', login);
router.get('/logout', logout);

//Route for createUser
router.post('/createuser', userController.createUser);
//Route for getAllUsers
router.get('/getallusers', userController.getAllUsers);
//Route for getUserById
router.get('/getuserbyid/:id', userController.getUserById);

//Route for createExpense
router.post('/createexpense', expenseController.createExpense);
//Route for getExpense_ByUserId
router.get('/getuserexpense/:id', expenseController.getExpense_ByUserId);

//Route for createGoal
router.post('/creategoal', goalController.createGoal);
//Route for getGoal_ByUserId
router.get('/getusergoal/:id', goalController.getGoal_ByUserId);

//Route for createIncome
router.post('/createincome', incomeController.createIncome);
//Route for getGoal_ByUserId
router.get('/getuserincome/:id', incomeController.getIncome_ByUserId);



module.exports = router;