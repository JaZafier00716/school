# TAMZ Token App (Expo React Native)

This app implements the required two-step request flow:

1. Send a request with `user` and `timestamp` query args to:
   `https://homel.vsb.cz/~mor03/TAMZ/TAMZ22.php`
2. Read the `text/plain` body (base64 token), decode it with `atob`, and show it in a read-only input.
3. Send a second request to the same URL without `user`, with header:
   `Authorization: Bearer <token>`

## Run

```bash
npm install
npx expo start
```

For web:

```bash
npx expo start --web -c
```

## Main implementation

- Screen: `app/(tabs)/index.tsx`
- Tab config: `app/(tabs)/_layout.tsx`

## Notes

- Default script URL is prefilled and editable.
- A fallback decoder from `base-64` is used when `globalThis.atob` is not available on native runtime.
