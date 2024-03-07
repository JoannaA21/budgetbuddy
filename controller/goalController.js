const Goal = require('../models/goal');
const { Sequelize } = require('sequelize');


// Create goal
const createGoal = async(req, res, next) => {
    const {user_id, monthly_income, monthly_saving_goal, expense_type} = req.body;

    try {

        const goal = await Goal.create({
            user_id,
            monthly_income,
            monthly_saving_goal,
            expense_type
        });

        res.status(201).json(goal);
    } catch (err) {
      next(err);
      console.log('error: ' + err.message);
    }
};


//Get goal for specific user
const getGoal_ByUserId = async(req, res, next) => {
    const {id} = req.params;

    try{
        const userGoal = await Goal.findAll({'user_id': id});
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