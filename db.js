const sqlite3 = require('sqlite3').verbose();
const db = new sqlite3.Database('budgetbuddy.db');

db.run(`
    CREATE TABLE IF NOT EXISTS Profile (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        fname TEXT NOT NULL,
        lname TEXT NOT NULL,
        email TEXT NOT NULL,
        password TEXT NOT NULL,
        created_at TEXT NOT NULL,
        updated_at TEXT NOT NULL
    )
`, (err) => {
    if(err) {
        console.error('Error creating the profile table:', err.message)
    }else{
        console.log('profile table created (or already exists)');
    }
});

db.run(`
    CREATE TABLE IF NOT EXISTS Goal (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        monthly_income DECIMAL(10,2) NOT NULL DEFAULT 0,
        monthly_saving_goal DECIMAL(10,2) NOT NULL DEFAULT 0,
        goal_type TEXT NOT NULL,
        user_id INTERGER,
        created_at TEXT NOT NULL,
        updated_at TEXT NOT NULL,
        FOREIGN KEY(user_id) REFERENCES Profile(id)
    )
`, (err) => {
    if(err) {
        console.error('Error creating the goal table:', err.message)
    }else{
        console.log('goal table created (or already exists)');
    }
});


db.run(`
CREATE TABLE IF NOT EXISTS Expenses (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    expense_type TEXT NOT NULL,
    cost DECIMAL(10,2) NOT NULL DEFAULT 0,
    user_id INTERGER,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL,
    FOREIGN KEY(user_id) REFERENCES Profile(id)
)` ,(err) => {
    if(err) {
        console.error('Error creating the expense table:', err.message)
    }else{
        console.log('expense table created (or already exists)');
    }
})

