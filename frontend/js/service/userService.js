import config from '../config/config.js'

const createUser = async (username, password) => {
  const response = await fetch(`${config.apiUrl}/user`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password, role: 'USER' })
  })
  return await response.json()
}

const getUserAuth = () => {
  const username = document.getElementById('username').value
  const password = document.getElementById('password').value
  return 'Basic ' + btoa(`${username}:${password}`)
}

const login = () => {
  localStorage.setItem('auth', getUserAuth())
  window.location.href = '/'
}

const logout = () => {
  localStorage.removeItem('auth')
  window.location.href = '/'
}

const register = async () => {
  const username = document.getElementById('username').value
  const password = document.getElementById('password').value
  await createUser(username, password)
  alert(`you have registered as ${username}`)
}

const isLoggedIn = () => !!localStorage.getItem('auth')

export {
  createUser,
  login,
  logout,
  register,
  isLoggedIn
}
