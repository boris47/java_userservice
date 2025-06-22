module.exports = {
	content: ["./src/**/*.{html,js,ts,jsx,tsx}"],
	corePlugins: {
		backgroundOpacity: true
	},
	theme: {
		extend: {
			colors: {
				primary: "#1e40af"
			},
			opacity: {
				'15': '0.15',
				'35': '0.35',
				'65': '0.65',
			},
			backgroundOpacity: ['active'],
			backgroundOpacity: {
				'15': '0.15',
				'35': '0.35',
				'65': '0.65',
			},
		},
	},
	plugins: []
}
