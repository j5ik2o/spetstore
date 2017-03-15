#!/bin/sh

curl -Ss -X POST -H "Content-Type: application/json" http://localhost:9000/cutomers -d @create-customer.json