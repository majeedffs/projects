import { firebaseConfig } from './config.js';
import { initializeApp } from "https://www.gstatic.com/firebasejs/11.1.0/firebase-app.js";
import { getAuth, onAuthStateChanged, signOut } from "https://www.gstatic.com/firebasejs/11.1.0/firebase-auth.js";
import {
    getFirestore,
    collection,
    getDocs,
    getDoc,
    doc,
    query,
    orderBy,
    where
} from "https://www.gstatic.com/firebasejs/11.1.0/firebase-firestore.js";

const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const db = getFirestore(app);

const TEST_MODE = false;

const msg = document.getElementById("msg");
const list = document.getElementById("list");
const searchCourse = document.getElementById("searchCourse");

function escapeHtml(text) {
    return String(text || "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

function fileSizeText(bytes) {
    if (!bytes && bytes !== 0) return "";
    if (bytes < 1024) return bytes + " B";
    if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + " KB";
    return (bytes / (1024 * 1024)).toFixed(1) + " MB";
}

async function handleDownload(id, fileName, btn) {
    const originalText = btn.innerText;
    btn.innerText = "Downloading...";
    btn.style.opacity = "0.7";

    try {
        const fileSnap = await getDoc(doc(db, "resource_files", id));

        if (fileSnap.exists()) {
            const fileData = fileSnap.data().fileData;

            const link = document.createElement("a");
            link.href = fileData;
            link.download = fileName;
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);

            btn.innerText = "Done!";
        } else {
            alert("File not found in database!");
        }
    } catch (e) {
        console.error(e);
        alert("Download failed: " + e.message);
    }

    setTimeout(() => {
        btn.innerText = originalText;
        btn.style.opacity = "1";
    }, 2000);
}

function renderItem(docSnapshot) {
    const data = docSnapshot.data();
    const id = docSnapshot.id;

    const title = escapeHtml(data.title || data.fileName || "File");
    const course = escapeHtml(data.course || "");
    const uploader = escapeHtml(data.uploadedBy || data.user || "");
    const type = escapeHtml(data.fileType || "");

    const box = document.createElement("div");
    box.className = "card";
    box.style.padding = "16px";
    box.style.borderRadius = "14px";
    box.style.background = "white";
    box.style.border = "1px solid #e5e7eb";

    box.innerHTML = `
    <div style="display:flex; justify-content:space-between; gap:10px; align-items:flex-start;">
      <div>
        <div style="font-weight:800; font-size:16px; margin-bottom:4px;">${title}</div>
        <div style="color:#6b7280; font-size:13px; line-height:1.6;">
          <b>Course:</b> ${course}<br>
          <b>Type:</b> ${type}<br>
          <b>Uploaded by:</b> ${uploader}
        </div>
      </div>

      <div style="min-width:120px; text-align:right;">
        <button class="btn primary download-btn" style="padding:10px 12px; cursor:pointer; background:#2563eb; color:white; border:none; border-radius:8px;">Download</button>
      </div>
    </div>
  `;

    const btn = box.querySelector(".download-btn");
    btn.onclick = () => handleDownload(id, data.fileName, btn);

    return box;
}

async function loadAll() {
    msg.innerText = "";
    list.innerHTML = "Loading...";

    try {
        const q = query(collection(db, "resources"), orderBy("uploadedAt", "desc"));
        const snap = await getDocs(q);

        list.innerHTML = "";

        if (snap.empty) {
            list.innerHTML = `<p style="color:#6b7280;">No resources yet. Upload a file first.</p>`;
            return;
        }

        snap.forEach((doc) => {
            list.appendChild(renderItem(doc));
        });

    } catch (e) {
        list.innerHTML = "";
        msg.innerText = "Error loading resources: " + e.message;
    }
}

async function searchByCourse() {
    msg.innerText = "";
    list.innerHTML = "Searching...";

    const course = searchCourse.value.trim().toUpperCase().replace(/\s+/g, "");
    if (!course) {
        msg.innerText = "Type a course code first.";
        list.innerHTML = "";
        return;
    }

    try {
        const q = query(
            collection(db, "resources"),
            where("course", "==", course)
        );

        const snap = await getDocs(q);

        list.innerHTML = "";

        if (snap.empty) {
            list.innerHTML = `<p style="color:#6b7280;">No files found for <b>${course}</b>.</p>`;
            return;
        }

        snap.forEach((doc) => {
            list.appendChild(renderItem(doc));
        });

    } catch (e) {
        list.innerHTML = "";
        msg.innerText = "Search error: " + e.message;
    }
}

if (!TEST_MODE) {
    onAuthStateChanged(auth, (user) => {
        if (!user) window.location.href = "auth.html";
    });
}

document.getElementById("logoutBtn").addEventListener("click", async (e) => {
    e.preventDefault();
    await signOut(auth);
    window.location.href = "auth.html";
});

document.getElementById("btnSearch").addEventListener("click", searchByCourse);
document.getElementById("btnClear").addEventListener("click", () => {
    searchCourse.value = "";
    loadAll();
});

loadAll();