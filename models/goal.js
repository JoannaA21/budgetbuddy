const { DataTypes } = require('sequelize');
const dbConfig = require('../config/dbConfig');

const sequelize = dbConfig.connect();

const Goal = sequelize.define(
    'Goal',
    {
        id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true
        },
        monthly_income: {
            type: DataTypes.DECIMAL,
            allowNull: false,
            defaultValue: 0
        },
        monthly_saving_goal: {
            type: DataTypes.DECIMAL,
            allowNull: false,
            defaultValue: 0
        },
        expense_type: {
            type: DataTypes.STRING,
            allowNull: false,
        }
    }
)


module.exports = Goal;
