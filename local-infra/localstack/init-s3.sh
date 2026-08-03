#!/usr/bin/env bash
set -euo pipefail

awslocal s3 mb s3://food-delivery || true

awslocal s3api put-bucket-cors --bucket food-delivery --cors-configuration '{
  "CORSRules": [
    {
      "AllowedOrigins": ["http://localhost:3000", "http://localhost:5173"],
      "AllowedMethods": ["GET", "PUT", "HEAD"],
      "AllowedHeaders": ["*"],
      "ExposeHeaders": ["ETag"],
      "MaxAgeSeconds": 3000
    }
  ]
}'

echo "S3 bucket food-delivery ready"
