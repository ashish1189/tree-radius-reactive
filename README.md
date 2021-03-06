# Tree Radius Interview Assignment

Hi there! Congratulations on making it to the next step!

You are given a scaffold application based on Spring Boot to save your time, but you are free to use any other frameworks if you would like to.

Your task is to implement a specific feature as a REST service that uses some 3rd party API.
A service should make an aggregated search of trees (plants, not binary trees) in the circle.

Input:
  - X and Y of the cartesian center point
  - A search radius in meters

Output:
  - Aggregation by "common name" (please see in the documentation on the same page above which field to refer to)

Example of the expected output:

```json
{
    "red maple": 30,
    "American linden": 1,
    "London planetree": 3
}
```

The service should use the data from the 3rd party API (https://data.cityofnewyork.us/Environment/2015-Street-Tree-Census-Tree-Data/uvpi-gqnh): `https://data.cityofnewyork.us/resource/nwxe-4ae8.json`

If you happen to have any questions, please send an email to your HR contact at Holidu.

Good luck and happy coding!

# Build Steps
* mvn clean install
* java -jar tree-radius-reactive-1.0.0-SNAPSHOT.jar Or Just run the boot application.
* http://localhost:9093/api/v1/all-trees?radius=250&x=1002420.358&y=199244.2531

