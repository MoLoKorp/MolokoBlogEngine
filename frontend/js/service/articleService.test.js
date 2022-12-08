import { getArticles, getArticle, createArticle, updateArticle, deleteArticle, importArticles } from './articleService.js'
import { jest, expect, test, beforeEach } from '@jest/globals'

const articles = [
  {
    id: '1',
    text: 'article1'
  },
  {
    id: '2',
    text: 'article2'
  }
]

beforeEach(() => {
  const localStorageMock = {
    getItem: jest.fn()
  }
  global.localStorage = localStorageMock
})

test('getArticles successfully fetches 2 articles', async () => {
  global.fetch = jest.fn(() =>
    Promise.resolve({
      json: () => Promise.resolve(articles)
    })
  )
  const result = await getArticles()
  expect(result.length).toEqual(2)
  expect(result[0]).toEqual({
    id: '1',
    text: 'article1'
  })
  expect(result[1]).toEqual({
    id: '2',
    text: 'article2'
  })
})

test('getArticle successfully fetches article', async () => {
  global.fetch = jest.fn(() =>
    Promise.resolve({
      json: () => Promise.resolve(articles[0])
    })
  )
  const result = await getArticle('1')
  expect(result.id).toEqual('1')
  expect(result.text).toEqual('article1')
})

test('createArticle successfully fetches article', async () => {
  global.fetch = jest.fn(() =>
    Promise.resolve({
      json: () => Promise.resolve(articles[0])
    })
  )
  const result = await createArticle('1', {
    id: '1',
    text: 'new_text'
  })
  expect(result.id).toEqual('1')
  expect(result.text).toEqual('article1')
})

test('updateArticle successfully fetches article', async () => {
  global.fetch = jest.fn(() =>
    Promise.resolve({
      json: () => Promise.resolve(articles[0])
    })
  )
  const result = await updateArticle('1', {
    id: '1',
    text: 'new_text'
  })
  expect(result.id).toEqual('1')
  expect(result.text).toEqual('article1')
})

// :TODO
test('deleteArticle', async () => {
  global.fetch = jest.fn(() =>
    Promise.resolve({
      json: () => Promise.resolve(articles[0])
    })
  )
  await deleteArticle('1')
})

// :TODO
test('importArticles', async () => {
  global.fetch = jest.fn(() =>
    Promise.resolve({
      json: () => Promise.resolve(articles[0])
    })
  )
  await importArticles('importFile')
})
