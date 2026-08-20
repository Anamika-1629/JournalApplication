import api from './axios'

const journalService = {
  getAll: async () => {
    const response = await api.get('/journal/v1/journal-entries')
    return response.data
  },

  getById: async (id) => {
    const response = await api.get(`/journal/v1/find-entry/${id}`)
    return response.data
  },

  create: async (entry) => {
    const response = await api.post('/journal/v1/create-entries', entry)
    return response.data
  },

  update: async (id, entry) => {
    const response = await api.patch(`/journal/v1/update-entry/${id}`, entry)
    return response.data
  },

  delete: async (id) => {
    await api.delete(`/journal/v1/delete-entry/${id}`)
  },
}

export default journalService
