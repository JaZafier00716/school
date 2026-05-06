# BMI Calculator - Ionic React Version

This is a complete Ionic React TypeScript implementation of the BMI Calculator app.

## Project Structure

```
src/
├── lib/
│   ├── types.ts           # TypeScript types and interfaces
│   ├── storage.ts         # IndexedDB + localStorage storage layer
│   └── bmi-utils.ts       # BMI calculation and utility functions
├── pages/
│   ├── BMIPage.tsx        # BMI calculation form page
│   ├── BMIPage.css        # BMI page styling
│   ├── HistoryPage.tsx    # History list page
│   └── HistoryPage.css    # History page styling
├── components/
│   ├── ResultModal.tsx    # Result modal component
│   └── ResultModal.css    # Result modal styling
├── theme/
│   └── variables.css      # Ionic theme variables
├── App.tsx                # Root component with routing
├── main.tsx               # Application entry point
└── index.css              # Global styles
```

## Features

- **BMI Form**: Username, age, gender (radio), height (slider), weight (slider)
- **Validation**: Required fields with error messages
- **BMI Calculation**: Standard BMI and infant-adjusted formula (age < 2)
- **Result Modal**: Bottom sheet with calculated BMI and details
- **History**: List of all BMI calculations with edit/delete swipe actions
- **Persistent Storage**: IndexedDB for BMI history + localStorage cache
- **TypeScript**: Full type safety with interfaces
- **Ionic Components**: IonApp, IonTabs, IonModal, IonRange, IonRadio, etc.
- **About Menu**: Side menu with app information

## Setup Instructions

### Prerequisites
- Node.js 18+ and npm

### Installation

1. **Navigate to project directory**:
   ```bash
   cd bmi_calculator_ionic
   ```

2. **Install dependencies**:
   ```bash
   npm install
   ```

3. **Run development server**:
   ```bash
   npm run dev
   ```
   The app will open at `http://localhost:3000`

4. **Build for production**:
   ```bash
   npm run build
   ```
   Output will be in `dist/` directory

## Technology Stack

- **React 19**: UI framework
- **TypeScript**: Language
- **Ionic React 8**: UI components and tabs
- **React Router DOM 6**: Routing
- **IndexedDB**: Primary persistent storage
- **localStorage/sessionStorage**: Cache and temporary state
- **Vite**: Build tool
- **CSS**: Styling with Ionic theme variables

## Data Model

### HistoryEntry
```typescript
{
  id: string;
  username: string;
  age: string;
  gender: "female" | "male";
  heightCm: number;
  weightKg: number;
  bmi: number;
  isInfantFormula: boolean;      // true if age < 2
  category: string;              // "Underweight", "Normal weight", "Overweight", "Obesity"
  createdAt: string;             // ISO timestamp
}
```

## BMI Calculation

### Standard BMI (age >= 2)
- **Formula**: BMI = weight(kg) / height(m)²
- **Categories**:
  - < 18.5: Underweight
  - 18.5 - 24.9: Normal weight
  - 25.0 - 29.9: Overweight
  - ≥ 30: Obesity

### Infant BMI (age < 2)
- **Formula**: BMI = weight(kg) / height(m)³
- **Categories**:
  - < 23: Underweight
  - 23 - 25: Normal weight
  - > 25: Overweight

## Storage Architecture

### IndexedDB
- **Database**: `BMICalculatorDB`
- **Store**: `history` (keyPath: `id`)
- Stores all BMI history records persistently

### localStorage
- **Key**: `@bmi:history-cache`
- Caches the history for quick access
- Updated whenever IndexedDB is modified

## API

### storage.ts

#### `getHistory(): Promise<HistoryEntry[]>`
Retrieves all history entries sorted by creation date (newest first).

#### `saveEntry(entry: HistoryEntry): Promise<void>`
Saves or updates a history entry in IndexedDB. Updates the localStorage cache.

#### `deleteEntry(id: string): Promise<void>`
Deletes a history entry from IndexedDB by ID. Updates the localStorage cache.

#### `getEntry(id: string): Promise<HistoryEntry | undefined>`
Retrieves a single history entry by ID.

### bmi-utils.ts

#### `calculateBMI(input: BMICalculationInput): BMIResult`
Calculates BMI and returns the value, category, and whether infant formula was used.

#### `createHistoryEntry(...): HistoryEntry`
Creates a new history entry object with generated ID and current timestamp.

#### `getResultMessage(username, bmi, category): string`
Generates a personalized result message for the user.

#### `formatDate(isoString): string`
Formats an ISO timestamp to locale string with date and time.

## Pages

### BMI Page (`/bmi`)
- Form inputs:
  - Username (text, required)
  - Age (number, required, > 0)
  - Gender (radio: female/male)
  - Height (range slider: 50-250 cm)
  - Weight (range slider: 2-250 kg)
- Validation with red error text
- Submit button to calculate BMI
- Shows result in bottom sheet modal
- Edit mode when navigating from history (button shows "Update")

### History Page (`/history`)
- Displays list of all BMI calculations
- Shows empty state if no entries
- Each item displays:
  - Username and BMI value
  - Formatted date and category
  - Detail chips (height, weight, gender, age, infant badge)
- Swipe actions:
  - Edit: Load entry into BMI form
  - Delete: Remove from history

### About Menu
- Accessible from menu button in header
- Shows:
  - App name: BMI Calculator
  - Version: 1.0.0
  - Author: Jan
  - Created: 2026
  - Description

## Design

- **Primary Color**: Blue (#0071e3)
- **Danger Color**: Red (#eb445c)
- **Background**: Light slate (#f1f5f9)
- **Cards/Items**: White background
- **Mobile-first**: Responsive design for all screen sizes

## Deployment

Can be deployed to:
- Netlify: `npm run build` then deploy `dist/` folder
- Vercel: Direct git integration
- Traditional web server: `npm run build` → serve `dist/` folder

## Notes

- No React Native or Expo dependencies
- Uses browser APIs (IndexedDB, localStorage, sessionStorage)
- fully type-safe with TypeScript
- Ionic components handle responsive UI automatically
- CSS uses Ionic theme variables for consistency

