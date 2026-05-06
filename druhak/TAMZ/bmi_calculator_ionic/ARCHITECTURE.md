# Architecture & Implementation Details

## Overview

This is a complete Ionic React + TypeScript implementation of a BMI Calculator that mirrors the functionality of the original Expo/React Native version, but uses web-standard APIs instead.

## Key Differences from Original

| Aspect | Original | Ionic Version |
|--------|----------|---------------|
| Framework | React Native / Expo | React / Ionic React |
| Routing | Expo Router | React Router DOM + Ionic tabs |
| Storage | AsyncStorage | IndexedDB + localStorage |
| UI Components | React Native primitives | Ionic Components |
| Styling | NativeWind/Tailwind | CSS + Ionic theme variables |
| Target | Mobile App | Web Browser |
| Build Tool | Expo | Vite |

## Component Architecture

### App.tsx (Root Component)
- Sets up Ionic app structure
- Defines routing with React Router + Ionic tabs
- Implements side menu for About section
- Wraps all pages with necessary providers

### Pages

#### BMIPage.tsx
**Responsibilities**:
- Form rendering with Ionic components
- Validation logic
- BMI calculation coordination
- Result modal display
- Edit mode handling

**State Management**:
- `formData`: Current form values
- `resultData`: Last calculation result
- `errors`: Validation errors
- `isEditing`: Boolean flag for edit mode
- `showResultModal`: Modal visibility

**Key Features**:
- Required field validation with error display
- Default values for height and weight
- Sliders for intuitive input
- Radio buttons for gender selection
- Result modal with bottom sheet behavior
- Automatic navigation back to history after editing

#### HistoryPage.tsx
**Responsibilities**:
- Load and display history from IndexedDB
- Handle edit navigation
- Handle deletion with confirmation
- Show empty state UI

**State Management**:
- `entries`: Array of HistoryEntry objects
- `isLoading`: Loading state

**Key Features**:
- Auto-refresh when page comes into focus (useFocusEffect)
- Swipe-to-reveal edit/delete actions
- Formatted date display
- Category and detail chips
- "Infant" badge for infant formula usage
- Empty state message

### Components

#### ResultModal.tsx
**Props**:
- `isOpen`: Boolean control
- `onClose`: Callback function
- `entry`: HistoryEntry to display

**Features**:
- Bottom sheet modal with Ionic's `IonModal`
- Personalized message based on BMI category
- Large BMI value display
- Detailed breakdown of inputs
- Formula explanation (standard vs infant)
- Breakpoint handling for responsive display

## Storage Architecture

### IndexedDB Implementation

```typescript
// Database: BMICalculatorDB
// Object Store: history
// Key Path: id

Database Structure:
{
  id: "uuid-string",
  username: "John",
  age: "25",
  gender: "male",
  heightCm: 180,
  weightKg: 75,
  bmi: 23.1,
  isInfantFormula: false,
  category: "Normal weight",
  createdAt: "2024-05-06T10:30:00Z"
}
```

### Storage API Pattern

All storage operations return Promises to support async patterns:

```typescript
// Async operations for potential future backend integration
getHistory()      → Promise<HistoryEntry[]>
saveEntry(entry)  → Promise<void>
deleteEntry(id)   → Promise<void>
getEntry(id)      → Promise<HistoryEntry | undefined>
```

### Cache Strategy

- **Active Storage**: IndexedDB (reliable, large quota)
- **Cache Layer**: localStorage (quick reads)
- **Sync**: localStorage updated after each IndexedDB operation
- **Fallback**: If IndexedDB unavailable, read from localStorage cache

## BMI Calculation Logic

### Formula Selection

```typescript
if (age < 2) {
  // Infant Formula
  BMI = weight(kg) / (height(m) ** 3)
  Categories: < 23, 23-25, > 25
} else {
  // Standard Formula
  BMI = weight(kg) / (height(m) ** 2)
  Categories: < 18.5, 18.5-25, 25-30, >= 30
}
```

### Category Mapping

**Infant (age < 2)**:
- "Underweight" if BMI < 23
- "Normal weight" if 23 ≤ BMI ≤ 25
- "Overweight" if BMI > 25

**Standard (age ≥ 2)**:
- "Underweight" if BMI < 18.5
- "Normal weight" if 18.5 ≤ BMI < 25
- "Overweight" if 25 ≤ BMI < 30
- "Obesity" if BMI ≥ 30

## Validation Rules

### Required Fields
- Username: Non-empty string
- Age: Valid positive number (> 0)

### Optional Validation
- Gender: Defaults to "female" (always valid)
- Height: Range 50-250 (enforced by slider)
- Weight: Range 2-250 (enforced by slider)

### Error Display
- Red error text appears below invalid inputs
- No validation for radio buttons (always valid) or sliders (always within range)
- Errors cleared when user fixes input

## Editing Workflow

1. User clicks "Edit" on history item
2. Router navigates to `/bmi/:editId`
3. BMIPage loads entry via `getEntry(editId)`
4. Form prepopulated with entry values
5. Edit mode indicator shown ("Editing an existing record")
6. Button text changes to "Update"
7. Submit creates new entry with **same ID**
8. IndexedDB `put()` operation replaces old entry
9. Result modal shown
10. On close: navigate back to History tab
11. History page fetches fresh data

## Routing Structure

```
/ (redirect)
├── /bmi (BMI calculation form)
│   └── /bmi/:editId (edit existing entry)
└── /history (history list)

Tabs:
- BMI Tab (calculator icon)
- History Tab (history icon)

Menu:
- About (side menu)
```

## State Management Strategy

### Global State
- None needed (Ionic + React Router handles routing)

### Page State
- Each page manages its own state with `useState`
- No context/Redux needed for this simple app

### Shared Logic
- Utility functions in `bmi-utils.ts` (pure functions)
- Storage abstraction in `storage.ts` (async API)

### Patterns Used
- `useEffect` for side effects (unused in current implementation)
- `useIonRouter` for programmatic navigation
- `useParams` for route parameter access
- `useFocusEffect` for tab refocus events

## Styling Approach

### Architecture
- Ionic CSS framework provides base components
- Custom CSS modules per page
- Global CSS for theme variables
- Some inline styles for dynamic values

### Theme System
```css
:root {
  --ion-color-primary: #0071e3 (Blue)
  --ion-color-danger: #eb445c (Red)
  --ion-color-light: #f4f4f4 (Light Gray)
  --ion-color-medium: #727272 (Medium Gray)
  -- ... etc
}
```

### Responsive Design
- Ionic components handle breakpoints automatically
- Mobile-first approach
- Flexbox layout system
- Modal bottom sheet for results

## Performance Considerations

### Optimizations
1. **Lazy Loading**: Pages loaded only when needed
2. **Memoized Storage**: localStorage cache reduces IndexedDB reads
3. **Efficient Sorting**: History sorted on retrieval (O(n log n))
4. **Minimal Re-renders**: Form only updates on input change
5. **Debounced:** No debouncing needed (user input validated on submit)

### Bundle Size
- Vite tree-shaking removes unused code
- Ionic distribution includes ~1.2MB gzipped
- React 19 + React Router minimal core
- Production build: ~400-500KB gzipped

## Security Considerations

### Data Safety
- All data stored client-side (no transmission)
- No authentication/authorization (single-user app)
- IndexedDB supports origin isolation
- localStorage cleared per domain

### Input Sanitization
- No HTML rendering of user input
- No eval or dynamic code execution
- Text content safely rendered in React

### HTTPS
- Recommended for production deployment
- localStorage/sessionStorage restricted to HTTPS
- IndexedDB works on both HTTP and HTTPS

## Browser Compatibility

### Required APIs
- IndexedDB: All modern browsers
- localStorage: All modern browsers
- ES2020+: Modern browsers (Vite polyfills as needed)
- Web Components: For Ionic components

### Tested On
- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+

### Fallbacks
- localStorage cache if IndexedDB unavailable
- Error handling for storage operations
- Graceful degradation

## Development Workflow

### Adding a Feature

1. **Type Definition**:
   ```typescript
   // Update types.ts
   export type NewFeature = { ... }
   ```

2. **Storage Operation**:
   ```typescript
   // Add to storage.ts
   export async function newStorageMethod() { ... }
   ```

3. **Utility Function**:
   ```typescript
   // Add to bmi-utils.ts
   export function newUtility() { ... }
   ```

4. **Component**:
   ```typescript
   // Create component, use utilities and storage
   import { newStorageMethod } from "../lib/storage"
   import { newUtility } from "../lib/bmi-utils"
   ```

5. **Styling**:
   ```css
   /* Add to component .css file */
   .new-element { ... }
   ```

6. **Routing** (if new page):
   ```typescript
   // Update App.tsx
   <Route path="/newpage">
     <NewPage />
   </Route>
   ```

## Testing Strategy

### Unit Tests (Future)
- BMI calculation logic: `bmi-utils.test.ts`
- Storage layer: `storage.test.ts`
- Type safety: TypeScript compiler

### Integration Tests (Future)
- Form submission flow
- History crud operations
- Navigation between pages

### Manual Testing
- Test each field validation
- Test edit workflow
- Test delete workflow
- Test on mobile device/responsive

## Deployment

### Build
```bash
npm run build
# Output: dist/ directory
```

### Hosting Options

1. **Netlify**:
   - Connect GitHub repo
   - Build: `npm run build`
   - Publish: `dist`

2. **Vercel**:
   - Import project
   - Auto-detected framework (Vite)
   - Deploy on push

3. **Docker**:
   ```dockerfile
   FROM node:18
   WORKDIR /app
   COPY . .
   RUN npm install && npm run build
   FROM nginx
   COPY --from=0 /app/dist /usr/share/nginx/html
   ```

4. **Static Host**:
   - Upload `dist/` to any static hosting
   - Configure for SPA (redirect 404 to index.html)

## Future Enhancements

Potential features to add:
1. Export history as CSV
2. Charts/visualizations of BMI trends
3. Multi-user support
4. Dark mode
5. PWA offline support
6. Cloud sync (backend storage)
7. Reminders/scheduling
8. Photo upload for profile
9. Social sharing
10. BMI goals/targets

