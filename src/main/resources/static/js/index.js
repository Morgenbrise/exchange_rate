const country = document.querySelector("#country");
const countryCode = document.querySelector("#country-code");
const price = document.querySelector("#price");
const calculation = document.querySelector("#calculation");
const exchangeRateAmount = document.querySelector("#exchange-rate-amount");

const main = document.querySelector("main");
const convertedAmount = document.querySelector("#converted-amount");
const convertedCountry = document.querySelector("#converted-country");

//국가 정보 변경
const countryChange = async (e) => {
    await countryChangeApi(e.target.value);
    price.value ? fn_submitBtn() : hide();
    countryCode.innerHTML=` ${e.target.value}/USD`;
}

const fn_submitBtn = () => {
    const m = fn_calculation();
    if(m === 0) {
        hide();
        return false;
    }
    convertedAmount.innerHTML = numberWithCommas(m);
    convertedCountry.innerHTML = country.selectedOptions[0].value;
    main.classList.add("show");
}

const hide = () => {
    price.value = "";
    main.classList.remove("show");
}

//환율 정보 호출
const countryChangeApi = (value) => {
    return fetch(`/exchange-rate/USD/${value}`)
        .then(response => {
            const json = response.json();
            if(!response.ok) {
                json.then(data => {
                    alert(data.msg);
                    throw new Error(data.msg);
                });
            }
            return json;
        })
        .then(json => exchangeRateAmount.innerHTML = numberWithCommas(roundToTwo(json.money)))
        .catch(err => console.log(err));
}

//계산 함수
const fn_calculation = () => {
    const _price = price.value;
    const _exchangeRate = exchangeRateAmount.innerHTML;

    if(_price === "0" || !_price || parseFloat(_price) > 10000) {
        alert("송금액이 바르지 않습니다");
        return 0;
    }

    if(_exchangeRate === "0" || !_exchangeRate) {
        return 0;
    }
    return roundToTwo(parseFloat(_price.replace(",", ""))
                        * parseFloat(_exchangeRate.replace(",", "")))
                    .toFixed(2);
}

//반올림 함수
const roundToTwo = (num) => {
    return +(Math.round(num + "e+2")  + "e-2");
}

//자리수 콤마
const numberWithCommas = (x) => {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

country.addEventListener("change", countryChange);
calculation.addEventListener("click", fn_submitBtn);
window.addEventListener("DOMContentLoaded", () => {
    countryChangeApi(country.selectedOptions[0].value);
});