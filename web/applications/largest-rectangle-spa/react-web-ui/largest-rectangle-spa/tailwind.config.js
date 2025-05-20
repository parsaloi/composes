module.exports = {
  content: [
    './public/index.html',
    './src/**/*.{jsx,js}',
  ],
  theme: {
    extend: {
      colors: {
        primary: '#4CAF50',
        secondary: '#2196F3',
        background: '#f0f0f0',
        text: '#333333',
        error: '#f44336',
      },
      fontFamily: {
        main: ['Arial', 'sans-serif'],
        header: ['Georgia', 'serif'],
      },
    },
  },
  plugins: [],
};
