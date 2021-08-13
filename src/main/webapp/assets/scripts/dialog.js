const dialog = document.querySelector("#message-modal");
if (dialog) {
    window.messageModal = new bootstrap.Modal(dialog);
    if (window.messageModal) {
        window.messageIcon = document.querySelector("#message-icon");
        window.messageContent = document.querySelector("#message-content");

        window.neededReload = false;
        dialog.addEventListener('hidden.bs.modal', function () {
            if (neededReload) {
                window.location.reload();
                window.neededReload = false;
            }
        });
    } else {
        console.error("Message modal is not available.")
    }
} else {
    console.error("Message Dialog not loaded.")
}