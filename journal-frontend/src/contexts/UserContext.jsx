import { createContext, useContext, useState, useCallback, useEffect } from 'react'
import { useAuth } from './AuthContext'

const UserContext = createContext(null)

function parseJwt(token) {
  try {
    return JSON.parse(atob(token.split('.')[1]))
  } catch {
    return null
  }
}

export function UserProvider({ children }) {
  const { token, isAuthenticated } = useAuth()
  const [user, setUser] = useState(null)

  useEffect(() => {
    if (token) {
      const payload = parseJwt(token)
      if (payload) {
        setUser({
          userName: payload.sub,
          roles: payload.roles || [],
          email: payload.email || '',
        })
      }
    } else {
      setUser(null)
    }
  }, [token])

  const updateUser = useCallback((data) => {
    setUser((prev) => ({ ...prev, ...data }))
  }, [])

  return (
    <UserContext.Provider value={{ user, updateUser }}>
      {children}
    </UserContext.Provider>
  )
}

export function useUser() {
  const ctx = useContext(UserContext)
  if (!ctx) throw new Error('useUser must be used within UserProvider')
  return ctx
}
