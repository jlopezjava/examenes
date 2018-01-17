#!/bin/sh

echo ""
echo "=========================================================================="
echo "Se compila el projecto"
echo "=========================================================================="
mvn eclipse:eclipse 
echo "-------------------------------------"
echo "Se iniciara el jetty"
mvn jetty:run
echo ""
echo "Done"
