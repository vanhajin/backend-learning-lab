# Backend Learning Platform Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a personal learning dashboard for Java/Spring Boot backend study, with structured lessons, progress tracking, and interview-answer practice.

**Architecture:** A small React single-page app will provide the dashboard, module/lesson views, quiz prompts, and answer practice. Learning content is local typed data; progress and notes are persisted in `localStorage`, keeping the first version simple and usable without a server. Chat-based teaching remains outside the app and is linked as the companion workflow.

**Tech Stack:** Vite, React, TypeScript, CSS, Vitest, Testing Library, localStorage.

**Spec:** `docs/superpowers/specs/2026-11-13-backend-learning-platform-design.md`

## Global Constraints

- The first version is for one learner and does not require authentication or a backend service.
- Content must cover Java Core, Spring/Spring Boot, REST/HTTP, SQL/JPA, testing, security, caching, message queues, and system design.
- Each lesson must expose theory, code/example material, an exercise, interview questions, and self-assessment.
- Progress must survive page reloads through localStorage.
- The UI must remain usable on desktop and mobile widths.

## Review Focus

- Empty or corrupted localStorage must fall back to valid initial progress; test recovery behavior.
- Completing the same lesson twice must not inflate progress; test idempotency.
- A lesson with no notes or unanswered practice must render without errors; test empty state.
- Long Vietnamese lesson text and code blocks must remain readable on narrow screens; test responsive layout selectors.
- Resetting progress must affect only this app's storage key; test storage isolation.

### Task 1: Create the Vite/React application shell

**Files:**
- Create: `package.json`, `vite.config.ts`, `tsconfig.json`, `index.html`
- Create: `src/main.tsx`, `src/App.tsx`, `src/styles.css`
- Test: `src/App.test.tsx`

**Interfaces:**
- Produces the app entry point and a stable `App` component for later features.

- [ ] **Step 1: Scaffold the Vite React TypeScript app and install only required runtime/test dependencies.**
- [ ] **Step 2: Write a failing smoke test asserting the app renders the title “Backend Learning Lab” and navigation labels.**
- [ ] **Step 3: Implement the shell with responsive layout regions: header, sidebar/navigation, main content, and progress summary placeholder.**
- [ ] **Step 4: Run `npm test -- --run` and `npm run build`; expect the smoke test and production build to pass.**

### Task 2: Add typed learning content and module navigation

**Files:**
- Create: `src/data/curriculum.ts`, `src/types/curriculum.ts`
- Create: `src/components/CurriculumNav.tsx`, `src/components/LessonList.tsx`
- Modify: `src/App.tsx`
- Test: `src/components/CurriculumNav.test.tsx`, `src/components/LessonList.test.tsx`

**Interfaces:**
- `Module { id, title, description, lessons }`
- `Lesson { id, moduleId, title, summary, theory, example, exercise, interviewQuestions }`
- `curriculum: Module[]`

- [ ] **Step 1: Define the curriculum types and write tests for module titles, lesson counts, and the required lesson fields.**
- [ ] **Step 2: Add the initial curriculum with the nine modules from the spec and at least two lessons per module.**
- [ ] **Step 3: Implement navigation that selects a module and lesson without changing the URL.**
- [ ] **Step 4: Render lesson summaries and empty states for modules with no selected lesson.**
- [ ] **Step 5: Run the focused component tests and build; expect all content and navigation assertions to pass.**

### Task 3: Implement progress persistence and dashboard metrics

**Files:**
- Create: `src/lib/progressStorage.ts`, `src/hooks/useProgress.ts`
- Create: `src/components/Dashboard.tsx`, `src/components/ProgressBar.tsx`
- Modify: `src/App.tsx`
- Test: `src/lib/progressStorage.test.ts`, `src/hooks/useProgress.test.tsx`, `src/components/Dashboard.test.tsx`

**Interfaces:**
- `ProgressState { completedLessonIds: string[], selfAssessments: Record<string, number> }`
- `loadProgress(): ProgressState`
- `saveProgress(state: ProgressState): void`
- `useProgress()` exposes `isCompleted(id)`, `completeLesson(id)`, `setAssessment(id, value)`, `resetProgress()`, and aggregate counts.

- [ ] **Step 1: Write tests for initial state, valid reload, malformed JSON recovery, idempotent completion, and storage-key isolation.**
- [ ] **Step 2: Implement the storage adapter with a namespaced key and safe parsing/validation.**
- [ ] **Step 3: Implement the progress hook with persisted updates and derived completion percentage.**
- [ ] **Step 4: Implement the dashboard showing completed lessons, total lessons, module progress, and a reset action.**
- [ ] **Step 5: Run focused tests and verify progress remains after a browser reload using the dev server.**

### Task 4: Build the lesson detail and practice experience

**Files:**
- Create: `src/components/LessonDetail.tsx`, `src/components/InterviewPractice.tsx`, `src/components/SelfAssessment.tsx`
- Modify: `src/App.tsx`, `src/styles.css`
- Test: `src/components/LessonDetail.test.tsx`, `src/components/InterviewPractice.test.tsx`

**Interfaces:**
- `LessonDetail` consumes a `Lesson`, completion state, and progress callbacks.
- `InterviewPractice` accepts `questions: string[]` and keeps draft answers locally until submitted.

- [ ] **Step 1: Write tests asserting theory, example, exercise, interview questions, completion control, and empty-answer states.**
- [ ] **Step 2: Implement the lesson detail layout with readable code blocks and a “Mark complete” action.**
- [ ] **Step 3: Implement practice cards with a textarea, reveal/compare guidance, and self-assessment from 1–5.**
- [ ] **Step 4: Connect completion and assessment callbacks to the progress hook.**
- [ ] **Step 5: Run component tests and inspect the page at desktop and mobile widths.**

### Task 5: Add companion chat guidance and release verification

**Files:**
- Create: `src/components/StudyRoutine.tsx`, `README.md`
- Modify: `src/App.tsx`, `src/styles.css`
- Test: `src/components/StudyRoutine.test.tsx`, `src/App.test.tsx`

**Interfaces:**
- `StudyRoutine` renders the repeatable workflow: learn, code, answer, discuss, review.

- [ ] **Step 1: Write tests for the five-step study routine and links/instructions for bringing an answer into the chat.**
- [ ] **Step 2: Implement the routine panel and concise Vietnamese copy throughout the first version.**
- [ ] **Step 3: Add README setup, test, build, and usage instructions.**
- [ ] **Step 4: Run the complete test suite, production build, and a manual responsive smoke check.**
- [ ] **Step 5: Review the implementation against the spec and record any intentionally deferred backend/auth features.**

