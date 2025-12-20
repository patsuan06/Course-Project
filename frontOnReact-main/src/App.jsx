import { useState, useEffect } from "react";
import axios from "axios";

function AddressAutocomplete() {
    const [input, setInput] = useState("");
    const [suggestions, setSuggestions] = useState([]);

    useEffect(() => {
        if (!input) {
            setSuggestions([]);
            return;
        }

        // set a 1-second debounce
        const handler = setTimeout(() => {
            fetchSuggestions(input);
        }, 1000);

        // cleanup if input changes before timeout
        return () => clearTimeout(handler);
    }, [input]);

    const fetchSuggestions = async (query) => {
        try {
            const response = await axios.get("/api/google_maps/autocomplete", {
                params: { q: query },
            });
            setSuggestions(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    return (
        <div>
            <input
                type="text"
                value={input}
                onChange={(e) => setInput(e.target.value)}
                placeholder="Type address..."
            />
            <ul>
                {suggestions.map((s) => (
                    <li key={s.placeId}>{s.description}</li>
                ))}
            </ul>
        </div>
    );
}

export default AddressAutocomplete;
