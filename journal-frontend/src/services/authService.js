import api from './axios'

const authService = {
  login: async (credentials) => {
    const response = await api.post('/public/v1/login', credentials)
    return response.data
  },

  register: async (userData) => {
    const response = await api.post('/public/v1/signup', userData)
    return response.data
  },
}

export default authService
