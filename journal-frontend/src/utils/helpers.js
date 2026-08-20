export function formatDate(dateString) {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  })
}

export function formatDateShort(dateString) {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('en-US', {
    month: 'short',
    day: 'numeric',
    year: 'numeric',
  })
}

export function formatTime(dateString) {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleTimeString('en-US', {
    hour: '2-digit',
    minute: '2-digit',
  })
}

export function truncate(text, length = 160) {
  if (!text) return ''
  return text.length > length ? text.slice(0, length).trim() + '…' : text
}

export function getGreeting(name) {
  const hour = new Date().getHours()
  const part = hour < 12 ? 'morning' : hour < 17 ? 'afternoon' : 'evening'
  return `Good ${part}${name ? `, ${name}` : ''}`
}

export function extractApiError(err) {
  const data = err.response?.data
  if (!data) return 'Something went wrong'
  if (typeof data === 'string') return data
  if (data.message) return data.message
  if (typeof data === 'object') return Object.values(data).join(', ')
  return 'Something went wrong'
}
