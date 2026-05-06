# Quick Reference Card

## 🚀 Getting Started (Copy-Paste)

```bash
# Navigate
cd /home/jan/Documents/School/druhak/TAMZ/bmi_calculator_ionic

# Setup (one time)
npm install

# Run
npm run dev

# Build for production
npm run build
```

## 📂 File Purposes at a Glance

```
src/
├── lib/
│   ├── types.ts          → TypeScript interfaces
│   ├── storage.ts        → IndexedDB + cache layer
│   └── bmi-utils.ts      → Calculation logic
├── pages/
│   ├── BMIPage.tsx       → Form page
│   └── HistoryPage.tsx   → List page
├── components/
│   └── ResultModal.tsx   → Result display
├── App.tsx               → Routing root
└── main.tsx              → Entry point
```

## 📊 BMI Calculation Formulas

**Age < 2 Years (Infant)**
```
BMI = weight(kg) / height(m)³
Categories: <23 (Under), 23-25 (Normal), >25 (Over)
```

**Age ≥ 2 Years (Standard)**
```
BMI = weight(kg) / height(m)²
Categories: <18.5 (Under), 18.5-25 (Normal), 25-30 (Over), ≥30 (Obesity)
```

## 🔄 Data Flow Summary

```
Form Input → Validation → Calculation → Storage (IndexedDB + cache)
      ↓                                              ↓
   Errors Display                          Result Modal Display
                                                    ↓
                                         Optional: History Update
```

## 🛢️ Storage API

```typescript
// Get all entries (sorted newest first)
const entries = await getHistory()

// Save new or update existing
await saveEntry(entry)

// Remove entry
await deleteEntry(id)

// Get single entry
const entry = await getEntry(id)
```

## 📱 UI Components Used

```
IonApp → IonReactRouter → IonTabs
           ├── IonMenu (About)
           └── IonPage
               ├── IonHeader
               ├── IonContent
               ├── IonList
               ├── IonItem/IonItemSliding
               ├── IonRange (sliders)
               ├── IonRadio (gender)
               ├── IonInput (text fields)
               ├── IonButton
               └── IonModal (results)
```

## 🎨 Styling Quick Map

```css
/* Theme colors */
--ion-color-primary: #0071e3 (Blue)
--ion-color-danger: #eb445c (Red)
--ion-color-medium: #727272 (Gray)
--ion-color-light: #f4f4f4 (Light)

/* Per-page styles */
src/pages/BMIPage.css
src/pages/HistoryPage.css
src/components/ResultModal.css
```

## 🔑 Key Routes

```
/          → Redirects to /bmi
/bmi       → Form page
/bmi/:id   → Edit mode (load from history)
/history   → History list
```

## ✅ Validation Rules

```
username: Required (not empty)
age:      Required, > 0, numeric
gender:   Always valid (radio)
height:   50-250 cm (slider enforces)
weight:   2-250 kg (slider enforces)
```

## 📋 Form Defaults

```typescript
{
  username: "",        // Required - no default
  age: "",            // Required - no default
  gender: "female",   // Default
  heightCm: 170,      // Default
  weightKg: 70        // Default
}
```

## 🎬 User Workflows

**Create New Entry**
```
BMI page → Fill form → Validate → Calculate → Show result → Back to form
```

**Edit Entry**
```
History page → Click Edit → Form prefilled → Modify → Update → Show result → Back to history
```

**Delete Entry**
```
History page → Swipe right → Click Delete → Removed from history
```

## 🧪 Testing Checklist

- [ ] Form validation shows errors
- [ ] BMI calculates correctly
- [ ] Result modal displays
- [ ] History saves entry
- [ ] Can edit entry
- [ ] Can delete entry
- [ ] Auto-refresh when returning to history
- [ ] Menu shows about info
- [ ] Sliders work smoothly
- [ ] Radio buttons toggle

## 🐛 Common Issues & Fixes

| Issue | Fix |
|-------|-----|
| Port in use | `npm run dev -- --port 3001` |
| Build fails | `rm -rf node_modules && npm install` |
| Data not saving | Check private/incognito mode |
| Sliders jerky | Check browser performance |
| Form frozen | Check console for errors |

## 📦 Dependencies (Key Ones)

```
react               19.1.0    # UI framework
typescript          5.5+      # Type checking
@ionic/react        8.8.5     # UI components
react-router-dom    5.3.4     # Routing
ionicons            8.0.13    # Icons
vite                5.3.1     # Build tool
```

## 🔧 Common Commands

```bash
npm install               # Install dependencies
npm run dev              # Start dev server
npm run build            # Build for production
npm run preview          # Preview production build
npm run lint             # Check code style
```

## 📝 Code Examples

### Adding a new history entry
```typescript
const entry = createHistoryEntry(formData, calculateBMI(formData))
await saveEntry(entry)
```

### Loading history
```typescript
const all = await getHistory()
setEntries(all)
```

### Navigating
```typescript
ionRouter.push("/bmi")
ionRouter.push(`/bmi/${id}`)
ionRouter.push("/history", "back")
```

## 🎯 Editing Existing Code

### To change BMI formula
File: `src/lib/bmi-utils.ts`
Function: `calculateBMI()`

### To change colors
File: `src/theme/variables.css`
Variable: `--ion-color-primary`, etc.

### To change form fields
File: `src/pages/BMIPage.tsx`
Update: FormData state + JSX

### To change storage
File: `src/lib/storage.ts`
Replace: IndexedDB calls with your backend

## 🌐 Environment Info

- Uses IndexedDB for persistent storage
- localStorage as cache layer
- No external API needed
- Works offline (after initial load)
- ~450KB bundle size (gzipped)

## 📊 Performance Targets

- Initial load: 3-4 seconds
- Tab switch: < 100ms
- Form validation: < 10ms
- BMI calculation: < 1ms
- History load: ~50ms (for 100 entries)

## 🚢 Deployment Paths

```
Development → npm run dev
   ↓
Build → npm run build
   ↓
Test → npm run preview
   ↓
Deploy → Choose option:
   ├── Netlify (zip dist/)
   ├── Vercel (connect GitHub)
   ├── GitHub Pages (gh-pages CLI)
   ├── Docker (Dockerfile provided)
   └── Self-host (Nginx/Apache config in DEPLOYMENT.md)
```

## 📚 Documentation Files

```
README.md           → Full guide (start here)
QUICKSTART.md       → 5-minute setup
SETUP.md            → This summary expanded
ARCHITECTURE.md     → Design decisions
FILE_STRUCTURE.md   → Code map
DEPLOYMENT.md       → How to deploy
```

## 💡 Pro Tips

1. Use `npm run lint` before committing
2. Check DevTools → Application → IndexedDB to verify storage
3. TypeScript catches most errors - fix them before building
4. Use React DevTools extension to debug component state
5. Ionic docs at https://ionicframework.com/docs/react for component props
6. IndexedDB quota is usually 50MB+ per domain

## 🔐 Security Notes

- All data stays client-side (browser)
- No authentication (single-user, local app)
- Input not HTML-rendered (safe from XSS)
- No external API calls (no data sent anywhere)
- Use HTTPS in production

---

**Print this page or save as bookmark for quick reference!**

