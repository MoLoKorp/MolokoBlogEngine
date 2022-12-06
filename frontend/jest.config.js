export default {
  collectCoverage: true,
  collectCoverageFrom: [
    'js/**/*.js', '!js/config/config.js', '!js/eventHandlers.js', '!js/index.js'
  ],
  coverageThreshold: {
    global: {
      lines: 90
    }
  }
}
