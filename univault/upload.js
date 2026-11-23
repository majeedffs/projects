import { firebaseConfig } from './config.js';

import { initializeApp } from "https://www.gstatic.com/firebasejs/11.1.0/firebase-app.js";
import { getAuth, onAuthStateChanged, signOut } from "https://www.gstatic.com/firebasejs/11.1.0/firebase-auth.js";
import { getFirestore, collection, addDoc, doc, setDoc, serverTimestamp } from "https://www.gstatic.com/firebasejs/11.1.0/firebase-firestore.js";

const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const db = getFirestore(app);

const TEST_MODE = false;

const msg = document.getElementById("msg");
const statusEl = document.getElementById("status");
const bar = document.getElementById("bar");

let currentUserEmail = "test@student.com";

if (TEST_MODE) {
    currentUserEmail = "test@student.com";
} else {
    onAuthStateChanged(auth, (user) => {
        if (!user) {
            window.location.href = "auth.html";
            return;
        }
        currentUserEmail = user.email;
    });
}

document.getElementById("logoutBtn").addEventListener("click", async (e) => {
    e.preventDefault();

    if (TEST_MODE) {
        window.location.href = "index.html";
        return;
    }

    await signOut(auth);
    window.location.href = "auth.html";
});

document.getElementById("btnUpload").addEventListener("click", () => {
    msg.innerText = "";
    statusEl.innerText = "";
    bar.style.width = "0%";

    const course = document.getElementById("course").value.trim();
    const title = document.getElementById("title").value.trim();
    const file = document.getElementById("fileInput").files[0];

    if (!course || !file) {
        msg.innerText = "Please enter course code and choose a file.";
        return;
    }

    if (file.size > 800000) {
        msg.innerText = "File too large! Max 800KB for free database method.";
        return;
    }

    const cleanCourse = course.toUpperCase().replace(/\s+/g, "");

    statusEl.innerText = "Processing...";
    bar.style.width = "20%";

    const reader = new FileReader();
    reader.readAsDataURL(file);

    reader.onload = async () => {
        bar.style.width = "50%";
        try {
            const metaRef = await addDoc(collection(db, "resources"), {
                course: cleanCourse,
                title: title || file.name,
                fileName: file.name,
                fileType: file.type,
                fileSize: file.size,
                uploadedBy: currentUserEmail,
                uploadedAt: serverTimestamp()
            });

            await setDoc(doc(db, "resource_files", metaRef.id), {
                fileData: reader.result
            });

            bar.style.width = "100%";
            statusEl.innerText = "✅ Upload completed!";
            document.getElementById("fileInput").value = "";
            document.getElementById("title").value = "";

        } catch (e) {
            msg.innerText = "Upload error: " + e.message;
            statusEl.innerText = "";
            bar.style.width = "0%";
        }
    };

    reader.onerror = () => {
        msg.innerText = "Error reading file.";
        statusEl.innerText = "";
    };
});