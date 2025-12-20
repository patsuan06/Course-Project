import React, { useEffect, useRef, useState } from "react";

function GoogleMapsAutocomplete() {
    const inputRef = useRef(null);
    const [selectedPlace, setSelectedPlace] = useState(null);

    useEffect(() => {
        if (!window.google) {
            console.error("Google Maps JS API not loaded");
            return;
        }

        const autocomplete = new window.google.maps.places.Autocomplete(
            inputRef.current,
            {
                // restrict to a country, e.g., KG
                componentRestrictions: { country: "KG" },
                fields: ["place_id", "geometry", "formatted_address"],
                types: ["address"], // only addresses
            }
        );

        autocomplete.addListener("place_changed", () => {
            const place = autocomplete.getPlace();
            if (!place.geometry) {
                console.error("No geometry returned for selected place");
                return;
            }

            const data = {
                placeId: place.place_id,
                address: place.formatted_address,
                lat: place.geometry.location.lat(),
                lng: place.geometry.location.lng(),
            };

            setSelectedPlace(data);
            console.log("Selected place:", data);
        });
    }, []);

    return (
        <div className="max-w-md mx-auto mt-8 p-4 bg-white rounded shadow">
            <label className="block mb-2">
                <span className="text-gray-700">Введите адрес</span>
                <input
                    type="text"
                    ref={inputRef}
                    className="mt-1 block w-full rounded border border-gray-300 px-3 py-2
                     focus:outline-none focus:ring-2 focus:ring-blue-400"
                    placeholder="Начните вводить адрес..."
                />
            </label>

            {selectedPlace && (
                <div className="mt-4 p-3 border rounded bg-gray-50">
                    <p>
                        <strong>Address:</strong> {selectedPlace.address}
                    </p>
                    <p>
                        <strong>Lat:</strong> {selectedPlace.lat}, <strong>Lng:</strong>{" "}
                        {selectedPlace.lng}
                    </p>
                    <p>
                        <strong>Place ID:</strong> {selectedPlace.placeId}
                    </p>
                </div>
            )}
        </div>
    );
}

export default GoogleMapsAutocomplete;
