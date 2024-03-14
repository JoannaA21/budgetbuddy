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
        },
        user_id: {
            type: DataTypes.INTEGER,
            allowNull: false
        },
        created_at: {
            type: DataTypes.STRING,
            allowNull: false
        },
        updated_at: {
        type: DataTypes.STRING,
        allowNull: false
        }
    }, 
        {
            tableName: 'Expenses',
            timestamps: false
        }
)


module.exports = Expense;