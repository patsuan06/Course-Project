import Header from '../components/HeaderAuth';
import { useState } from 'react';
export default function Home() {

    // about location
    const [from, setFrom] = useState('');
    const [to, setTo] = useState('');

    // add GPS A & B 
    const handleGPSAChange = (event) => {
        setFrom(event.target.value);
    }
    const handleGPSBChange = (event) => {
        setTo(event.target.value)
    }

    // Description
    const [description, setDescription] = useState('');
    const handleDescriptionChange = (event) => {
        setDescription(event.target.value);
    }

    // Data about poluchatel
    const [name, setName] = useState('');
    const [number, setNumber] = useState('');

    const handleNameChange = (event) => {
        setName(event.target.value);
    }
    const handleNumberChange = (event) => {
        setNumber(event.target.value);
    }

    const handleSubmit = (event) => {
        event.preventDefault();
        alert("Заказ в обработке!")
        console.log('datas ', { from, to, description, name, number })
    }

    return (
        <><Header /><form onSubmit={handleSubmit} className="max-w-md mx-auto p-4 space-y-4">
            <div className="flex flex-col">
                <label className="font-medium">
                    Откуда:
                </label>
                <input className="border rounded p-2" type="text" value={from} onChange={handleGPSAChange} />
            </div>
            <div className="flex flex-col">
                <label className="font-medium">
                    Куда:
                </label>
                <input className="border rounded p-2" type="text" value={to} onChange={handleGPSBChange} />
            </div>

            <div className="flex flex-col">
                <label className="font-medium">
                    Описание посылки:
                </label>
                <input className="border rounded p-2" type="text" value={description} onChange={handleDescriptionChange} />
            </div>
            <div className="flex flex-col">
                <label className="font-medium">
                    Имя получателя:
                </label>
                <input className="border rounded p-2" type="text" value={name} onChange={handleNameChange} />
            </div>

            <div className="flex flex-col">
                <label className="font-medium">
                    Номер получателя:
                </label>
                <input className="border rounded p-2" type="text" value={number} onChange={handleNumberChange} />
            </div>
            <button type="submit" className="bg-blue-600 text-white p-2 rounded">
                Заказать
            </button>
        </form></>
    )

}