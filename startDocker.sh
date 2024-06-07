#!/bin/bash

docker build -t stock-data-streamer .

docker run -d --name stock-data-streamer-container stock-data-streamer