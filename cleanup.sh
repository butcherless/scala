#!/bin/bash

find . -type d -name target -o -name .bloop -o -name .bsp -o -name .idea -o -name .vscode -o -name .metals | xargs rm -rf
