#!/bin/bash

# NOT USED, ONLY FOR TESTING PURPOSES.

SPLIT_UNIT_TEST_CLASS_NAMES="feature.upcoming_movies.com.android.dev.engineer.kotlin.compose.feature.upcoming_movies.test.screen.UpcomingMoviesScreenComposableScreenshotTest"
BUILD_VARIANT=debug
UNIT_TEST_COMMAND=test${BUILD_VARIANT}UnitTest

run_unit_tests() {
  COMMAND="$1"
  echo "Running unit tests: $COMMAND"
  ./gradlew $COMMAND
}

group_unit_tests_per_module() {
  MODULES=$(
    echo "$SPLIT_UNIT_TEST_CLASS_NAMES" |
    awk '{
      for (i=1; i<=NF; i++) {
        sub(/\.com\..*/, "", $i)
        print($i)
      }
    }' |
    awk '!visited[$0]++' ORS=" "
  )

  read -r -a MODULES <<< "$MODULES"

  for MODULE in "${MODULES[@]}"; do
    UNIT_TEST_CLASS_NAMES=$(
      echo "$SPLIT_UNIT_TEST_CLASS_NAMES" |
      awk "/^$MODULE\./" |
      awk -F "^$MODULE." '{
        print("--tests", $2)
      }' ORS=" "
    )

    if [ -n "$UNIT_TEST_CLASS_NAMES" ]; then
      run_unit_tests "${MODULE//\./:}:$UNIT_TEST_COMMAND $UNIT_TEST_CLASS_NAMES"
    fi
  done
}

group_unit_tests_per_module

# https://stackoverflow.com/questions/42052154/extract-the-text-of-string-after-a-word-in-bash
# sub(/\./, ":", $i) -> Replace . with :
# awk -F "^$MODULE\\." '{
#  print("--tests", $2)
# }' ORS=" "
# To avoid: awk: warning: escape sequence `\.' treated as plain `.'