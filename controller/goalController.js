const Goal = require('../models/goal');
const { Sequelize } = require('sequelize');

const timeElapsed = Date.now();
const today = new Date(timeElapsed);

// Create goal (post)
const createGoal = async(req, res, next) => {
    const {user_id, amount_goal, goal_type} = req.body;

    try {

        created_at = today.toISOString();
        updated_at = today.toISOString();
        
        const goal = await Goal.create({
            user_id,
            amount_goal,
            goal_type,
            created_at,
            updated_at
        });

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
        const userGoal = await Goal.findAll(
          {
            where: {
              user_id: id,
            }
          });
        // console.log(userGoal);
        if (userGoal.length > 0) {
            console.log('data found!');
            res.status(200).json(userGoal);
        } else {
            console.log('No data found!');
            data = [{
                "amount_goal": "0",
                "goal_type": "",
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
    getGoal_ByUserId,
    createGoal
}