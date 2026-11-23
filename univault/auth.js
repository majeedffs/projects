import { firebaseConfig } from './config.js';
import { initializeApp } from "https://www.gstatic.com/firebasejs/11.1.0/firebase-app.js";
import {
    getAuth,
    createUserWithEmailAndPassword,
    signInWithEmailAndPassword
} from "https://www.gstatic.com/firebasejs/11.1.0/firebase-auth.js";
import {
    getFirestore,
    doc,
    setDoc,
    serverTimestamp
} from "https://www.gstatic.com/firebasejs/11.1.0/firebase-firestore.js";

const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const db = getFirestore(app);

const msg = document.getElementById('msg');

document.getElementById('roleSelect').addEventListener('change', (e) => {
    const role = e.target.value;

    if (role === 'student') {
        document.getElementById('studentFields').style.display = "block";
        document.getElementById('teacherFields').style.display = "none";
    } else {
        document.getElementById('studentFields').style.display = "none";
        document.getElementById('teacherFields').style.display = "block";
    }
});


document.getElementById('btnLogin').addEventListener('click', async () => {
    try {
        await signInWithEmailAndPassword(
            auth,
            document.getElementById('email').value,
            document.getElementById('pass').value
        );


        window.location.href = "dashboard.html";

    } catch (e) {
        msg.innerText = "Login Error: " + e.message;
    }
});


document.getElementById('btnReg').addEventListener('click', async () => {
    const email = document.getElementById('regEmail').value;
    const pass = document.getElementById('regPass').value;
    const pass2 = document.getElementById('regPass2').value;
    const role = document.getElementById('roleSelect').value;


    if (role === 'student' && !document.getElementById('studentId').value) {
        msg.innerText = "Missing Student ID!";
        return;
    }

    if (role === 'teacher' && !document.getElementById('department').value) {
        msg.innerText = "Missing Department!";
        return;
    }


    if (pass !== pass2) {
        showPasswordPopup();
        return;
    }

    try {
        const cred = await createUserWithEmailAndPassword(auth, email, pass);
        const user = cred.user;

        let userData = {
            uid: user.uid,
            email: email,
            role: role,
            createdAt: serverTimestamp()
        };

        if (role === 'student') {
            userData.studentId = document.getElementById('studentId').value;
            userData.university = document.getElementById('university').value;
            userData.major = document.getElementById('major').value;
        } else {
            userData.department = document.getElementById('department').value;
            userData.office = document.getElementById('office').value;
        }

        await setDoc(doc(db, "users", user.uid), userData);


        window.location.href = "dashboard.html";

    } catch (e) {
        msg.innerText = "Register Error: " + e.message;
    }
});


document.getElementById('showReg').onclick = () => {
    document.getElementById('loginForm').style.display = "none";
    document.getElementById('regForm').style.display = "block";
    document.getElementById('title').innerText = "Create Account";
};

document.getElementById('showLogin').onclick = () => {
    document.getElementById('regForm').style.display = "none";
    document.getElementById('loginForm').style.display = "block";
    document.getElementById('title').innerText = "Student Login";
};
