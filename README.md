Lesson 1 Recap
--------------

In lesson one we started with a blank Play project and walked through it.  
We looked at how the routes file specifies a method on a controller to handle a request, which then calls a 'Twirl' template to render a page.


Today's Task
------------
Create a RESTful API endpoint to return Hotels within a certain radius of a destination.  
You can see this in action here: https://limitless-lowlands-73789.herokuapp.com/hotels/london/2

Prerequisites
-------------

You will need to checkout this branch:

1. git remote add origin https://github.com/Grogs/scala-course.git
2. git fetch --all
3. git reset --hard origin/lesson2
4. sbt populateCatalogueCache
5. Run `services.HotelsTest` from IntelliJ and verify that your see some Hotels
6. (Optional) Push this to Heroku with `git push -f heroku master`



Implementing the endpoint
----

I have created a 'HotelCatalogueService' for you. Let's familiarise ourselves with that...

Next up, we will need a geography service and a hotel finder service.


The geography service will take a destination name as text, and return the corresponding coordinates.

|destination|lat|long|
|---|---|---|
|london| 51.507351 | -0.127758|
|paris| 48.856614 | 2.352222|
|bath| 51.375801 | -2.359904|
|birmingham| 52.486243 | -1.890401|

The hotel finder service will give you all the hotels within a given radius of some given coordinates

We will use haversine formula for this. Rosetta Code provides us a Scala implementation of this: https://rosettacode.org/wiki/Haversine_formula#Scala

And now we will create a new Controller with an endpoint to return hotels within a certain radius of a destination name.