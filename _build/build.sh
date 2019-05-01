#!/bin/bash

gulp && lein ring uberwar || exit 1
warfile=`ls -1 -t target/uberjar/*.war | head -1`
cp $warfile _build/map_points_display.war
cd _build
docker build -t map_points_display .
