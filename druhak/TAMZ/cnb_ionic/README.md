# CNB Ionic Currency Converter

Ionic Angular app that converts CZK to foreign currencies using CNB exchange-rate data.

## Setup

```bash
npm install
```

If you are starting from scratch, install the Ionic CLI first:

```bash
npm install -g @ionic/cli
```

## Generate the Ionic/Angular scaffold

These are the CLI commands that match the structure in this workspace:

```bash
ionic start cnb-ionic blank --type=angular
cd cnb-ionic
ionic generate page pages/home
ionic generate service services/currency
ionic generate component components/language-selector
ionic generate component components/date-selector
ionic generate component components/currency-selector
ionic generate component components/conversion-result
ionic generate component components/currency-grid
ionic generate component components/loading-state
ionic generate component components/error-banner
```

## Run locally

```bash
npm start
```

## Build

```bash
npm run build
```

## Project tree

```text
cnb_ionic/
├── angular.json
├── ionic.config.json
├── karma.conf.js
├── package.json
├── README.md
├── src/
│   ├── app/
│   │   ├── app-routing.module.ts
│   │   ├── app.component.html
│   │   ├── app.component.scss
│   │   ├── app.component.ts
│   │   ├── app.module.ts
│   │   ├── components/
│   │   │   ├── conversion-result/
│   │   │   ├── currency-grid/
│   │   │   ├── currency-selector/
│   │   │   ├── date-selector/
│   │   │   ├── error-banner/
│   │   │   ├── language-selector/
│   │   │   └── loading-state/
│   │   ├── models/
│   │   │   └── currency.model.ts
│   │   ├── pages/
│   │   │   └── home/
│   │   │       ├── home.page.html
│   │   │       ├── home.page.scss
│   │   │       └── home.page.ts
│   │   └── services/
│   │       └── currency.service.ts
│   ├── assets/
│   │   └── .gitkeep
│   ├── global.scss
│   ├── index.html
│   ├── main.ts
│   ├── polyfills.ts
│   ├── test.ts
│   └── theme/
│       └── variables.scss
├── tsconfig.app.json
├── tsconfig.json
└── tsconfig.spec.json
```

## Notes

- The app uses SSE automatically for live rates when custom date mode is disabled.
- When custom date mode is enabled, it uses a normal HTTP request with `date=YYYY-MM-DD`.
- The searchable currency modal and selectable grid are backed by reusable Angular components using `selector` + `templateUrl`.

