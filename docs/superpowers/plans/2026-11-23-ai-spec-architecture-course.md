# AI Spec Architecture Course Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add a detailed Vietnamese beginner course about AI-assisted spec-driven software architecture and add account-based learning progress.

**Architecture:** Keep the established single-page curriculum rendering. Add eight uniquely named curriculum modules and one course metadata entry, with lesson-specific Vietnamese theory/examples/exercises/interview questions in the current topic map. Add Spring Security email/password authentication using BCrypt and server sessions, then scope progress APIs to the authenticated user and synchronize browser progress on login.

**Tech Stack:** Vanilla JavaScript, HTML/CSS, Vite static build, Spring Boot, Spring Security, Spring Session JDBC, PostgreSQL.

**Spec:** `docs/superpowers/specs/2026-11-23-vibe-architecture-course-design.md`

## Global Constraints

- Keep Backend Java/Spring Boot at 74 lessons and current lesson identifiers.
- Keep Claude and AI Engineering at 42 lessons and current lesson identifiers.
- New course lessons use a distinct ID prefix and must not share IDs with existing courses.
- Course explanations and exercises are original Vietnamese paraphrases; do not reproduce book chapters verbatim.
- Preserve browser-local completion tracking and the existing home/course navigation behavior.
- Require a Gmail address and separate password; do not use Google OAuth.
- Hash passwords with BCrypt and keep auth state in a server-managed session cookie with CSRF protection; never put passwords or session tokens in localStorage.
- Derive progress ownership from the authenticated server session, never an arbitrary learnerId supplied by the browser.
- Merge existing browser-local progress into the account only when that account has no saved progress.
- Leave email verification and password reset out of this change because no mail delivery provider is configured.

## Review Focus

- Old completed lessons must not affect new course progress: use unique lesson IDs and verify course filtering.
- Additions must not be included in Backend or Claude totals: verify existing totals stay 74 and 42.
- New lesson topics must not fall back to generic boilerplate: provide topic content for every added lesson and compare ID sets.
- The root app and frontend/public app copies must remain in sync with frontend/app.js.
- Unauthenticated requests cannot read or write account progress; account A cannot access account B's progress.
- Registration rejects duplicate Gmail accounts and never stores a plaintext password.
- Ignore generated `frontend/dist` churn and existing untracked `tmp/` artifacts; do not stage or overwrite them.

---

### Task 1: Add the new course curriculum and lesson content

**Files:**
- Modify: `frontend/app.js`
- Modify: `frontend/public/app.js`
- Modify: `app.js`

**Interfaces:**
- Consumes: existing `curriculum`, `lessonTopics`, and `courses` data structures.
- Produces: eight `spec-*` curriculum modules and matching `spec-*` lessonTopics, 40 lessons total.

- [x] Add eight course modules covering AI-assisted engineering foundations; requirements/specs; context and project rules; modular boundaries; behavioral and architecture tests; spec-to-code workflow; security/contracts/CI; capstone Spring Boot workflow.
- [x] Give all 40 lessons a beginner-friendly title/summary and lesson-specific theory, a concrete example, an exercise, and two self-explanation questions.
- [x] Add unique IDs with a `spec-` prefix; avoid all existing lesson IDs.
- [x] Add metadata for `spec` to `courses`, and route `courseModules()` and home-card counting for that course only.
- [x] Sync the identical source to root `app.js` and `frontend/public/app.js` without changing the existing two course definitions.

### Task 2: Verify course boundaries and production bundle

**Files:**
- Verify: `frontend/app.js`, `frontend/public/app.js`, `app.js`, `frontend/dist/`

- [x] Inspect lesson counts and ID intersections: Backend 74, Claude 42, new course 40, no new IDs shared with existing courses.
- [x] Confirm every new lesson has a specific topic entry rather than default-topic fallback.
- [x] Run the frontend production build and inspect output for build errors.
- [x] Review changes while preserving existing package.json and unrelated dist changes.

### Task 3: Add account authentication and secure progress APIs

**Files:**
- Modify: `backend/pom.xml`, `backend/src/main/java/com/backendlab/LearningController.java`, `backend/src/main/java/com/backendlab/Progress.java`, `backend/src/main/java/com/backendlab/ProgressRepository.java`, `backend/src/main/java/com/backendlab/WebConfig.java`, backend configuration.
- Create: auth entity/repository/service/controller/security configuration and request/response DTOs.

- [x] Add Spring Security and configure secure server sessions/CSRF.
- [x] Add unique Gmail account registration with BCrypt hashing and email/password login/logout/current-user endpoints.
- [x] Replace arbitrary learnerId progress routes with authenticated current-user routes and derive owner from the session.
- [x] Keep existing progress records intact and associate new saved data with the account's server-generated ID.
- [x] Configure exact-origin credentialed CORS for the existing frontend URL and safe local development.

### Task 4: Add Vietnamese login/register UI and progress synchronization

**Files:**
- Modify: `frontend/app.js`, `frontend/styles.css`, `frontend/public/app.js`, `app.js`.

- [x] Add login/register screens with Gmail and separate password fields, validation, pending/error/success states, and navigation.
- [x] Obtain and send CSRF tokens on unsafe requests; include credentials for session cookies.
- [x] On authentication, load server progress. If the account has none, merge browser-local completed lessons once; thereafter use server state as authoritative.
- [x] Add current-account/logout controls and retain progress separation per user across all three courses.
- [x] Show a localized connection error if the backend cannot be reached.

### Task 5: Build and review

- [x] Run frontend production build and backend package compilation without running an unrequested test suite.
- [x] Review auth/session configuration, course totals, progress ownership, and source-copy synchronization.
- [x] Record Render environment/configuration requirements; changes have not been published or deployed.
