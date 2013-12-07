CSC191
======

CSC 191 @ CSU, Sacramento is the 2nd part of a "Senior Project" class.

The goal of this class is to actively develop a software using industry standards.

> That includes but not limited to:

     Documentation
     Testing
     Development
     
     
Our Sponser, the business for whom we are developing this system is Dragonfly Hair and Nail Salon.

They have asked us to develop a scheduling system that will enable their cliental to schedule appointments. In turn, this enables their staff to accept and schedule customers as well.


Salon Scheduler
================

##What is it?

The Salon Scheduler is an online scheduling system that gives clients the ability to schedule appointments online without calling into a salon. The system utilizes a REST API backend written in SpringMVC with MongoDB, and a HTML/CSS3/JavaScript frontend written in AngularJS


##Installation

###Backend
The backend requires an application server such as Tomcat or JBoss as well as MongoDB to run. Once these have been installed, using Maven, simply run `mvn package`. This will generate a `.war` file which you can deploy to your application server.

###Frontend
The frontend only requires a webserver to run. There are no special requirements so any web server will work because it will only be serveing static files. Simply copy the frontend folder to the root document folder of your webserver and you are ready to go.


