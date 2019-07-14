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

const MapIcon = L.Icon.extend({
    options: {
        iconSize: [34, 51],
        iconAnchor: [34 / 2, 51],
        popupAnchor: [0, -30]
    }
})

function makeMarker({name, lat, lon, category}) {
    var icon = new MapIcon({iconUrl: "/icons/" + category + ".png"})

    return L.marker([lat, lon], {icon: icon}).bindPopup(`${name} (${category})`)
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

        // TODO: think about marker hover events
        // dp.el.addEventListener('mouseenter', eventsProc.mouseHandler)
        // dp.el.addEventListener('mouseleave', eventsProc.mouseHandler)
        dp.el.querySelector('.pan-link').addEventListener('click', eventsProc.clickHandler)
    })
}

main();
