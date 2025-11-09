let stomp;

// Set up the connection and subscribe to the broadcasts
function connectWs() {
    const sock = new SockJS('/ws');
    stomp = Stomp.over(sock);
    stomp.debug = null;
    stomp.connect({}, () => {
        // Broadcasts from server
        stomp.subscribe('/topic/apps/opened', msg => {
            const message = JSON.parse(msg.body);
            lockApp(message.id);
        });
        stomp.subscribe('/topic/apps/closed', msg => {
            const message = JSON.parse(msg.body);
            unlockApp(message.id)
        });
    });
}

function openAppOnServer(id) {
    // Server: set openedByAdmin=true and broadcast to /topic/apps/opened
    if (stomp && stomp.connected) {
        stomp.send(`/app/open/${id}`, {}, "{}");
    }
}

function closeAppOnServer(id) {
    // Server: set openedByAdmin=false and broadcast to /topic/apps/closed
    if (stomp && stomp.connected) {
        stomp.send(`/app/close/${id}`, {}, "{}");
    }
}

const byAppId = (id) =>
    document.querySelectorAll(`[data-id="${id}"]`)

// Lock the app so a user can't open it at the same time
function lockApp(id) {
    for (const element of byAppId(id)) {
        if ("disabled" in element) element.disabled = true;
        element.setAttribute("aria-disabled", "true");

        // add a lock icon if it doesn't already
        if (!element.querySelector(".lock-icon")) {
            const icon = document.createElement("span");
            icon.className = "lock-icon";
            icon.setAttribute("aria-hidden", "true");
            icon.textContent = "🔒";
            element.prepend(icon);
        }
    }
}

// Unlock the app so another user can open it
function unlockApp(id) {
    for (const el of byAppId(id)) {
        if ("disabled" in el) el.disabled = false;
        el.removeAttribute("aria-disabled");
        const icon = el.querySelector(".lock-icon");
        if (icon) icon.remove();
    }
}

// Start websocket connection
connectWs();