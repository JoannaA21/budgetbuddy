const Income = require('../models/income');
const Expense = require('../models/expense');
const Goal = require('../models/goal');
const { Sequelize, DECIMAL, FLOAT } = require('sequelize');

const timeElapsed = Date.now();
const today = new Date(timeElapsed);

// Create income (post)
const createIncome = async(req, res, next) => {
    const {user_id, monthly_income} = req.body;
    var amount_goal = 0;
    var cost = 0;

    try {

        // created = today.toISOString();
        updated_at = today.toISOString();

        const all_savings = await Goal.findAll({where: {user_id}});
        const myExpenses = await Expense.findAll({where:{user_id}});
        console.log(all_savings);
        all_savings.forEach(savings => {
          console.log(savings.amount_goal);
          amount_goal += savings.amount_goal;
        })
        myExpenses.forEach(expense => {
          console.log(expense.cost);
          cost += expense.cost;
        });

        const currentBalance = parseFloat(monthly_income -  cost) -  parseFloat(amount_goal);

        const [incomeGoal, created] = await Income.findOrCreate({
          where: { user_id: user_id }, // Conditions to find the record
          defaults: { // Data to be used if no matching record is found
              user_id: user_id,
              monthly_income: monthly_income,
              all_savings: amount_goal,
              current_balance: currentBalance,
              expenses: cost,
              created_at: today.toISOString(),
              updated_at: today.toISOString()
          }
      });
      
      // If the record was found and updated, log the message
      if (!created) {
          console.log('Existing record updated:', incomeGoal);
          // Update the value of the current_balance field in the incomeGoal object
          incomeGoal.monthly_income = parseFloat(monthly_income);
          // incomeGoal.current_balance = parseFloat(monthly_income -  cost) -  parseFloat(amount_goal);
          incomeGoal.current_balance = parseFloat(monthly_income -  cost);

          // Save the changes to the database
          await incomeGoal.save();
      } else {
          console.log('New record created:', incomeGoal);
      }      

        res.status(201).json(incomeGoal);
    } catch (err) {
      next(err);
      console.log('error: ' + err.message);
    }
};


//Get income for specific user (get)
const getIncome_ByUserId = async(req, res, next) => {
    const {id} = req.params;

    try{
        const userIncome = await Income.findAll(
          {
            where: {
              user_id: id,
            }
          });
        if (userIncome.length > 0) {
            console.log('data found!');
            res.status(200).json(userIncome);
        } else {
            console.log('No data found!');
            data = [{
                "monthly_income": 0,
                "all_savings": 0,
                "current_balance": 0,
                "expenses": 0,
                "user_id": id
            }]
            res.status(200).json(data);
        }
            

    }catch  (err) {
        next(err);
        console.log('error: ' + err.message);
      }
}



module.exports = {
    getIncome_ByUserId,
    createIncome
}