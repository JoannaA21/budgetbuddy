const Expense = require('../models/expense');
const { Sequelize } = require('sequelize');


// Create expense
const createExpense = async(req, res, next) => {
    const {expense_type, cost, user_id} = req.body;

    try {

        const expense = await Expense.create({
            expense_type, 
            cost, 
            user_id
        });

        res.status(201).json(expense);
    } catch (err) {
      next(err);
      console.log('error: ' + err.message);
    }
};


//Get goal for specific user
const getExpense_ByUserId = async(req, res, next) => {
    const {id} = req.params;

    try{
        const userExpense = await Expense.findAll({'user_id': id});
        res.status(200).json(userExpense);
    }catch  (err) {
        next(err);
        console.log('error: ' + err.message);
      }
}


module.exports = {
    createExpense,
    getExpense_ByUserId
}