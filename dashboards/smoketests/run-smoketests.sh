#!/bin/sh

echo "--------------------------------------------------------------------------"
echo "Running Smoketests"
echo "--------------------------------------------------------------------------"
echo ""
echo "=========================================================================="
echo "Ping Service"
echo "=========================================================================="
echo ""
echo "--------------------------------------------------------------------------"
echo "Test Response Header for different content-types, expect HTTP 200"
echo "-------------------------------------"
curl -I http://localhost:8080/dashboards/ -H'Accept:text/plain' 
echo "-------------------------------------"

echo ""
echo "=========================================================================="
echo "Dashboard Service"
echo "=========================================================================="
echo ""
echo "--------------------------------------------------------------------------"
echo "Test scope cities"
echo "--------------------------------------------------------------------------"
curl http://localhost:8080/dashboards/hotels?scope=cities
echo ""
echo "-------------------------------------"
curl -I http://localhost:8080/dashboards/hotels?scope=cities

echo ""
echo "--------------------------------------------------------------------------"
echo "Test scope countries"
echo "--------------------------------------------------------------------------"
curl http://localhost:8080/dashboards/hotels?scope=countries
echo ""
echo "-------------------------------------"
curl -I http://localhost:8080/dashboards/hotels?scope=countries

echo ""
echo "--------------------------------------------------------------------------"
echo "Test scope continentes"
echo "--------------------------------------------------------------------------"
curl http://localhost:8080/dashboards/hotels?scope=continents
echo ""
echo "-------------------------------------"
curl -I http://localhost:8080/dashboards/hotels?scope=continents

echo ""
echo "--------------------------------------------------------------------------"
echo "Test scope cities availabilities"
echo "--------------------------------------------------------------------------"
curl http://localhost:8080/dashboards/availabilities?destinations=982,1,2,4971,7848,3696,6624,4088,1569,31684&scope=cities
echo ""
echo "-------------------------------------"
curl -I http://localhost:8080/dashboards/availabilities?destinations=982,1,2,4971,7848,3696,6624,4088,1569,31684&scope=cities


echo ""
echo "Done"
