import { useState, useCallback } from 'react'
import journalService from '../services/journalService'
import { useToast } from '../contexts/ToastContext'

export function useJournal() {
  const [entries, setEntries] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const { toast } = useToast()

  const fetchEntries = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await journalService.getAll()
      setEntries(data)
      return data
    } catch (err) {
      setError('Failed to load journal entries')
      throw err
    } finally {
      setLoading(false)
    }
  }, [])

  const createEntry = useCallback(async (entry) => {
    try {
      const newEntry = await journalService.create(entry)
      setEntries((prev) => [newEntry, ...prev])
      toast.success('Entry saved to your journal')
      return newEntry
    } catch (err) {
      toast.error('Failed to save entry')
      throw err
    }
  }, [toast])

  const updateEntry = useCallback(async (id, data) => {
    try {
      const updated = await journalService.update(id, data)
      setEntries((prev) => prev.map((e) => (e.id === id ? updated : e)))
      toast.success('Entry updated')
      return updated
    } catch (err) {
      toast.error('Failed to update entry')
      throw err
    }
  }, [toast])

  const deleteEntry = useCallback(async (id) => {
    try {
      await journalService.delete(id)
      setEntries((prev) => prev.filter((e) => e.id !== id))
      toast.success('Entry removed')
    } catch (err) {
      toast.error('Failed to delete entry')
      throw err
    }
  }, [toast])

  return { entries, loading, error, fetchEntries, createEntry, updateEntry, deleteEntry }
}
