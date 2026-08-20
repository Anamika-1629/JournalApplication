import api from './axios'

const userService = {
  getMe: async () => {
    // Backend derives identity from JWT; we decode it client-side
    const token = localStorage.getItem('token')
    if (!token) throw new Error('No token')
    const payload = JSON.parse(atob(token.split('.')[1]))
    return payload
  },

  update: async (data) => {
    const response = await api.patch('/user/v1/update', data)
    return response.data
  },

  changePassword: async (data) => {
    const response = await api.patch('/user/v1/password', data)
    return response.data
  },

  deleteAccount: async () => {
    await api.delete('/user/v1/delete')
  },
}

export default userService
