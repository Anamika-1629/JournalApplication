import { useState, useEffect, useMemo } from 'react'
import { useNavigate } from 'react-router-dom'
import { useUser } from '../contexts/UserContext'
import { useJournal } from '../hooks/useJournal'
import JournalCard from '../components/ui/JournalCard'
import SearchBar from '../components/ui/SearchBar'
import Button from '../components/ui/Button'
import Loader from '../components/ui/Loader'
import EmptyState from '../components/ui/EmptyState'
import Modal from '../components/ui/Modal'
import Pagination from '../components/ui/Pagination'
import ErrorMessage from '../components/ui/ErrorMessage'
import { getGreeting } from '../utils/helpers'
import styles from './Dashboard.module.css'

const PAGE_SIZE = 9
const SORT_OPTIONS = [
  { value: 'newest', label: 'Newest first' },
  { value: 'oldest', label: 'Oldest first' },
  { value: 'az', label: 'A → Z' },
]

export default function Dashboard() {
  const { user } = useUser()
  const { entries, loading, error, fetchEntries, deleteEntry } = useJournal()
  const navigate = useNavigate()

  const [search, setSearch] = useState('')
  const [sort, setSort] = useState('newest')
  const [page, setPage] = useState(1)
  const [deleteModal, setDeleteModal] = useState({ open: false, entry: null })
  const [deleting, setDeleting] = useState(false)

  useEffect(() => { fetchEntries() }, [fetchEntries])

  // Reset page when search/sort changes
  useEffect(() => { setPage(1) }, [search, sort])

  const filtered = useMemo(() => {
    let result = entries.filter((e) => {
      const q = search.toLowerCase()
      return e.title.toLowerCase().includes(q) || e.content.toLowerCase().includes(q)
    })
    if (sort === 'newest') result = result.slice().sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
    else if (sort === 'oldest') result = result.slice().sort((a, b) => new Date(a.createdAt) - new Date(b.createdAt))
    else if (sort === 'az') result = result.slice().sort((a, b) => a.title.localeCompare(b.title))
    return result
  }, [entries, search, sort])

  const totalPages = Math.ceil(filtered.length / PAGE_SIZE)
  const paged = filtered.slice((page - 1) * PAGE_SIZE, page * PAGE_SIZE)

  const handleDeleteRequest = (entry) => setDeleteModal({ open: true, entry })
  const handleDeleteCancel = () => setDeleteModal({ open: false, entry: null })

  const handleDeleteConfirm = async () => {
    if (!deleteModal.entry) return
    setDeleting(true)
    try {
      await deleteEntry(deleteModal.entry.id)
      setDeleteModal({ open: false, entry: null })
    } finally {
      setDeleting(false)
    }
  }

  const displayName = user?.userName || 'there'

  return (
    <div className={styles.page}>
      <div className={styles.container}>
        {/* Header */}
        <div className={styles.header}>
          <div>
            <p className={styles.greeting}>{getGreeting(displayName)}</p>
            <h1 className={styles.title}>My Journal</h1>
          </div>
          <Button onClick={() => navigate('/journal/new')} size="md">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5">
              <line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/>
            </svg>
            New entry
          </Button>
        </div>

        {/* Toolbar */}
        {!loading && entries.length > 0 && (
          <div className={styles.toolbar}>
            <div className={styles.searchWrap}>
              <SearchBar value={search} onChange={setSearch} />
            </div>
            <select
              value={sort}
              onChange={(e) => setSort(e.target.value)}
              className={styles.sortSelect}
              aria-label="Sort entries"
            >
              {SORT_OPTIONS.map((o) => (
                <option key={o.value} value={o.value}>{o.label}</option>
              ))}
            </select>
          </div>
        )}

        {/* Stats bar */}
        {!loading && entries.length > 0 && (
          <div className={styles.stats}>
            <span>{entries.length} {entries.length === 1 ? 'entry' : 'entries'}</span>
            {search && <span>· {filtered.length} matching "{search}"</span>}
          </div>
        )}

        {/* Content */}
        {loading ? (
          <Loader fullPage text="Loading your journal…" />
        ) : error ? (
          <ErrorMessage message={error} onRetry={fetchEntries} />
        ) : entries.length === 0 ? (
          <EmptyState
            title="Your journal is empty"
            description="Write your first entry and start capturing your thoughts, ideas, and memories."
            actionLabel="Write your first entry"
            onAction={() => navigate('/journal/new')}
          />
        ) : filtered.length === 0 ? (
          <EmptyState
            title="No entries found"
            description={`No entries match "${search}". Try a different search.`}
            actionLabel="Clear search"
            onAction={() => setSearch('')}
          />
        ) : (
          <>
            <div className={styles.grid}>
              {paged.map((entry, i) => (
                <div key={entry.id} style={{ animationDelay: `${i * 40}ms` }}>
                  <JournalCard entry={entry} onDelete={handleDeleteRequest} />
                </div>
              ))}
            </div>
            <Pagination currentPage={page} totalPages={totalPages} onPageChange={setPage} />
          </>
        )}
      </div>

      <Modal
        isOpen={deleteModal.open}
        onClose={handleDeleteCancel}
        onConfirm={handleDeleteConfirm}
        title="Delete this entry?"
        message={`"${deleteModal.entry?.title}" will be permanently removed. This cannot be undone.`}
        confirmLabel="Delete entry"
        loading={deleting}
      />
    </div>
  )
}
