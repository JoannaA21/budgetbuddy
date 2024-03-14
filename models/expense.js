const { DataTypes } = require('sequelize');
const dbConfig = require('../config/dbConfig');

const sequelize = dbConfig.connect();


const Expense = sequelize.define (
    'Expense',
    {
        id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true
        },
        expense_type: {
            type: DataTypes.STRING,
            allowNull: false 
        },
        cost: {
            type: DataTypes.DECIMAL,
            allowNull: false,
            defaultValue: 0
        }
    }
)


module.exports = Expense;