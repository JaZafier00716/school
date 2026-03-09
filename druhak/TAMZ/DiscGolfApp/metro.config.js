const { getDefaultConfig } = require("expo/metro-config");
const { withNativeWind } = require('nativewind/metro');
const { withStorybook } = require('@storybook/react-native/metro/withStorybook');
 
const config = getDefaultConfig(__dirname)
 
module.exports = withNativeWind(config, { input: './app/globals.css' });

// For basic usage with all defaults, this is all you need
module.exports = withStorybook(config);

// Or customize the options
module.exports = withStorybook(config, {
    // When false, removes Storybook from bundle (useful for production)
    enabled: process.env.EXPO_PUBLIC_STORYBOOK_ENABLED === 'true',

    // Path to your storybook config (default: './.rnstorybook')
    configPath: './.rnstorybook',

    // Optional websockets configuration for syncing between devices
    // websockets: {
    //   port: 7007,
    //   host: 'localhost',
    // },
});