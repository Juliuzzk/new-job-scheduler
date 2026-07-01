#!/bin/bash
# ==========================================================
# Start JobFlowScheduler with external jobs-config.json
# ==========================================================

JAR_FILE="target/jobflow-scheduler-0.0.1-SNAPSHOT.jar"
CONFIG_DIR="$(cd "$(dirname "$0")" && pwd)"

if [ ! -f "$JAR_FILE" ]; then
    echo "ERROR: JAR not found. Run './mvnw package' first."
    exit 1
fi

echo "Starting JobFlowScheduler..."
echo "Config dir: $CONFIG_DIR"
echo "Jobs file:  $CONFIG_DIR/jobs-config.json"
echo ""

java -jar "$JAR_FILE" \
    --jobs.config.path="file:$CONFIG_DIR/jobs-config.json"
