const mobileNav = document.querySelector(".mobile-nav")
const mobileMenuButton = document.querySelector(".navbar-toggler-icon")
mobileMenuButton.addEventListener("click", () => {
    document.body.classList.add("no-scroll")
    mobileNav.classList.add("open")
})
const mobileMenuCloseButton = document.querySelector(".close-btn")
mobileMenuCloseButton.addEventListener("click", () => {
    document.body.classList.remove("no-scroll")
    mobileNav.classList.remove("open")
})