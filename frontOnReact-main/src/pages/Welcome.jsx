import { Link } from 'react-router-dom';

export default function Welcome() {
    return (
        <div className="min-h-screen bg-gradient-to-br from-blue-500 to-purple-600 flex items-center justify-center p-4">
            <div className="text-center text-white">
                <h1 className="text-5xl font-bold mb-4">КурьерМВП</h1>
                <p className="text-xl mb-8">Быстрая доставка по городу</p>

                <div className="space-x-4">
                    <Link
                        to="/login"
                        className="bg-white text-blue-600 px-6 py-3 rounded-lg font-semibold hover:bg-gray-100 transition"
                    >
                        Войти
                    </Link>
                    <Link
                        to="/register"
                        className="border-2 border-white px-6 py-3 rounded-lg font-semibold hover:bg-white hover:text-blue-600 transition"
                    >
                        Регистрация
                    </Link>
                </div>
            </div>
        </div>
    );
}