import { registerUser } from './userService.js'
import { jest, expect, test } from '@jest/globals'

test('registerUser successfully calls registerUser API', async () => {
  const user = {
    username: 'testUsername',
    password: 'testPassword',
    role: 'USER_ROLE'
  }
  global.fetch = jest.fn(() =>
    Promise.resolve({
      json: () => Promise.resolve(user)
    })
  )
  const result = await registerUser('testUsername', 'testPassword')
  expect(result).toEqual(user)
})
