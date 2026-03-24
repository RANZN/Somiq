# Somiq chat app — phone login & signup flow

See also: **[Architecture overview](./ARCHITECTURE.md)** (presentation layer, `BaseViewModel`, navigation).

This document describes the **unified** authentication path: one phone entry, one OTP step, then either the main app or profile completion for new users.

## User journey

1. **Phone** (`OnBoarding.Login` → `PhoneEntryScreen`)  
   User enters a phone number and taps Continue.

2. **OTP** (`OnBoarding.Otp`)  
   User enters the verification code (dev: `000000`).

3. **Branch (server decides)**  
   - **Existing user** — API returns `LOGGED_IN` with tokens → **Home** (`HomeGraph`).  
   - **New user** — API returns `SIGNUP_REQUIRED` with a signup JWT → **Complete profile** (`OnBoarding.CompleteProfile`) for **display name** and **username** (`userId`), then Home after `complete-signup`.

There is **no separate “log in vs sign up” phone screen**; new vs returning is determined only after OTP verification.

## API contract

| Step | Endpoint | Notes |
|------|----------|--------|
| Verify OTP | `POST /auth/verify-otp` | Body includes `flow: PHONE_AUTH` (see `OtpFlow` / `OtpFlowDto`). |
| Complete signup | `POST /auth/complete-signup` | `Authorization: Bearer <signupToken>`; body: `name`, `userId` (username), optional `email`, `profilePictureUrl`. |

### Server: `OtpFlow.PHONE_AUTH`

After OTP is valid:

- If a user exists for the normalized phone → issue session tokens (`LOGGED_IN`).
- If not → issue a short-lived signup token (`SIGNUP_REQUIRED`).

Legacy flows `LOGIN` and `SIGNUP` remain on the server for compatibility but the **Somiq client only sends `PHONE_AUTH`**.

## Navigation (Compose)

```
Splash → (unauthenticated) → Login (phone)
         → Otp(phone)
         → HomeGraph | CompleteProfile(signupToken)
```

- Removed: `OnBoarding.SignUp` (duplicate phone route).
- `OnBoarding.Otp` is now `Otp(phone)` only (no `isSignUpFlow` flag).

## Implementation map (code)

| Area | Responsibility |
|------|----------------|
| `SmartCentsServer/.../VerifyOtpUseCase.kt` | `PHONE_AUTH` branch: existing user → login; else → signup token. |
| `feature-auth/.../data/repository/AuthRepositoryImpl.kt` | `verifyOtp` uses `OtpFlowDto.PHONE_AUTH`. |
| `AppNavigation.kt` | Single `PhoneEntryScreenHost` → `Otp` → home or `CompleteProfile`. |
| `Destination.kt` | `OnBoarding` sealed type updated (no `SignUp`; simplified `Otp`). |
| `RememberNavBackStack.kt` | Polymorphic serializers aligned with `OnBoarding` keys. |

## Saved state / upgrades

Removing `OnBoarding.SignUp` and changing `Otp`’s shape can break **restored navigation state** from a previous app version. If needed, users clear app data or reinstall; alternatively a one-time migration could map old keys to `Login` (not implemented here).

## Future improvements

- SMS OTP instead of dev `000000`.
- Optional email / avatar on complete profile (already partially supported by API).
- Analytics funnel: phone → OTP → home vs complete profile.

---

*Last updated: 2026-03-23 — unified `PHONE_AUTH` flow implemented on server and client. Cross-linked from [ARCHITECTURE.md](./ARCHITECTURE.md).*
