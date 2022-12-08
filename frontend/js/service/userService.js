import config from '../config/config.js'

const registerUser = async (username, password) => {
  const response = await fetch(`${config.apiUrl}/user`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password, role: 'USER' })
  })
  return await response.json()
}

export {
  registerUser
}
