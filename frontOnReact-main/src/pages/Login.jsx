// ...existing code...
import { useState } from "react";

function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");
        if (!username.trim() || !password) {
            setError("Введите логин и пароль");
            return;
        }
        setLoading(true);
        try {
            // TODO: заменить на реальный вызов API
            const res = await fetch("/api/auth/login", { method: "POST", headers:{ "Content-Type":"application/json" }, body: JSON.stringify({ email, password }) });
            if (!res.ok) throw new Error("Неверные данные");
            await new Promise((r) => setTimeout(r, 800)); // заглушка
            console.log("Успешный вход:", username);
            // редирект / сохранить токен / контекст аутентификации
        } catch (err) {
            setError("Ошибка входа — проверьте логин и пароль");
        } finally {
            setLoading(false);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="max-w-sm mx-auto mt-12 p-6 bg-white/70 backdrop-blur rounded-md shadow">
            <h2 className="text-2xl font-semibold mb-4">Вход</h2>

            {error && <div className="text-sm text-red-600 mb-3">{error}</div>}

            <label className="block mb-3">
                <span className="text-sm text-gray-700">Имя пользователя</span>
                <input
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    className="mt-1 block w-full rounded border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"
                    type="text"
                    placeholder="Имя пользователя"
                />
            </label>

            <label className="block mb-4">
                <span className="text-sm text-gray-700">Пароль</span>
                <input
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    className="mt-1 block w-full rounded border border-gray-300 px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-400"
                    type="password"
                    placeholder="Пароль"
                />
            </label>

            <button
                type="submit"
                disabled={loading}
                className="w-full py-2 bg-blue-600 hover:bg-blue-700 text-white rounded disabled:opacity-50"
            >
                {loading ? "Вход..." : "Войти"}
            </button>
        </form>
    );
}

export default Login;
// ...existing code...