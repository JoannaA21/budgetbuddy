const Goal = require('../models/goal');
const { Sequelize, Op } = require('sequelize');
const Income = require('../models/income');

const timeElapsed = Date.now();
const today = new Date(timeElapsed);

// Create goal (post)
const createGoal = async(req, res, next) => {
    const {user_id, amount_goal, goal_type} = req.body;
    try {

        // created_at = today.toISOString();
        // updated_at = today.toISOString();
        
        // const goal = await Goal.create({
        //     user_id,
        //     amount_goal,
        //     goal_type,
        //     created_at,
        //     updated_at
        // });

        // res.status(201).json(goal);
        const [goal, created] = await Goal.findOrCreate({
          where: { user_id: user_id }, // Conditions to find the record
          defaults: { // Data to be used if no matching record is found
              user_id: user_id,
              amount_goal: amount_goal,
              goal_type: goal_type,
              created_at: today.toISOString(),
              updated_at: today.toISOString()
            }
        });
      
        // If the record was found and updated, log the message
        if (!created) {
            console.log('Existing record updated:', goal);
            // Update the value of all fields in the Goal object
            goal.user_id = user_id
            goal.amount_goal = amount_goal
            goal.goal_type = goal_type
            goal.updated_at = today.toISOString()
  
            // Save the changes to the database
            await goal.save();
        } else {
            console.log('New record created:', goal);
        }  

        const [incomeGoal, incomeCreated] = await Income.findOrCreate({
            where: { user_id: user_id }, // Conditions to find the record
            defaults: { // Data to be used if no matching record is found
                user_id: user_id,
                monthly_income: 0,
                all_savings: amount_goal,
                current_balance: 0,
                expenses: 0,
                created_at: today.toISOString(),
                updated_at: today.toISOString()
            }
        });
        
        // If the record was found and updated, log the message
        if (!incomeCreated) {
            console.log('Existing record updated:', incomeGoal);
            // Update the value of the current_balance field in the incomeGoal object
            console.log(incomeGoal.monthly_income);
            console.log(incomeGoal.all_savings);
            // incomeGoal.monthly_income = parseFloat(monthly_income);
            incomeGoal.all_savings = amount_goal;
            // incomeGoal.current_balance = parseFloat(incomeGoal.monthly_income -  incomeGoal.expenses) -  parseFloat(amount_goal);
            incomeGoal.current_balance = parseFloat(incomeGoal.monthly_income -  incomeGoal.expenses);
  
            // Save the changes to the database
            await incomeGoal.save();
        } else {
            console.log('New record created:', incomeGoal);
        }      

        res.status(201).json(goal);
    } catch (err) {
      next(err);
      console.log('error: ' + err.message);
    }
};


//Get goal for specific user (get)
const getGoal_ByUserId = async(req, res, next) => {
    const {id} = req.params;

    try{
        // const userGoal = await Goal.findAll(
        //   {
        //     where: {
        //       user_id: id,
        //     }
        //   });
        // // console.log(userGoal);
        // if (userGoal.length > 0) {
        //     console.log('data found!');
        //     res.status(200).json(userGoal);
        // } else {
        //     console.log('No data found!');
        //     data = [{
        //         "amount_goal": "0",
        //         "goal_type": "",
        //         "user_id": id
        //     }]
        //     res.status(200).json(data);
        // }

      const userGoal = await Goal.findOne({
        where: {
          [Op.or]: [
            { user_id: id }
          ]
        }
      });
      if (!userGoal) {
        // return res.status(404).json({ message: 'Goal not found' });
        console.log('No data found!');
        data = {
            "amount_goal": "0",
            "goal_type": "",
            "user_id": id
        }
        res.status(200).json(data);
      }
      res.status(200).json(userGoal);
            

    }catch  (err) {
        next(err);
        console.log('error: ' + err.message);
      }
}

module.exports = {
    getGoal_ByUserId,
    createGoal
}