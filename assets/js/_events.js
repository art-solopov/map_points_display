var eventsProc = {
    dataPoints: null,
    map: null,
    mouseHandler(event) {
        let { target, type: evtype } = event;
        let elid = target.id;
        let dp = this.dataPoints.find(e => e.id === elid)

        let style = {};
        if(evtype == 'mouseenter') { style.color = MARKER_HOVER_BORDER }
        else { style.color = MARKER_DEFAULT_BORDER }
        dp.marker.setStyle(style)
    },
    clickHandler(event) {
        let { target } = event;
        target = target.parentElement
        let { lat, lon } = target.dataset

        event.preventDefault()
        this.map.panTo([lat, lon].map(Number))
        this.map.getContainer().scrollIntoView({ behavior: 'smooth' })
    }
}

// TODO: add Babel plugin?
eventsProc.mouseHandler = eventsProc.mouseHandler.bind(eventsProc)
eventsProc.clickHandler = eventsProc.clickHandler.bind(eventsProc)
