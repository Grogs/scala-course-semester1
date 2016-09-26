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
5. Run `services.HotelsTest` from IntelliJ to verify


Implementing the endpoint
----

I have created a 'HotelCatalogueService' for you. We will create a

Geography Service, and a Hotel Finder Service.