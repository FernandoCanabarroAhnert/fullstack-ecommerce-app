const adminActionsButton = document.querySelector(".admin-actions");
const adminActionsMenu = document.querySelector(".admin-actions-menu");

const showMenu = () => adminActionsMenu.classList.add("show");
const hideMenu = () => adminActionsMenu.classList.remove("show");

adminActionsButton.addEventListener("mouseenter", showMenu);
adminActionsButton.addEventListener("mouseleave", hideMenu);

adminActionsMenu.addEventListener("mouseenter", showMenu);
adminActionsMenu.addEventListener("mouseleave", hideMenu);