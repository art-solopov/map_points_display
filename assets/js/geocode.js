function main() {
    let button = document.getElementById('btn_geocode')
    let results = document.getElementById('geocode_results')
    let form = button.closest('form')

    let link = button.dataset.link
    let tripId = button.dataset.tripId

    button.addEventListener('click', (ev) => {
        let address = form.querySelector('textarea[name=address]').value
        let name = form.querySelector('input[name=name]').value

        axios.post(link, { trip_id: tripId, search: address || name }).then(res => console.log(res))
    })
}

main()
