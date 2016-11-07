Recap
-----

In lesson 6 we added a map to our hotel listing page. We usec the Google Maps API; adding markers with infowindows containing the hotel details.

Lesson 7 
--------

We will create a client for an external 'Hotel Pricing Service', and see how to add an integration test for it.  

It has one endpoint, which returns the available rooms (with prices) between two dates for a given hotel. 
An example call for one of our London hotels:
http://<host>/prices/105996?from=2016-11-09&to=2016-11-15