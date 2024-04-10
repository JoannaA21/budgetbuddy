const Expense = require('../models/expense');
const { Sequelize } = require('sequelize');
const Income = require('../models/income');

const timeElapsed = Date.now();
const today = new Date(timeElapsed);

// Create expense (post)
const createExpense = async(req, res, next) => {
    const {expense_type, cost, user_id} = req.body;
    var newCost = 0;
    try {
        created_at = today.toISOString();
        updated_at = today.toISOString();

        const expense = await Expense.create({
            expense_type, 
            cost, 
            user_id,
            created_at,
            updated_at
        });

        const myExpenses = await Expense.findAll(
            {
              where: {
                user_id: user_id,
              }
            });
            myExpenses.forEach(expense => {
              console.log(expense.cost);
              newCost += expense.cost;
            });

        const [incomeGoal, created] = await Income.findOrCreate({
            where: { user_id: user_id }, // Conditions to find the record
            defaults: { // Data to be used if no matching record is found
                user_id: user_id,
                monthly_income: 0,
                all_savings: 0,
                current_balance: 0,
                expenses: newCost,
                created_at: today.toISOString(),
                updated_at: today.toISOString()
            }
        });
        
        // If the record was found and updated, log the message
        if (!created) {
            console.log('Existing record updated:', incomeGoal);
            // Update the value of the current_balance field in the incomeGoal object
            console.log(incomeGoal.monthly_income);
            console.log(incomeGoal.all_savings);
            // incomeGoal.monthly_income = parseFloat(monthly_income);
            incomeGoal.expenses = newCost;
            incomeGoal.current_balance = parseFloat(incomeGoal.monthly_income -  newCost) -  parseFloat(incomeGoal.all_savings);
  
            // Save the changes to the database
            await incomeGoal.save();
        } else {
            console.log('New record created:', incomeGoal);
        }      

        res.status(201).json(expense);
    } catch (err) {
      next(err);
      console.log('error: ' + err.message);
    }
};


//Get goal for specific user (get)
const getExpense_ByUserId = async(req, res, next) => {
    const {id} = req.params;

    try{
        const userExpense = await Expense.findAll(
            {
              where: {
                user_id: id,
              }
            });
          // console.log(userGoal);
          if (userExpense.length > 0) {
              console.log('data found!');
              res.status(200).json(userExpense);
          } else {
              console.log('No data found!');
              data = [{
                  "cost": "0",
                  "expense_type": "",
                  "user_id": id
              }]
              res.status(200).json(data);
          }
        // res.status(200).json(userExpense);
    }catch  (err) {
        next(err);
        console.log('error: ' + err.message);
      }
}


module.exports = {
    createExpense,
    getExpense_ByUserId
}