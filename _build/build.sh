#!/bin/bash

gulp && lein uberjar || exit 1
uberjar=`ls -1 -t target/*.jar | head -1`
cp $uberjar _build/map_points_display.jar
cd _build
docker build -t map_points_display .
