module.exports = {
    plugins: [
        // Для Tailwind v4:
        require("@tailwindcss/postcss"),
        require("autoprefixer"),
        // Или для Tailwind v3:
        // require("tailwindcss"),
        // require("autoprefixer"),
    ],
};