module.exports = {
  collectCoverage: true,
  collectCoverageFrom: [
    'js/**/*.js', '!js/config/config.js', '!js/eventHandlers.js'
  ],
  coverageThreshold: {
    global: {
      lines: 100
    }
  }
}
