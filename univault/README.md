# Univault – study resource platform

Team project (2 people), December 2025 – January 2026. **My part: backend, database design and authentication.**

A small web app where students and teachers sign up, upload study material for a course, and search for material by course code.

## Features

- **Accounts with roles.** Sign-up as a student (student ID, university, major) or a teacher (department, office). The role is stored in a `users` document keyed by the Firebase user ID.
- **Upload** a file with a course code and title, with a size check before upload and a progress bar.
- **Browse and search** all resources, newest first, or filter by course code. Download any file.
- **Protected pages.** Dashboard, upload and browse redirect to the login page when nobody is signed in.

## Data design

Files are stored directly in Cloud Firestore (as data URLs), so metadata and content are split into two collections:

| Collection | Holds | Why |
|---|---|---|
| `resources` | course, title, file name, size, uploader, timestamp | Small documents, so listing and searching stays fast |
| `resource_files` | the file contents, same document ID as its metadata | Only fetched when someone clicks Download |

Because a Firestore document is limited to about 1 MB, uploads are capped at 800 KB and checked in the browser before anything is sent.

## Tech

HTML · CSS · JavaScript (ES modules) · Firebase Authentication · Cloud Firestore (Firebase JS SDK 11, loaded from the CDN, no build step)

## How to run

1. Create a Firebase project, add a **Web app**, and enable **Email/Password** sign-in under Authentication. Create a Cloud Firestore database.
2. Copy `config.example.js` to `config.js` and paste in your project's web config.
3. Serve the folder with any static server (ES modules don't load from `file://`), for example:
   ```
   cd univault
   python -m http.server 8000
   ```
4. Open http://localhost:8000.

`config.js` is in `.gitignore`, so your Firebase settings stay out of the repository.

## Files

| File | Purpose |
|---|---|
| `index.html` | Landing page |
| `auth.html`, `auth.js` | Login and role-based sign-up |
| `dashboard.html` | Signed-in home |
| `upload.html`, `upload.js` | Upload with size check and progress |
| `browse.html`, `browse.js` | List, search by course, download |
| `style.css` | Shared styles |
