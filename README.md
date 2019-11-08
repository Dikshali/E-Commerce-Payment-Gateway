# E-Commerce Payment Gateway

[![license](https://img.shields.io/github/license/Dikshali/E-Commerce-Payment-Gateway?style=flat-square)](https://github.com/Dikshali/E-Commerce-Payment-Gateway/blob/master/LICENSE)
[![GitHub issues](https://img.shields.io/github/issues/Dikshali/E-Commerce-Payment-Gateway?style=flat-square)](https://github.com/Dikshali/E-Commerce-Payment-Gateway/issues)

Its a shopping cart application which lets uer perform operation like signin, signup, view profile, edit profile, select the items they would like to purchase, and be able to pay for their order. 

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

[Profile Application Wireframe](https://xd.adobe.com/spec/a11753fc-121a-4b63-6f80-9463455ef2d5-d726/)

### Prerequisites

What things you need to install the software and how to install them
* Braintree - Credit Payment System
* Android Studio
* Node.js
* Java

### Installing

There are two folders to this project
1. ProfileApplication - It contains an android project.
2. webapp - Nodejs web application

A step by step series of examples that tell you how to get a development env running

Say what the step will be

```
Clone the repo and open ProfileApplication folder inside android studio.
```

### API Routes
Application is hosted on a heroku app server.
Domain Link: [https://profile-application.herokuapp.com/](https://profile-application.herokuapp.com/)

All the routes takes header as:
Content-Type: application/json

#### Authentication Routes
1. /auth/signin - post
This api takes two parameters.
	1.	username
	2.	password
this api check for the user's authentication and returns the jwt token.

2. /auth/signup - post
This api takes following parameters.
	1. firstName
	2. lastName
	3. email - unique
	4. username - unique
	5. password - more than 7 characters
	6. city
	7. gender
It check for the user. If user already present then it won't sign up with that username. Once all the criteria are met it signup the new user and returns the jwt token

3. /user/profile - get
header:
Authorization: Bearer __Token__
It check for the provided user token and return the user data.

4. /user/edit - post
Api takes following parameters
	1. firstName
	2. lastName
	3. city
	4. gender
It updates the user data for the logged in user. If the request is successful it returns with the success message.

Postman Collection Link: [https://www.getpostman.com/collections/db19f626a527b73a1c43](https://www.getpostman.com/collections/db19f626a527b73a1c43)

### Web Application
It is hosted on Heroku
Running on following dependencies: 
Node: 10
Express: 4.17.1
Mongoose: 4.4

Web application Link: [https://profile-application.herokuapp.com/](https://profile-application.herokuapp.com/)


### DB Schema
Our database is hosted on mlab.
We are using MongoDB for the application.
User Model:
	- userId: auto-generated in numeric values
	- firstName: String
	- lastName: String
	- email: String [requires valid email id format, unique parameter]
	- username: String [unique parameter]
	- password: Stored using hashing.
	- salt: String
	- role: String [default: User]
	- city: String
	- gender: String
	- created: Date [default: Date.now]
	
