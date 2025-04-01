#!/bin/bash
# Script to create a test database for SQLG-CLJ tests

# Default values
DB_HOST=${SQLG_TEST_HOST:-localhost}
DB_PORT=${SQLG_TEST_PORT:-5432}
DB_NAME=${SQLG_TEST_DB:-sqlgtest}
DB_USER=${SQLG_TEST_USER:-postgres}
DB_PASS=${SQLG_TEST_PASS:-postgres}

# Check if psql is available
if ! command -v psql &> /dev/null; then
    echo "psql could not be found. Please install PostgreSQL client tools."
    exit 1
fi

echo "Creating test database for SQLG-CLJ tests..."
echo "DB_HOST: $DB_HOST"
echo "DB_PORT: $DB_PORT"
echo "DB_NAME: $DB_NAME"
echo "DB_USER: $DB_USER"

# Create database if not exists
PGPASSWORD=$DB_PASS psql -h $DB_HOST -p $DB_PORT -U $DB_USER -tc "SELECT 1 FROM pg_database WHERE datname = '$DB_NAME'" | grep -q 1 || \
  PGPASSWORD=$DB_PASS psql -h $DB_HOST -p $DB_PORT -U $DB_USER -c "CREATE DATABASE $DB_NAME"

echo "Test database setup complete!"
echo "Run tests with:"
echo "export SQLG_TEST_HOST=$DB_HOST"
echo "export SQLG_TEST_PORT=$DB_PORT"
echo "export SQLG_TEST_DB=$DB_NAME"
echo "export SQLG_TEST_USER=$DB_USER"
echo "export SQLG_TEST_PASS=$DB_PASS"
echo "lein with-profile +dev,+logging,+lib-3.1.1 test"