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

function main() {
    let dataPoints = getDataPoints()
    let baseLatLon = dataPoints.reduce(([accLat, accLon], dp) => {
        let {lat, lon} = dp.data
        return [accLat + lat, accLon + lon]
    }, [0, 0]).map(e => e / dataPoints.length)

    let map = L.map('map').setView(baseLatLon, 15)
    L.tileLayer(MAP_URL, {attribution: MAP_ATTRIBUTION}).addTo(map)

    eventsProc.dataPoints = dataPoints
    eventsProc.map = map

    dataPoints.forEach((dp) => {
        dp.marker = makeMarker(dp.data)
        dp.marker.addTo(map)

        // TODO: replace with an actual id
        dp.id = genRandomId()
        dp.el.dataset.elid = dp.id

        dp.el.addEventListener('mouseenter', eventsProc.mouseHandler)
        dp.el.addEventListener('mouseleave', eventsProc.mouseHandler)
        dp.el.addEventListener('click', eventsProc.clickHandler)
    })
}

main();
