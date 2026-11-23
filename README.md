# Abd Almajed Masood – Projects

Computer Science student (B.Sc., German Jordanian University), currently an exchange student in Informatik at Hochschule Nordhausen, Germany. I am looking for a mandatory internship from March 2027 in software engineering, test automation / QA, or AI agents.

[LinkedIn](https://linkedin.com/in/abdalmajedmasood)

## Projects

| Project | What it is | Tech | Code in this repo |
|---|---|---|---|
| [**nova**](nova/) | Windows desktop agent that carries out natural-language tasks by looking at the screen and driving the real mouse and keyboard. Senior project, solo, grade 97/100. | Python, PySide6 (Qt), Gemini API, Tesseract OCR, PyAutoGUI, mss, Firebase | Write-up and design diagrams |
| [**Univault**](univault/) | Web platform for uploading and finding study material by course code, with student and teacher accounts. Team of 2, I owned backend, database and authentication. | JavaScript, Firebase Authentication, Cloud Firestore | Full source |
| [**PharmaSync**](pharmasync/) | Android app for running a small pharmacy: point of sale, stock and orders, staff, shifts and statistics, with separate admin and employee views. | Kotlin, XML layouts, Android Studio | Full source |

## Highlights

- **Measuring, not demoing.** For nova I ran the same multi-app task ten times, traced the failures in the recorded transcripts to one cause, fixed it, and re-measured: full task completion went from 3 of 10 runs to 5 of 5, and the median number of steps dropped from 21 to 14.
- **Grounding AI output in something checkable.** nova takes click coordinates from OCR rather than trusting the model's guess, and uses screen differencing to notice when an action had no effect.
- **Data design for speed.** Univault keeps file metadata and file contents in separate Firestore collections, so browsing a course list never downloads the files themselves.

## Other coursework

Computer graphics with OpenGL/GLSL in C++ · Java Swing apps with MySQL/JDBC · MIPS assembly · PHP/SQL
