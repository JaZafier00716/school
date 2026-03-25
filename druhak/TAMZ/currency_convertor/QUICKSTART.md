# Quick Start Guide - Currency Converter

## 🚀 Getting Started in 5 Minutes

### 1. Install Dependencies
```bash
cd /home/jan/Documents/School/druhak/TAMZ/currency_convertor
npm install
```

### 2. Start the Application
```bash
npm start
```

### 3. Choose Your Platform
When you see the Expo menu, press:
- **`w`** for Web Browser (easiest for testing)
- **`a`** for Android Emulator
- **`i`** for iOS Simulator

## 📱 Using the Application

### Basic Currency Conversion
1. **Type an amount** in the "Amount (CZK)" field
2. **Select a currency** from the "Select Currency" dropdown or tap quick-select buttons
3. **See instant results** in the "Conversion Result" card

### Advanced Features

#### Change Language
1. Tap the "Language" dropdown
2. Choose **English** (en) or **Czech** (cs)
3. The entire app and exchange rates update immediately

#### View Historical Rates
1. Toggle the **"Use Custom Date"** switch
2. Enter a date in format: **YYYY-MM-DD** (e.g., 2024-03-15)
3. Exchange rates for that date load automatically
4. Conversion results update based on historical rates

#### Quick-Select Currencies
- Scroll down to see all available currencies
- Tap any currency button to instantly select it
- See the exchange rate to CZK displayed on each button

## 🎯 Key Features

| Feature | How to Use |
|---------|-----------|
| **Real-time Conversion** | Type amount → See result instantly |
| **Currency Selection** | Dropdown or grid buttons |
| **Language Support** | Tap Language → Select English/Czech |
| **Historical Rates** | Toggle custom date → Enter YYYY-MM-DD |
| **Exchange Rate Info** | View in result card and currency grid |

## 📊 Example Usage

### Scenario 1: Quick USD Conversion
1. Default amount is 1 CZK
2. Scroll down and tap **USD** button
3. Enter **100** in amount field
4. See result: ~2.0 USD (example)

### Scenario 2: Historical EUR Rate
1. Toggle **"Use Custom Date"**
2. Enter date: **2024-01-15**
3. Tap **EUR** from dropdown
4. Enter **1000** CZK
5. See historical conversion result

## 🔧 Troubleshooting

| Problem | Solution |
|---------|----------|
| **App won't start** | Run `npm install` again |
| **"Failed to load currencies"** | Check internet connection |
| **Rates not updating** | Try refreshing app (press `r` in terminal) |
| **Modal won't close** | Tap outside the modal area |

## 📱 Device-Specific Instructions

### Web Browser (Recommended for Testing)
```bash
npm start
# Press 'w' for web
# Opens at http://localhost:19006
```

### iOS Simulator
```bash
npm start
# Press 'i'
# Requires Xcode installed
```

### Android Emulator
```bash
npm start
# Press 'a'
# Requires Android Studio emulator running
```

## 💡 Tips & Tricks

1. **Favorite Currencies**: Tap currency grid buttons to quickly switch currencies
2. **Fast Date Changes**: Use YYYY-MM-DD format for instant updates
3. **Decimal Support**: Enter amounts like 123.45 for precise conversions
4. **Language Switching**: Change language anytime - app reloads rates in that language

## 📄 Full Documentation

For detailed information, see:
- **[README.md](./README.md)** - Complete feature documentation
- **[IMPLEMENTATION.md](./IMPLEMENTATION.md)** - Technical implementation details

## 🆘 Common Questions

**Q: What currencies are supported?**
A: 30+ currencies including USD, EUR, GBP, JPY, CHF, AUD, CAD, and more (loaded dynamically from CNB API)

**Q: Can I see old exchange rates?**
A: Yes! Toggle "Use Custom Date" and enter any date in YYYY-MM-DD format

**Q: What language is the app in?**
A: Both! Use the Language dropdown to switch between English and Czech

**Q: Does it work offline?**
A: No, it needs internet to fetch current exchange rates from the CNB API

**Q: Is my data secure?**
A: Yes, conversions happen locally in your device. Only exchange rate data is fetched from the API.

## ⌚ Development Server Commands

While running `npm start`:

| Key | Action |
|-----|--------|
| `r` | Reload app |
| `j` | Open debugger |
| `m` | Toggle menu |
| `q` | Quit |

## 📞 Need Help?

1. Check the [README.md](./README.md) for detailed documentation
2. Review [IMPLEMENTATION.md](./IMPLEMENTATION.md) for technical details
3. Check the app console for error messages (press `j` for debugger)
4. Verify internet connection for API access

## ✅ You're All Set!

The Currency Converter app is ready to use. Start with `npm start` and enjoy converting currencies! 💱

---

**Version**: 1.0.0  
**Course**: TAMZ (VSB-TUO)  
**Last Updated**: March 2026

