const { DataTypes } = require('sequelize');
const dbConfig = require('../config/dbConfig');

const sequelize = dbConfig.connect();

const Income = sequelize.define(
    'Income',
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
        all_savings: {
            type: DataTypes.DECIMAL,
            allowNull: false,
            defaultValue: 0
        },
        current_balance: {
            type: DataTypes.DECIMAL,
            allowNull: false,
            defaultValue: 0
        },
        expenses: {
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
            tableName: 'Income',
            timestamps: false
        }
)


module.exports = Income;
