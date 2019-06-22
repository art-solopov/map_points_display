const MAP_ATTRIBUTION = "<a href=\"https://wikimediafoundation.org/wiki/Maps_Terms_of_Use\">Wikimedia</a>"
const MAP_URL = "https://maps.wikimedia.org/osm-intl/{z}/{x}/{y}{r}.png"
const MAP_ZOOM_LEVEL = 12

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
