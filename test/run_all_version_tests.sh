#!/bin/bash
# Script to run tests with all supported SQLG versions

set -e

echo "===== Testing with SQLG 3.1.1 ====="
lein with-profile +dev,+logging,+updated-deps,+lib-3.1.1 test

echo "===== Testing with SQLG 3.0.4 ====="
lein with-profile +dev,+logging,+updated-deps,+lib-3.0.4 test

echo "===== Testing with SQLG 3.0.1 ====="
lein with-profile +dev,+logging,+updated-deps,+lib-3.0.1 test

echo "===== Testing with SQLG 2.1.6 ====="
lein with-profile +dev,+logging,+updated-deps,+lib-2.1.6 test

echo "All tests completed!"