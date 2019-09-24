// Invoke 'strict' JavaScript mode
'use strict';

// Load the module dependencies
var users = require('../../app/controllers/users.server.controller'),
	passport = require('passport');

// Define the routes module' method
module.exports = function(app) {
	// Set up the 'signup' routes 

	app.get('/', (req, res) => res.send('Hello World!'))

	app.route('/signup')
	   .post(users.signup);

	// Set up the 'signin' routes 
	app.route('/signin')
	   .post(passport.authenticate('local', {
			successRedirect: '/',
			failureRedirect: '/signin',
			failureFlash: true
	   }));

	// Set up the 'signout' route
	app.get('/signout', users.signout);

	app.get('/api/userIdList' , users.getUserIdList);

	app.post('/api/changeUserRole' , users.requiresLogin , users.changeUserRole);
};