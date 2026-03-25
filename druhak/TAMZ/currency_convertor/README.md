# Currency Converter Application 💱

A React Native/Expo application that converts currencies on-the-fly based on real-time exchange rates from the Czech National Bank (CNB).

## Features ✨

- **Real-time Conversion**: Convert amounts between CZK and 30+ foreign currencies instantly
- **Live Exchange Rates**: Fetches current exchange rates from CNB API
- **Multi-language Support**: Switch between English and Czech language
- **Custom Date Selection**: View historical exchange rates for specific dates
- **Interactive UI**: 
  - Dropdown currency selector with all available currencies
  - Touch-friendly buttons to quick-select currencies
  - Display of country names and currency labels
  - Real-time calculation as you type
- **Responsive Design**: Beautiful card-based layout with smooth interactions
- **Loading States**: Activity indicators while fetching data
- **Error Handling**: User-friendly error messages if API calls fail

## Technology Stack 🛠️

- **React Native 19.1.0** - Cross-platform mobile framework
- **Expo 54.0** - React Native development platform
- **TypeScript** - Type-safe JavaScript
- **React Hooks** - State management (useState, useEffect, useCallback)

## How It Works 🔄

### API Integration
The application uses the CNB (Czech National Bank) JSON API endpoint:
```
http://linedu.vsb.cz/~mor03/TAMZ/cnb_json.php
```

### Supported Parameters:
- `date=YYYY-MM-DD` - Get historical exchange rates for a specific date
- `lang=en|cs` - Response language (English or Czech)
- `callback=?` - JSONP callback (for cross-origin requests)

### Conversion Formula:
```
Result = (Input Amount × Exchange Rate) / Unit
```

Example:
- Amount: 100 CZK
- Selected Currency: EUR
- Rate: 25.50 (from API)
- Unit: 1
- Result: (100 × 25.50) / 1 = 2550 EUR

## Installation & Setup 🚀

### Prerequisites
- Node.js (v20.19.4 or higher recommended)
- npm or yarn

### Steps

1. **Install Dependencies**
   ```bash
   npm install
   ```

2. **Start Development Server**
   ```bash
   npm start
   ```

   Or for specific platforms:
   ```bash
   npm run ios      # iOS Simulator
   npm run android  # Android Emulator
   npm run web      # Web Browser
   ```

3. **In Development Server Menu**:
   - Press `i` for iOS Simulator
   - Press `a` for Android Emulator
   - Press `w` for Web
   - Press `j` to open debugger
   - Press `r` to reload app
   - Press `q` to quit

## Application Structure 📁

```
app/
├── index.tsx          # Main Currency Converter component
├── _layout.tsx        # Root layout with navigation stack
assets/
├── images/            # App icons and splash screens
package.json           # Dependencies configuration
tsconfig.json          # TypeScript configuration
```

## Usage Guide 📖

### Basic Conversion

1. **Select Language**: Tap the "Language" dropdown to switch between English (en) and Czech (cs)

2. **Select Currency**:
   - Tap the "Select Currency" dropdown
   - Choose from the list of available currencies
   - Alternatively, tap any currency in the "All Available Currencies" grid below

3. **Enter Amount**: Type the amount in CZK in the "Amount (CZK)" field

4. **View Result**: The conversion result displays instantly in the "Conversion Result" card

### Historical Rates

1. Toggle "Use Custom Date" switch
2. Enter a date in `YYYY-MM-DD` format
3. The application fetches and displays rates for that date
4. Re-enter your amount to see conversion based on historical rates

### Exchange Rate Information

Each currency card displays:
- **Country Name**: The country that uses the currency
- **Currency Code**: ISO 4217 currency code (e.g., EUR, USD, GBP)
- **Exchange Rate**: How much CZK equals 1 unit of the currency

## Data Format 📊

### API Response Example

```json
{
  "date": "2024-03-25",
  "order": "54",
  "data": [
    {
      "country_label": "Australia",
      "curr_label": "dollar",
      "unit": "1",
      "code": "AUD",
      "rate": "16.028"
    },
    ...
  ],
  "labels": ["Country", "Currency", "Amount", "Code", "Rate"],
  "lang": "en",
  "cached": false
}
```

## User Interface 🎨

### Components
- **Header**: Application title and subtitle
- **Language Selector**: Modal dropdown for language selection
- **Custom Date Toggle**: Switch to enable/disable historical date selection
- **Currency Selector**: Modal dropdown for currency selection with scrollable list
- **Amount Input**: Text input field for CZK amount
- **Result Card**: Displays conversion result with exchange rate information
- **Currency Grid**: Quick-select buttons for all available currencies
- **Info Footer**: Shows current date and total currency count

### Styling
- Clean, modern card-based design
- Color scheme: Blue (#0066CC) for primary actions
- Responsive layout that adapts to different screen sizes
- Shadows and borders for visual hierarchy

## Features Breakdown 🔍

### Real-time Conversion
- As you type the amount, the conversion updates instantly
- Detects valid numeric input using JavaScript's parseFloat
- Handles decimal inputs and displays results with 2 decimal places

### Multi-language Support
The API supports:
- English (`lang=en`)
- Czech (`lang=cs`)
- Defaults to browser/device language if not specified

### Error Handling
- Network errors are caught and displayed to the user
- Invalid date formats are handled gracefully
- Missing data is managed with loading states

### Performance Optimizations
- `useCallback` for fetchCurrencies to prevent unnecessary re-renders
- Proper dependency arrays in useEffect hooks
- Efficient FlatList for currency modal to handle 30+ items

## Supported Currencies 💰

The application dynamically loads all currencies available from the CNB API, typically including:
- AUD, BGN, BRL, CAD, CHF, CNY, CZK, DKK, EUR, GBP, HRK, HUF, IDR, ILS, INR, JPY, KRW, MXN, MYR, NOK, NZD, PHP, PLN, RON, RUB, SEK, SGD, THB, TRY, USD, XAU, and more...

## Development Features 🔧

### Type Safety
- Full TypeScript implementation
- Interfaces for API responses and component props
- Type-safe state management

### Debugging
- Console logging for API calls and errors
- Loading states for network operations
- Visual error messages for troubleshooting

### Code Organization
- Component-based architecture
- Separated concerns (data fetching, calculation, UI)
- Clean and maintainable code structure

## Common Issues & Solutions 🆘

### "Failed to load currencies"
- Check internet connection
- Verify CNB API is accessible
- Check browser/app console for CORS issues

### Empty Currency List
- Ensure API is responding with data
- Check language parameter is valid (en or cs)
- Try refreshing the app

### Incorrect Conversion Results
- Verify the amount is a valid number
- Check that the correct currency is selected
- Ensure the exchange rate has been loaded

## Performance Considerations ⚡

- **API Calls**: Throttled to only fetch when language, date, or useCustomDate changes
- **Calculations**: Instant using JavaScript's parseFloat and multiplication
- **List Rendering**: FlatList component optimized for 30+ currency items
- **Memory**: Clean state management prevents memory leaks

## API Rate Limiting 📡

The CNB API should handle typical usage patterns. For high-frequency conversions in production, consider implementing:
- Client-side caching
- API response caching with timestamps
- Request debouncing for date changes

## Future Enhancements 🌟

Potential improvements:
- Add favorite currencies for quick access
- Implement server-sent events (SSE) for real-time rate updates
- Add currency conversion history
- Support for crypto currencies
- Rate change notifications
- Offline mode with cached rates
- Charts showing rate trends over time

## License 📄

This project was created as part of the TAMZ course at VSB-TUO.

## Links 🔗

- [CNB API Documentation](http://linedu.vsb.cz/~mor03/TAMZ/cnb_json.php)
- [React Native Documentation](https://reactnative.dev)
- [Expo Documentation](https://docs.expo.dev)
- [TypeScript Documentation](https://www.typescriptlang.org)

---

**Happy Currency Converting!** 💱✨
