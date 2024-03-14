const { DataTypes } = require('sequelize');
const dbConfig = require('../config/dbConfig');

//used to encryot password
const bcrypt = require('bcrypt');

//connect to the database
const sequelize = dbConfig.connect();

const Profile = sequelize.define(
    'Profile',
    {
        id: {
            type: DataTypes.INTEGER,
            primaryKey: true,
            autoIncrement: true
        },
        fname: {
            type: DataTypes.STRING,
            allowNull: false
        },
        lname: {
            type: DataTypes.STRING,
            allowNull: false
        },
        email: {
            type: DataTypes.STRING,
            allowNull: false,
            unique: true,
            // validate: {
            //     isEmail: true, 
            //     isUniqueEmail: async(value) => {
            //         const existingUser = await Profile.findOne({where: {email:value}});
            //         if(existingUser){
            //             throw new Error ('Email already exists in the database.')
            //         }
            //     }
            // }
        }, password: {
            type: DataTypes.STRING,
            allowNull: false
        }
    },
        {
            tableName: 'Profile',
            timestamps: false,
            hooks: {
                beforeCreate: async(user) => {
                    const saltRounds = 10;
                    const hashedPassword = await bcrypt.hash(user.password, saltRounds);
                    user.password = hashedPassword;
                }
            }
        }   
)

Profile.prototype.verifyPassword = async function (password) {
    console.log('Provided Password:', password);
    console.log('Hashed Password from DB:', this.password);
  
    const isPasswordValid = await bcrypt.compare(password, this.password);
    console.log('Is password valid?', isPasswordValid);
  
    return isPasswordValid;
};
  
 
module.exports = Profile;