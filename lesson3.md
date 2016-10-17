Recap
-----

In lesson 2 we took the HotelCatalogueService, added two more services, and built an endpoint to return Hotels in JSON given some destination name and radius.

How did everyone get on with that?

Let's go through the bits we didn't finish, and the dependency injection that we blazed through.


Lesson 3 
--------

Now we have a service to get hotel data, let's create a listings page which uses it.

Start by switching to the lesson3 branch: `git fetch --all && git checkout lesson3`

There's a few changes:

- We've moved everything into a 'server' module and created two new modules. 
- The 'client' module holds ScalaJS code which will be compiled to JavaScript, and used on the frontend.
- The 'shared' module holds code which can be shared between the backend and frontend.
- A skeleton for the hotel listings page has been created - the twirl template, controller, and route, and is setup to use our ScalaJS frontend.

We'll build out the template and controller to get a functioning page. Then we'll introduce some JavaScript to bring some interactivity to the page.