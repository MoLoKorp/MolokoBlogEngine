module.exports = {
  collectCoverage: true,
  collectCoverageFrom: [
    'js/**/*.mjs', '!js/config/config.mjs', '!js/eventHandlers.mjs'
  ],
  coverageThreshold: {
    global: {
      lines: 100
    }
  }
}
