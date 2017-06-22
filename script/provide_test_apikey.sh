KEY_PROPERTIES=$TRAVIS_BUILD_DIR"/PopularMovies/key.properties"

export KEY_PROPERTIES
echo "Key Properties should exist at $KEY_PROPERTIES"

    if [ ! -f "$KEY_PROPERTIES" ]; then
        echo "Key Properties does not exist"

        echo "Creating Key Properties file..."
        touch $KEY_PROPERTIES

        echo "Writing TEST_API_KEY to key.properties..."
        echo "api_key_movdb=$TEST_API_KEY_ENV_VAR" >> $KEY_PROPERTIES
    fi