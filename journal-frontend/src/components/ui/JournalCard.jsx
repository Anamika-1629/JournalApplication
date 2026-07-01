import { useNavigate } from 'react-router-dom'
import { truncate, formatDateShort } from '../../utils/helpers'
import styles from './JournalCard.module.css'

export default function JournalCard({ entry, onDelete }) {
  const navigate = useNavigate()

  return (
    <article className={styles.card}>
      <div className={styles.meta}>
        <time className={styles.date}>{formatDateShort(entry.createdAt)}</time>
      </div>

      <h3 className={styles.title}>{entry.title}</h3>
      <p className={styles.preview}>{truncate(entry.content)}</p>

      <div className={styles.footer}>
        <button className={styles.openBtn} onClick={() => navigate(`/journal/${entry.id}`)}>
          Read
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5">
            <line x1="5" y1="12" x2="19" y2="12"/>
            <polyline points="12 5 19 12 12 19"/>
          </svg>
        </button>

        <div className={styles.actions}>
          <button
            className={styles.actionBtn}
            onClick={() => navigate(`/journal/edit/${entry.id}`)}
            aria-label="Edit entry"
          >
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/>
              <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/>
            </svg>
            Edit
          </button>
          <button
            className={[styles.actionBtn, styles.deleteBtn].join(' ')}
            onClick={() => onDelete(entry)}
            aria-label="Delete entry"
          >
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <polyline points="3 6 5 6 21 6"/>
              <path d="M19 6l-1 14a2 2 0 01-2 2H8a2 2 0 01-2-2L5 6"/>
              <path d="M10 11v6M14 11v6"/>
              <path d="M9 6V4a1 1 0 011-1h4a1 1 0 011 1v2"/>
            </svg>
            Delete
          </button>
        </div>
      </div>
    </article>
  )
}
