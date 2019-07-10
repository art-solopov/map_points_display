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

function getBounds(dataPoints) {
    let lats = dataPoints.map(e => e.data.lat)
    let lons = dataPoints.map(e => e.data.lon)

    let minLat = Math.min(...lats)
    let minLon = Math.min(...lons)
    let maxLat = Math.max(...lats)
    let maxLon = Math.max(...lons)

    return [[minLat, minLon], [maxLat, maxLon]]
}

function makeMarker({name, lat, lon, category}) {
    let options = Object.assign({fillColor: MARKER_COLORS[category]},
                                BASE_MARKER_OPTS)

    return L.circleMarker([lat, lon], options).bindPopup(`${name} [${category}]`)
}

function main() {
    let dataPoints = getDataPoints()
    let baseLatLon = dataPoints.reduce(([accLat, accLon], dp) => {
        let {lat, lon} = dp.data
        return [accLat + lat, accLon + lon]
    }, [0, 0]).map(e => e / dataPoints.length)

    let bounds = getBounds(dataPoints)
    let map = L.map('map').fitBounds(bounds)
    L.tileLayer(MAP_URL, {attribution: MAP_ATTRIBUTION}).addTo(map)

    eventsProc.dataPoints = dataPoints
    eventsProc.map = map

    dataPoints.forEach((dp) => {
        dp.marker = makeMarker(dp.data)
        dp.marker.addTo(map)

        dp.id = dp.el.id

        dp.el.addEventListener('mouseenter', eventsProc.mouseHandler)
        dp.el.addEventListener('mouseleave', eventsProc.mouseHandler)
        dp.el.addEventListener('click', eventsProc.clickHandler)
    })
}

main();
