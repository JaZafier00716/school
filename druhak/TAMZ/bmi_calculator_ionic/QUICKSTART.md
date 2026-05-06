# Quick Start Guide

## 1. Initial Setup

```bash
# Navigate to the project
cd bmi_calculator_ionic

# Install dependencies
npm install

# Start development server
npm run dev
```

The app will open automatically at `http://localhost:3000`

## 2. Using the App

### BMI Calculation
1. Enter your username
2. Enter your age (must be > 0)
3. Select your gender (female/male)
4. Use sliders to adjust height (50-250 cm) and weight (2-250 kg)
5. Click "Calculate BMI"
6. Review the result in the modal that slides up

### Viewing History
1. Click the "History" tab
2. View all your previous BMI calculations
3. Swipe right on any entry to see edit/delete options
4. Click edit to modify a calculation
5. Click delete to remove an entry

### About
1. Click the menu button (≡) in the header
2. View app information and details

## 3. Project Commands

```bash
# Development
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview

# Run linter
npm run lint
```

## 4. Development Notes

### Storage
- All data is stored in IndexedDB (persistent browser storage)
- localStorage is used as a cache for fast access
- No server backend required

### Validation
- Username is required
- Age is required and must be a positive number
- All other fields have default values or use sliders (no missing values possible)

### BMI Categories
- **Under 2 years**: Uses infant formula (weight/height³)
- **2+ years**: Uses standard BMI formula (weight/height²)

### Editing & Deleting
- Edit: Click edit on a history item to modify and update
- Delete: Click delete on a history item to remove permanently

## 5. Building for Production

```bash
# Create optimized build
npm run build

# Output in dist/ folder
# Deploy the dist/ folder to any static hosting:
# - Netlify
# - Vercel
# - GitHub Pages
# - Traditional web server
```

## 6. Browser Storage Limits

- **IndexedDB**: Typically 50MB+ (depending on browser)
- **localStorage**: Typically 5-10MB
- This app uses very little storage (< 1MB for 1000+ entries)

## 7. Troubleshooting

### Page not loading
- Clear browser cache (Ctrl+Shift+Delete)
- Clear IndexedDB: Open DevTools → Application → IndexedDB → Right-click database → Delete

### Data not persisting
- Ensure Private/Incognito mode is OFF (browsers restrict storage in private mode)
- Check browser storage quota

### Build errors
- Delete `node_modules` and `dist`, then run `npm install` again
- Ensure you're using Node.js 18+: `node --version`

