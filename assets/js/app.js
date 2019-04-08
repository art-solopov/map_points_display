var dataPoints = null;

const MAP_ATTRIBUTION = "<a href=\"https://wikimediafoundation.org/wiki/Maps_Terms_of_Use\">Wikimedia</a>"
const MAP_URL = "https://maps.wikimedia.org/osm-intl/{z}/{x}/{y}{r}.png"

function getDataPoints() {
    let els = Array.from(document.querySelectorAll('.group .items li'))

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

const MARKER_DEFAULT_BORDER = "black"
const MARKER_HOVER_BORDER = "#be005f"

const BASE_MARKER_OPTS = {
    radius: 12,
    fill: true,
    fillOpacity: 0.85,
    color: MARKER_DEFAULT_BORDER,
    weight: 2
}

function makeMarker({name, lat, lon, category}) {
    let options = Object.assign({fillColor: MARKER_COLORS[category]},
                                BASE_MARKER_OPTS)

    return L.circleMarker([lat, lon], options).bindPopup(`${name} [${category}]`)
}

function genRandomId() {
    let a = Math.ceil(Math.random() * 1000)
    let b = Math.ceil(Math.random() * 10000)
    let c = Math.ceil(Math.random() * 100)

    return `${a}-${b}-${c}`
}

function makeMouseEventProcessor(dataPoints) {
    return event => {
        let { target, type: evtype } = event;
        let { elid } = target.dataset;
        let dp = dataPoints.find(e => e.id === elid)

        let style = {};
        if(evtype == 'mouseenter') { style.color = MARKER_HOVER_BORDER }
        else { style.color = MARKER_DEFAULT_BORDER }
        dp.marker.setStyle(style)
    }
}

function makeClickProcessor(map) {
    return event => {
        let { target } = event;
        target = target.parentElement
        let { lat, lon } = target.dataset

        event.preventDefault()
        map.panTo([lat, lon].map(Number))
    }
}

function main() {
    dataPoints = getDataPoints()
    let baseLatLon = dataPoints.reduce(([accLat, accLon], dp) => {
        let {lat, lon} = dp.data
        return [accLat + lat, accLon + lon]
    }, [0, 0]).map(e => e / dataPoints.length)

    let map = L.map('map').setView(baseLatLon, 15)
    L.tileLayer(MAP_URL, {attribution: MAP_ATTRIBUTION}).addTo(map)

    let moev = makeMouseEventProcessor(dataPoints)
    let clev = makeClickProcessor(map)

    dataPoints.forEach((dp) => {
        dp.marker = makeMarker(dp.data)
        dp.marker.addTo(map)

        // TODO: replace with an actual id
        dp.id = genRandomId()
        dp.el.dataset.elid = dp.id

        dp.el.addEventListener('mouseenter', moev)
        dp.el.addEventListener('mouseleave', moev)
        dp.el.addEventListener('click', clev)
    })
}

main();
