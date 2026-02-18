let currentRecipientId = null;
const token = localStorage.getItem("token");
let pollingInterval = null;
let currentUserId = null;

// Store contact details here to avoid passing strings in HTML
let contactsMap = {};

document.addEventListener("DOMContentLoaded", function() {
    if (!token) window.location.href = "login.html";
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
    } catch(e) {}

    loadContacts();

    const urlParams = new URLSearchParams(window.location.search);
    const quickStartId = urlParams.get('startChatWith');

    if (quickStartId) {
        currentRecipientId = quickStartId;
        // Temporary placeholder until contact list loads
        updateHeader(quickStartId, "User", "User");
    }

    startPolling();
});

function startPolling() {
    if (pollingInterval) clearInterval(pollingInterval);
    pollingInterval = setInterval(() => {
        if (currentRecipientId) {
            fetchMessagesSilent(currentRecipientId);
            loadContacts(true);
        }
    }, 3000);
}

function goBack() { window.history.back(); }

// 1. Load Contacts
async function loadContacts(isSilent = false) {
    try {
        const response = await fetch("http://localhost:8080/api/chat/conversations", {
            headers: { "Authorization": "Bearer " + token }
        });
        if(!response.ok) return;

        const contacts = await response.json();
        const list = document.getElementById("contactList");

        let html = "";
        contacts.forEach(c => {
            // FIX: Store data in map
            contactsMap[c.userId] = c;

            const activeClass = (c.userId == currentRecipientId) ? "active" : "";
            const initial = c.name ? c.name.charAt(0).toUpperCase() : '?';

            // Prefer username for images, fallback to name
            const userIdentifier = c.username || c.name;
            const pfpUrl = `http://localhost:8080/api/user/${userIdentifier}/pfp`;

            // FIX: onclick just passes ID now
            html += `
                <div class="contact-item ${activeClass}" onclick="handleContactClick(${c.userId})">
                    <div class="avatar">
                        <img src="${pfpUrl}" onerror="this.style.display='none'; this.parentElement.innerText='${initial}'">
                    </div>
                    <div class="contact-details">
                        <div class="contact-name">${c.name}</div>
                        <div class="contact-last-msg">${c.lastMessage}</div>
                    </div>
                </div>`;
        });

        // Only update DOM if not silent or if list is empty (first load)
        if (!isSilent || list.innerHTML.trim() === "") {
            list.innerHTML = html;
        }

        // If we have a current recipient, ensure header is synced (fixes the bug)
        if (currentRecipientId && contactsMap[currentRecipientId]) {
            const c = contactsMap[currentRecipientId];
            updateHeader(c.userId, c.name, c.username || c.name);
        }

    } catch (e) { console.error(e); }
}

// Wrapper for click
function handleContactClick(userId) {
    if (contactsMap[userId]) {
        const c = contactsMap[userId];
        loadChat(userId, c.name, c.username || c.name);
    }
}

// 2. Load Chat
async function loadChat(userId, userName, userIdentifier) {
    currentRecipientId = userId;
    if(!userIdentifier) userIdentifier = userName;

    updateHeader(userId, userName, userIdentifier);
    await fetchMessagesSilent(userId, true);
}

// Helper to update Header UI
function updateHeader(userId, userName, userIdentifier) {
    document.getElementById("chatHeader").style.display = "flex";
    document.getElementById("inputArea").style.display = "flex";
    document.getElementById("chatUserName").innerText = userName;

    const initial = userName ? userName.charAt(0).toUpperCase() : '?';
    const pfpUrl = `http://localhost:8080/api/user/${userIdentifier}/pfp`;

    // Completely replace avatar HTML to force reload image
    const avatar = document.getElementById("chatAvatar");
    avatar.innerHTML = `<img src="${pfpUrl}" onerror="this.style.display='none'; this.parentElement.innerText='${initial}'">`;
}

// 3. Fetch Messages
async function fetchMessagesSilent(userId, forceScroll = false) {
    const box = document.getElementById("messagesBox");
    const isAtBottom = (box.scrollHeight - box.scrollTop - box.clientHeight) < 100;

    try {
        const response = await fetch(`http://localhost:8080/api/chat/messages/${userId}`, {
            headers: { "Authorization": "Bearer " + token }
        });
        const messages = await response.json();

        let html = "";
        messages.forEach(msg => {
            const isSentByMe = (msg.senderId != currentRecipientId);
            const type = isSentByMe ? "msg-sent" : "msg-received";
            const time = new Date(msg.timestamp).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
            const editedText = msg.edited ? `<span style="font-size:10px; opacity:0.7; margin-left:4px;">(edited)</span>` : "";

            let actionsHtml = "";
            if (isSentByMe) {
                actionsHtml = `
                    <div class="msg-actions" style="position: absolute; top: 5px; right: 5px; cursor: pointer;">
                        <span onclick="toggleMenu('menu-${msg.id}')" style="font-size:16px; color:rgba(255,255,255,0.8); padding:0 5px;">⋮</span>
                        <div id="menu-${msg.id}" class="action-menu" style="display:none; position:absolute; right:0; top:20px; background:white; color:#333; border-radius:6px; box-shadow:0 4px 12px rgba(0,0,0,0.15); z-index:100; min-width:100px; overflow:hidden;">
                            <div onclick="editMessage(${msg.id}, '${msg.content.replace(/'/g, "\\'")}')" style="padding:10px; border-bottom:1px solid #f0f0f0; font-size:13px; text-align:left; cursor:pointer;" onmouseover="this.style.background='#f9f9f9'" onmouseout="this.style.background='white'">Edit</div>
                            <div onclick="deleteMessage(${msg.id})" style="padding:10px; color:#ef4444; font-size:13px; text-align:left; cursor:pointer;" onmouseover="this.style.background='#fef2f2'" onmouseout="this.style.background='white'">Delete</div>
                        </div>
                    </div>
                `;
            }

            html += `
                <div class="message ${type}" style="position: relative; padding-right: 35px;" onmouseleave="closeMenu('menu-${msg.id}')">
                    ${msg.content} ${editedText}
                    <div class="msg-time" style="font-size:10px; opacity:0.8; text-align:right; margin-top:4px;">${time}</div>
                    ${actionsHtml}
                </div>
            `;
        });

        if(box.innerHTML !== html) {
            box.innerHTML = html;
            if (forceScroll || isAtBottom) box.scrollTop = box.scrollHeight;
        }

    } catch (e) { console.error(e); }
}

// 4. Message Actions
function toggleMenu(id) {
    const menu = document.getElementById(id);
    if(menu) menu.style.display = (menu.style.display === 'block') ? 'none' : 'block';
}
function closeMenu(id) {
    const menu = document.getElementById(id);
    if(menu) menu.style.display = 'none';
}

async function editMessage(id, oldText) {
    const newText = prompt("Edit message:", oldText);
    if(newText && newText !== oldText) {
        await fetch(`http://localhost:8080/api/chat/edit/${id}`, {
            method: 'PUT',
            headers: { "Authorization": "Bearer " + token, "Content-Type": "application/json" },
            body: JSON.stringify({ content: newText })
        });
        fetchMessagesSilent(currentRecipientId);
    }
}

async function deleteMessage(id) {
    if(!confirm("Delete message?")) return;
    await fetch(`http://localhost:8080/api/chat/delete/${id}`, {
        method: 'DELETE',
        headers: { "Authorization": "Bearer " + token }
    });
    fetchMessagesSilent(currentRecipientId);
}

async function sendMessage() {
    const input = document.getElementById("msgInput");
    const text = input.value.trim();
    if (!text || !currentRecipientId) return;

    try {
        await fetch("http://localhost:8080/api/chat/send", {
            method: "POST",
            headers: { "Authorization": "Bearer " + token, "Content-Type": "application/json" },
            body: JSON.stringify({ recipientId: currentRecipientId, content: text })
        });
        input.value = "";
        fetchMessagesSilent(currentRecipientId, true);
    } catch (e) {}
}

function handleEnter(e) { if (e.key === "Enter") sendMessage(); }