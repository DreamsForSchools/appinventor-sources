var express = require('express');
var router = express.Router();

/* GET home page. */
console.log("Server running...");
router.get('/healthcheck', function(req, res, next) {
    const healthcheck = {
        uptime: process.uptime(),
        message: 'OK',
        timestamp: Date.now()
    };
    try {
        res.send(healthcheck);
    } catch (error) {
        healthcheck.message = error;
        res.status(503).send();
    }


    // var UID = global.userCount;
    // global.userCount += 1;
    // req.session.user = UID;
    // res.render('index', { title: 'Blockly-Demo', uid:UID });
});

module.exports = router;
