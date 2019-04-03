var dataPoints = null;

const MAP_ATTRIBUTION = "<a href=\"https://wikimediafoundation.org/wiki/Maps_Terms_of_Use\">Wikimedia</a>"
const MAP_URL = "https://maps.wikimedia.org/osm-intl/{z}/{x}/{y}{r}.png"

function getDataPoints() {
    let els = Array.from(document.querySelectorAll('.group .items li'))
    console.log(els)

    return els.map(el => {
        let data = {
            name: el.querySelector('.item-name').innerText,
            lat: Number(el.dataset.lat),
            lon: Number(el.dataset.lon),
            category: el.dataset.category
        }
        return {el, data}
    })
}

const MARKER_COLORS = {
    "museum": "#8affff",
    "transport": "#aaff7f",
    "accomodation": "#e29cff",
    "sight": "#419eee",
    "food": "#bc7d00",
    "shop": "#8b8bcf",
    "other": "#d1d1d1",
}

const BASE_MARKER_OPTS = {
    radius: 12,
    fill: true,
    fillOpacity: 0.85,
    color: "black",
    weight: 2
}

function makeMarker({name, lat, lon, category}) {
    let options = Object.assign({fillColor: MARKER_COLORS[category]},
                                BASE_MARKER_OPTS)

    return L.circleMarker([lat, lon], options).bindPopup(`${name} [${category}]`)
}

function main() {
    let map = L.map('map').setView([43.766, 11.233], 15)
    L.tileLayer(MAP_URL, {attribution: MAP_ATTRIBUTION}).addTo(map)

    dataPoints = getDataPoints()
    dataPoints.forEach((el) => {
        el.marker = makeMarker(el.data)

        el.marker.addTo(map)
    })
}

main();
