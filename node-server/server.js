// import required modules

const express = require('express');
// const path = require('path');
const app = express();
const port =3000;

// Serve the static files
app.use(express.static('D:\\Z\\my-projects\\JAVA-LEARN\\ATMSimulator\\src\\main\\resources\\static'));

app.listen(port, () => {
    console.log(`Server running at http://localhost:${port}`);
    console.log(`Visit http://localhost:${port}/index.html`);
});
