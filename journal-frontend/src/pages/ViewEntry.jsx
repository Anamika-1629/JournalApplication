import { useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { useJournal } from '../hooks/useJournal'
import journalService from '../services/journalService'
import Loader from '../components/ui/Loader'
import Button from '../components/ui/Button'
import Modal from '../components/ui/Modal'
import ErrorMessage from '../components/ui/ErrorMessage'
import { formatDate, formatTime } from '../utils/helpers'
import styles from './ViewEntry.module.css'

export default function ViewEntry() {
  const { id } = useParams()
  const { deleteEntry } = useJournal()
  const navigate = useNavigate()

  const [entry, setEntry] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [deleteModal, setDeleteModal] = useState(false)
  const [deleting, setDeleting] = useState(false)

  useEffect(() => {
    journalService.getById(id)
      .then(setEntry)
      .catch(() => setError('This entry could not be found.'))
      .finally(() => setLoading(false))
  }, [id])

  const handleDelete = async () => {
    setDeleting(true)
    try {
      await deleteEntry(id)
      navigate('/dashboard')
    } finally {
      setDeleting(false)
    }
  }

  if (loading) return <Loader fullPage text="Loading entry…" />

  if (error) return (
    <div className={styles.page}>
      <div className={styles.container}>
        <button className={styles.back} onClick={() => navigate('/dashboard')}>
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5">
            <line x1="19" y1="12" x2="5" y2="12"/>
            <polyline points="12 19 5 12 12 5"/>
          </svg>
          Back to journal
        </button>
        <ErrorMessage message={error} />
      </div>
    </div>
  )

  return (
    <div className={styles.page}>
      <div className={styles.container}>
        {/* Nav */}
        <div className={styles.topBar}>
          <button className={styles.back} onClick={() => navigate('/dashboard')}>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5">
              <line x1="19" y1="12" x2="5" y2="12"/>
              <polyline points="12 19 5 12 12 5"/>
            </svg>
            Back to journal
          </button>
          <div className={styles.entryActions}>
            <Button variant="ghost" size="sm" onClick={() => navigate(`/journal/edit/${id}`)}>
              <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/>
                <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/>
              </svg>
              Edit
            </Button>
            <Button variant="danger" size="sm" onClick={() => setDeleteModal(true)}>
              <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <polyline points="3 6 5 6 21 6"/>
                <path d="M19 6l-1 14a2 2 0 01-2 2H8a2 2 0 01-2-2L5 6"/>
                <path d="M10 11v6M14 11v6"/>
              </svg>
              Delete
            </Button>
          </div>
        </div>

        {/* Entry */}
        <article className={styles.article}>
          <div className={styles.entryMeta}>
            <time className={styles.date}>{formatDate(entry.createdAt)}</time>
            <span className={styles.time}>{formatTime(entry.createdAt)}</span>
          </div>
          <h1 className={styles.title}>{entry.title}</h1>
          <div className={styles.divider} />
          <div className={styles.content}>
            {entry.content.split('\n').map((para, i) =>
              para.trim() ? <p key={i}>{para}</p> : <br key={i} />
            )}
          </div>
        </article>
      </div>

      <Modal
        isOpen={deleteModal}
        onClose={() => setDeleteModal(false)}
        onConfirm={handleDelete}
        title="Delete this entry?"
        message={`"${entry?.title}" will be permanently removed. This action cannot be undone.`}
        confirmLabel="Delete entry"
        loading={deleting}
      />
    </div>
  )
}
