const routerBuddy = require('./routes/route');
const express = require('express');
const bodyParser = require('body-parser');

const app = express();
app.use(bodyParser.json());
app.use('/api', routerBuddy);
const port = 3000;

app.listen(port, () => {
    console.log(`Server is running on port ${port}`);
  });