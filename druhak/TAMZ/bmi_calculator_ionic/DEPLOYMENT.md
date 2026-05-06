# Migration & Deployment Guide

## Moving the Ionic Version Outside This Project

### Option 1: Git Submodule (If Using Git)

```bash
# From parent directory of bmi_calculator_ionic
git submodule add ./bmi_calculator_ionic <target-path>/bmi-calculator-web

# Later: Clone project with submodule
git clone --recurse-submodules <repo-url>
```

### Option 2: Simple Copy

```bash
# Copy the folder
cp -r bmi_calculator_ionic ~/Projects/bmi-calculator-web

# Or use Git to extract just this folder
cd bmi_calculator_ionic
git init
git add .
git commit -m "Initial commit"

# Then move standalone
mv .git ../bmi-calculator-web/.git
```

### Option 3: Export as Zip

```bash
# Create a standalone archive
zip -r bmi-calculator-web.zip bmi_calculator_ionic/

# Extract in new location
unzip bmi_calculator_web.zip
cd bmi_calculator_ionic
npm install
```

## Modifying for Standalone Use

Once moved out, customize:

### 1. Update package.json

```json
{
  "name": "bmi-calculator-web",
  "description": "A web-based BMI calculator using Ionic React",
  "author": "Your Name",
  "license": "MIT",
  "repository": {
    "type": "git",
    "url": "https://github.com/youruser/bmi-calculator-web.git"
  }
}
```

### 2. Update README.md

```markdown
# BMI Calculator - Ionic React Edition

A cross-platform web BMI calculator built with Ionic React.

[Add your specific modifications here]
```

### 3. Add LICENSE

```bash
# Create LICENSE file (MIT example)
echo "MIT License - (c) 2026 Your Name" > LICENSE
```

## Deployment Scenarios

### Scenario 1: Deploy to Netlify

```bash
# Prerequisites
npm run build

# Option A: Direct folder access
# 1. Drag and drop dist/ folder to Netlify
# 2. Or connect GitHub repo

# Option B: Netlify CLI
npm install -g netlify-cli
netlify deploy --prod --dir=dist
```

**netlify.toml** (optional):
```toml
[build]
  command = "npm run build"
  publish = "dist"

[[redirects]]
  from = "/*"
  to = "/index.html"
  status = 200
```

### Scenario 2: Deploy to Vercel

```bash
# Option A: Direct push to GitHub + connect Vercel
git push origin main
# Then connect repo to Vercel dashboard

# Option B: Vercel CLI
npm install -g vercel
vercel --prod
```

**vercel.json** (optional):
```json
{
  "buildCommand": "npm run build",
  "outputDirectory": "dist",
  "rewrites": [
    { "source": "/(.*)", "destination": "/index.html" }
  ]
}
```

### Scenario 3: Deploy to GitHub Pages

```bash
# Install gh-pages
npm install --save-dev gh-pages

# Add to package.json scripts:
# "deploy": "npm run build && gh-pages -d dist"

# Then run
npm run deploy
```

**vite.config.ts** update:
```typescript
export default defineConfig({
  base: '/bmi-calculator-web/', // if serving from subdirectory
  // ... rest of config
})
```

### Scenario 4: Self-Hosted (Apache/Nginx)

**Apache .htaccess** (for dist/ folder):
```apache
<IfModule mod_rewrite.c>
  RewriteEngine On
  RewriteBase /
  RewriteRule ^index\.html$ - [L]
  RewriteCond %{REQUEST_FILENAME} !-f
  RewriteCond %{REQUEST_FILENAME} !-d
  RewriteRule . /index.html [L]
</IfModule>
```

**Nginx config** (for serving dist/):
```nginx
server {
    listen 80;
    server_name yourdomain.com;
    
    root /var/www/bmi-calculator/dist;
    
    # Serve index.html for SPA routing
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    # Cache static assets
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

### Scenario 5: Docker Deployment

**Dockerfile**:
```dockerfile
# Build stage
FROM node:18 AS builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Runtime stage
FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

**nginx.conf**:
```nginx
server {
    listen 80;
    location / {
        root /usr/share/nginx/html;
        try_files $uri $uri/ /index.html;
    }
}
```

**Build & Run**:
```bash
docker build -t bmi-calculator .
docker run -p 80:80 bmi-calculator
```

### Scenario 6: Docker Compose (with Backend - Future)

```yaml
version: '3.8'
services:
  web:
    build: .
    ports:
      - "80:80"
    depends_on:
      - api
    environment:
      REACT_APP_API_URL: http://api:3000

  api:
    image: nodejs:18
    ports:
      - "3000:3000"
    # Configure backend API here
```

## Environment Variables (Future)

### Create .env.local (development)
```
VITE_API_BASE_URL=http://localhost:3000
VITE_APP_NAME=BMI Calculator
VITE_APP_VERSION=1.0.0
```

### Create .env.production
```
VITE_API_BASE_URL=https://api.yourdomain.com
VITE_APP_NAME=BMI Calculator
VITE_APP_VERSION=1.0.0
```

### Update vite.config.ts
```typescript
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  define: {
    __API_URL__: JSON.stringify(process.env.VITE_API_BASE_URL),
  },
})
```

## Performance Optimization

### Minification & Gzip
```bash
# Build creates minified output
npm run build

# Check bundle size
npm install -g vite
vite preview --outDir dist
```

### Enable Compression

**Nginx**:
```nginx
gzip on;
gzip_vary on;
gzip_types text/plain text/css text/xml text/javascript 
           application/x-javascript application/xml+rss 
           application/javascript application/json;
```

### Caching Strategy

Add hash to builds:
```typescript
// vite.config.ts
export default defineConfig({
  build: {
    rollupOptions: {
      output: {
        entryFileNames: 'js/[name]-[hash].js',
        chunkFileNames: 'js/[name]-[hash].js',
        assetFileNames: 'assets/[name]-[hash][extname]'
      }
    }
  }
})
```

## Monitoring & Analytics (Optional)

### Add Google Analytics
```typescript
// main.tsx
import ReactGA from 'react-ga4'

ReactGA.initialize('G-XXXXXXXXXX')
ReactGA.send({ hitType: 'pageview', page: '/' })
```

### Add Error Tracking (Sentry)
```typescript
import * as Sentry from "@sentry/react"

Sentry.init({
  dsn: "https://...",
  environment: import.meta.env.MODE,
})
```

## CI/CD Pipeline Examples

### GitHub Actions

**.github/workflows/deploy.yml**:
```yaml
name: Deploy

on:
  push:
    branches: [main]

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - uses: actions/setup-node@v3
        with:
          node-version: '18'
      
      - run: npm install
      - run: npm run build
      - run: npm run lint
      
      - uses: peaceiris/actions-gh-pages@v3
        if: github.ref == 'refs/heads/main'
        with:
          github_token: ${{ secrets.GITHUB_TOKEN }}
          publish_dir: ./dist
```

### GitLab CI

**.gitlab-ci.yml**:
```yaml
stages:
  - build
  - deploy

build:
  stage: build
  image: node:18
  script:
    - npm install
    - npm run build
    - npm run lint
  artifacts:
    paths:
      - dist/

pages:
  stage: deploy
  dependencies:
    - build
  script:
    - mv dist public
  artifacts:
    paths:
      - public
  only:
    - main
```

## SSL/HTTPS Setup

### Let's Encrypt (Free)

```bash
# Using Certbot
sudo apt-get install certbot certbot-nginx
sudo certbot certonly --nginx -d yourdomain.com

# Nginx auto-renewal
sudo systemctl enable certbot.timer
sudo systemctl start certbot.timer
```

### Nginx with HTTPS

```nginx
server {
    listen 443 ssl http2;
    server_name yourdomain.com;
    
    ssl_certificate /etc/letsencrypt/live/yourdomain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/yourdomain.com/privkey.pem;
    
    # Security headers
    add_header Strict-Transport-Security "max-age=63072000; includeSubDomains" always;
    add_header X-Content-Type-Options "nosniff" always;
    add_header X-Frame-Options "DENY" always;
    
    root /var/www/bmi-calculator/dist;
    
    location / {
        try_files $uri $uri/ /index.html;
    }
}

# Redirect HTTP to HTTPS
server {
    listen 80;
    server_name yourdomain.com;
    return 301 https://$server_name$request_uri;
}
```

## Backup & Recovery

### Database Backup

Since using IndexedDB in browser, user data is local:

```javascript
// Export function to add to app
export async function backupHistory() {
  const history = await getHistory()
  const json = JSON.stringify(history, null, 2)
  const blob = new Blob([json], { type: 'application/json' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `bmi-history-${Date.now()}.json`
  a.click()
}
```

### Restore from Backup

```javascript
export async function restoreHistory(file: File) {
  const text = await file.text()
  const entries = JSON.parse(text) as HistoryEntry[]
  for (const entry of entries) {
    await saveEntry(entry)
  }
}
```

## Version Management

### Semantic Versioning

```json
{
  "version": "1.0.0"
}
```

Format: MAJOR.MINOR.PATCH
- MAJOR: Breaking changes
- MINOR: New features (backward compatible)
- PATCH: Bug fixes

### Changelog

**CHANGELOG.md**:
```markdown
# Changelog

## [1.0.0] - 2026-05-06

### Added
- Initial release
- BMI calculation for adults and infants
- History tracking with edit/delete
- IndexedDB storage

### Fixed
- Form validation messages
```

## Rollback Procedures

If deployment goes wrong:

```bash
# Keep dist/ versions tagged
git tag v1.0.0
git tag -l

# Revert to previous build
git checkout v1.0.0^
npm run build
# Redeploy

# Or keep dist/ in separate branch
git checkout -b dist/v1.0.0
git add dist/
git commit -m "Build: v1.0.0"
```

