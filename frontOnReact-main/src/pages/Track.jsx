import { useState } from "react";
import Header from "../components/HeaderAuth";

export default function Track() {
    const [trackNumber, setTrackNumber] = useState("");

    const handleSubmit = (e) => {
        e.preventDefault();
        alert(`Отслеживание заказа: ${trackNumber}`);
    };
    return (
        <>
            <Header />
            <div className="min-h-screen bg-gray-100 p-8">
                <div className="max-w-md mx-auto bg-white p-6 rounded-lg shadow-md">
                    <h1 className="text-2xl font-bold mb-6">Отследить посылку</h1>
                    <form onSubmit={handleSubmit} className="space-y-4">
                        <div className="flex flex-col">
                            <label className="font-medium mb-1">Номер заказа:</label>
                            <input
                                type="text"
                                value={trackNumber}
                                onChange={(e) => setTrackNumber(e.target.value)}
                                className="border rounded p-2"
                                placeholder="Введите номер"
                            />
                        </div>
                        <button type="submit" className="bg-blue-600 text-white p-2 rounded w-full">
                            Проверить
                        </button>
                    </form>
                </div>
            </div>
        </>
    );
}