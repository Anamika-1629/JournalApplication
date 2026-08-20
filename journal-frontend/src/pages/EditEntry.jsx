import { useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { useJournal } from '../hooks/useJournal'
import journalService from '../services/journalService'
import Loader from '../components/ui/Loader'
import Button from '../components/ui/Button'
import ErrorMessage from '../components/ui/ErrorMessage'
import styles from './EntryForm.module.css'

export default function EditEntry() {
  const { id } = useParams()
  const { updateEntry } = useJournal()
  const navigate = useNavigate()

  const [form, setForm] = useState({ title: '', content: '' })
  const [errors, setErrors] = useState({})
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [fetchError, setFetchError] = useState('')

  useEffect(() => {
    journalService.getById(id)
      .then((data) => {
        setForm({ title: data.title, content: data.content })
      })
      .catch(() => setFetchError('Could not load this entry.'))
      .finally(() => setLoading(false))
  }, [id])

  const validate = () => {
    const errs = {}
    if (!form.title.trim()) errs.title = 'Title is required'
    if (!form.content.trim()) errs.content = 'Content is required'
    return errs
  }

  const handleChange = (e) => {
    const { name, value } = e.target
    setForm((prev) => ({ ...prev, [name]: value }))
    if (errors[name]) setErrors((prev) => ({ ...prev, [name]: '' }))
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    const errs = validate()
    if (Object.keys(errs).length) { setErrors(errs); return }
    setSaving(true)
    try {
      await updateEntry(id, form)
      navigate(`/journal/${id}`)
    } catch {
      setSaving(false)
    }
  }

  const wordCount = form.content.trim() ? form.content.trim().split(/\s+/).length : 0

  if (loading) return <Loader fullPage text="Loading entry…" />
  if (fetchError) return (
    <div className={styles.page}>
      <div className={styles.container}>
        <ErrorMessage message={fetchError} />
      </div>
    </div>
  )

  return (
    <div className={styles.page}>
      <div className={styles.container}>
        <div className={styles.topBar}>
          <button className={styles.back} onClick={() => navigate(-1)}>
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5">
              <line x1="19" y1="12" x2="5" y2="12"/>
              <polyline points="12 19 5 12 12 5"/>
            </svg>
            Back
          </button>
          <div className={styles.actions}>
            <Button variant="ghost" onClick={() => navigate(-1)} disabled={saving}>
              Cancel
            </Button>
            <Button onClick={handleSubmit} loading={saving}>
              Save changes
            </Button>
          </div>
        </div>

        <form onSubmit={handleSubmit} className={styles.form} noValidate>
          <div className={styles.titleWrap}>
            <input
              name="title"
              value={form.title}
              onChange={handleChange}
              placeholder="Entry title…"
              className={[styles.titleInput, errors.title ? styles.titleError : ''].join(' ')}
              autoFocus
            />
            {errors.title && <span className={styles.errorMsg}>{errors.title}</span>}
          </div>

          <div className={styles.contentWrap}>
            <textarea
              name="content"
              value={form.content}
              onChange={handleChange}
              placeholder="What's on your mind?"
              className={[styles.contentInput, errors.content ? styles.contentError : ''].join(' ')}
              rows={20}
            />
            {errors.content && <span className={styles.errorMsg}>{errors.content}</span>}
          </div>

          <div className={styles.meta}>
            <span className={styles.wordCount}>{wordCount} {wordCount === 1 ? 'word' : 'words'}</span>
          </div>
        </form>
      </div>
    </div>
  )
}
