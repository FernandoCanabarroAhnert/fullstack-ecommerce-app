let paymentTypeInputs = document.querySelectorAll(".payment-type-input");
let paymentMethodDetails = document.querySelectorAll(".payment-method-detail");
paymentTypeInputs.forEach(input => {
    input.addEventListener("click", () => {
        const paymentMethodDetailTarget = input.dataset.target;
        const paymentMethodDetail = document.querySelector(paymentMethodDetailTarget);
        paymentMethodDetails.forEach(item => item.classList.remove("payment-detail-active"));
        paymentMethodDetail.classList.add("payment-detail-active");
    })
})

function copyText(text, num) {
    navigator.clipboard.writeText(text).then(() => {
        document.querySelector(`.copy-response-${num}`).classList.remove("d-none");
    }).catch(err => {
        console.error('Erro ao copiar o texto:', err);
        document.getElementById('status').innerText = 'Erro ao copiar o texto.';
    });
}

let couponsInputs = document.querySelectorAll(".coupon-id-input");
couponsInputs.forEach(input => {
    input.addEventListener("click", () => {
        const discountPercentage = parseFloat(input.dataset.percentagediscount);
        let orderPrice = parseFloat(input.dataset.orderprice.replace(",", "."));

        const discountValue = (orderPrice * discountPercentage) / 100;
        const orderSummaryDiscountPriceElement = document.querySelector("#order-summary-price-discount");
        orderSummaryDiscountPriceElement.innerText = `R$-${discountValue.toFixed(2).replace(".", ",")}`;

        const orderSummaryFinalPriceElement = document.querySelector("#order-summary-final-price");
        const orderSummaryFinalPrice = (orderPrice - discountValue).toFixed(2).replace(".", ",");
        orderSummaryFinalPriceElement.innerText = "R$" + orderSummaryFinalPrice;
        updateBoleto(orderSummaryFinalPrice);
    });
});

function updateBoleto(newPrice) {
    let boletoLink = document.querySelector('#boleto-link');
    let currentUrl = boletoLink.getAttribute('href');

    let newUrl = currentUrl.replace(/totalOrderValue=[^&]*/, 'totalOrderValue=' + newPrice);
    boletoLink.setAttribute('href', newUrl);
}
